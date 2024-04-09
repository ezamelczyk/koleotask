package ernest.zamelczyk.koleotask

import android.location.Location.distanceBetween
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ernest.zamelczyk.koleotask.data.StationsRepository
import ernest.zamelczyk.koleotask.ui.components.StationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DistanceInfoCardState(
    val homeName: String,
    val destinationName: String,
    val distance: String
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stationsRepository: StationsRepository
) : ViewModel() {

    private val queryState = MutableStateFlow("")
    private val selectionState = MutableStateFlow(emptySet<Long>())

    val query: StateFlow<String> = queryState.asStateFlow()
    val stations: StateFlow<List<StationItem>> = flow { emit(stationsRepository.getStations()) }
        .flatMapLatest { stations -> selectionState.map { stations.toStationItems(it) } }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val searchResults: StateFlow<List<StationItem>> = queryState
        .combine(selectionState, ::mapQueryToState)
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val distanceInfoCardState = selectionState
        .mapLatest(::mapToDistanceInfoCardState)
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, DistanceInfoCardState("", "", ""))

    fun onQueryChanged(query: String) {
        queryState.value = query
    }

    fun itemCheckedChange(id: Long, checked: Boolean) {
        selectionState.update { state ->
            when {
                checked && state.size < 2 -> state + id
                !checked && state.contains(id) -> state - id
                else -> state
            }
        }
    }

    fun onRemoveHome() {
        selectionState.update { state ->
            state - state.first()
        }
    }

    fun onRemoveDestination() {
        selectionState.update { state ->
            state - state.last()
        }
    }

    private suspend fun mapToDistanceInfoCardState(selectedIds: Set<Long>): DistanceInfoCardState {
        val stations = stationsRepository.getByIds(selectedIds)
        val home = stations.getOrNull(0)
        val destination = stations.getOrNull(1)
        val distance =
            if (home?.latitude != null && home.longitude != null && destination?.latitude != null && destination.longitude != null) {
                val resultsArray = FloatArray(1)
                distanceBetween(
                    home.latitude,
                    home.longitude,
                    destination.latitude,
                    destination.longitude,
                    resultsArray
                )
                String.format("%.2f km", resultsArray.first() / 1000f)
            } else "0 km"
        return DistanceInfoCardState(
            homeName = home?.name.orEmpty(),
            destinationName = destination?.name.orEmpty(),
            distance = distance
        )
    }

    private suspend fun mapQueryToState(
        query: String,
        selectionState: Set<Long>
    ): List<StationItem> {
        return stationsRepository.query(query).toStationItems(selectionState)
    }

    private fun List<Station>.toStationItems(
        selectedItemsSet: Set<Long>
    ): List<StationItem> {
        val selectedItems = mutableListOf<StationItem>()
        val mapped = mapNotNull {
            if (selectedItemsSet.contains(it.id)) {
                selectedItems.add(
                    StationItem(
                        id = it.id,
                        name = it.name,
                        city = it.city.orEmpty(),
                        selected = true,
                        selectable = true
                    )
                )
                null
            } else {
                StationItem(
                    id = it.id,
                    name = it.name,
                    city = it.city.orEmpty(),
                    selected = false,
                    selectable = selectedItemsSet.size < 2
                )
            }
        }
        return selectedItems + mapped
    }

}