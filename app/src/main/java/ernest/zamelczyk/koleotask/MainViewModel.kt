package ernest.zamelczyk.koleotask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ernest.zamelczyk.koleotask.data.StationsRepository
import ernest.zamelczyk.koleotask.ui.components.StationItem
import ernest.zamelczyk.koleotask.ui.screens.StationsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stationsRepository: StationsRepository
) : ViewModel() {

    private val mutableState: MutableStateFlow<StationsState> =
        MutableStateFlow(StationsState.Loading)
    val state: StateFlow<StationsState> = mutableState.asStateFlow()

    fun fetchStations() {
        viewModelScope.launch {
            val stationItems = stationsRepository.getStations().map { StationItem(name = it.name) }
            mutableState.emit(StationsState.Stations(stationItems))
        }
    }

}