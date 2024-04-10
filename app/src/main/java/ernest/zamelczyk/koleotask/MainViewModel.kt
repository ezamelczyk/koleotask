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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stationsRepository: StationsRepository
) : ViewModel() {

    private val homeStation = MutableStateFlow<Station?>(null)
    private val destinationStation = MutableStateFlow<Station?>(null)
    private val _homeState = MutableStateFlow("")
    private val _destinationState = MutableStateFlow("")

    val homeState = _homeState.asStateFlow()
    val destinationState = _destinationState.asStateFlow()
    val distanceState = combine(homeStation, destinationStation, ::calculateDistance)
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.Eagerly, "0 km")
    val homeSearchResults: StateFlow<List<StationItem>> = homeState
        .mapLatest(::mapQueryToItems)
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val destinationSearchResults: StateFlow<List<StationItem>> = destinationState
        .mapLatest(::mapQueryToItems)
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun fetchStations() {
        viewModelScope.launch {
            stationsRepository.getStations()
        }
    }

    fun onHomeSelected(id: Long) {
        viewModelScope.launch {
            val station = stationsRepository.getById(id)
            homeStation.emit(station)
            _homeState.emit(station.name)
        }
    }

    fun onDestinationSelected(id: Long) {
        viewModelScope.launch {
            val station = stationsRepository.getById(id)
            destinationStation.emit(station)
            _destinationState.emit(station.name)
        }
    }

    fun onHomeChanged(value: String) {
        _homeState.value = value
    }

    fun onHomeCleared() {
        _homeState.value = ""
        homeStation.value = null
    }

    fun onDestinationChanged(value: String) {
        _destinationState.value = value
    }

    fun onDestinationCleared() {
        _destinationState.value = ""
        destinationStation.value = null
    }

    private suspend fun mapQueryToItems(query: String): List<StationItem> {
        return stationsRepository.query(query).toStationItems()
    }

    private fun calculateDistance(home: Station?, destination: Station?): String {
        return if (
            home?.latitude != null &&
            home.longitude != null &&
            destination?.latitude != null &&
            destination.longitude != null
        ) {
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
    }

    private fun List<Station>.toStationItems(): List<StationItem> {
        return map {
            StationItem(
                id = it.id,
                name = it.name,
                city = it.city.orEmpty()
            )
        }
    }

}