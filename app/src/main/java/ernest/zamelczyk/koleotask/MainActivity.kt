package ernest.zamelczyk.koleotask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import ernest.zamelczyk.koleotask.ui.components.DistanceInfoCard
import ernest.zamelczyk.koleotask.ui.components.SearchBox
import ernest.zamelczyk.koleotask.ui.components.Stations
import ernest.zamelczyk.koleotask.ui.theme.KoleoTaskTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KoleoTaskTheme {
                val stations = viewModel.stations.collectAsState()
                val searchResults = viewModel.searchResults.collectAsState()
                val query = viewModel.query.collectAsState()
                val distanceInfoCardState = viewModel.distanceInfoCardState.collectAsState()

                Scaffold(
                    topBar = {
                        SearchBox(
                            query = query.value,
                            searchResults = searchResults.value,
                            onQueryChanged = viewModel::onQueryChanged,
                            onItemCheckedChange = viewModel::itemCheckedChange
                        )
                    }) {
                    Stations(modifier = Modifier.fillMaxSize(),
                        contentPadding = it,
                        stations = stations.value,
                        onItemCheckedChange = viewModel::itemCheckedChange,
                        header = {
                            val state = distanceInfoCardState.value
                            DistanceInfoCard(
                                homeName = state.homeName,
                                destinationName = state.destinationName,
                                distance = state.distance,
                                onRemoveHome = viewModel::onRemoveHome,
                                onRemoveDestination = viewModel::onRemoveDestination
                            )
                        })
                }
            }
        }
    }
}