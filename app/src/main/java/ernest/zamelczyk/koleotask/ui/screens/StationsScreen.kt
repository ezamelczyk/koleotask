package ernest.zamelczyk.koleotask.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ernest.zamelczyk.koleotask.ui.components.ErrorState
import ernest.zamelczyk.koleotask.ui.components.LoadingState
import ernest.zamelczyk.koleotask.ui.components.StationItem

sealed class StationsState {
    object Error : StationsState()
    object Loading : StationsState()
    data class Stations(
        val stations: List<StationItem>
    ) : StationsState()
}

@Composable
fun StationsScreen(state: StationsState) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (state) {
            StationsState.Error -> ErrorState()
            StationsState.Loading -> LoadingState()
            is StationsState.Stations -> Stations(stations = state.stations)
        }
    }
}

@Composable
fun Stations(stations: List<StationItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(stations) {
            StationItem(it)
        }
    }
}