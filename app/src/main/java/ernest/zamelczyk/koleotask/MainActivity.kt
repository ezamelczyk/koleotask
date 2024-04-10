package ernest.zamelczyk.koleotask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ModeOfTravel
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import ernest.zamelczyk.koleotask.ui.components.SearchBox
import ernest.zamelczyk.koleotask.ui.theme.KoleoTaskTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchStations()
            }
        }

        enableEdgeToEdge()

        setContent {
            KoleoTaskTheme {
                val homeSearchResults = viewModel.homeSearchResults.collectAsState()
                val destinationSearchResults = viewModel.destinationSearchResults.collectAsState()
                val homeState = viewModel.homeState.collectAsState()
                val destinationState = viewModel.destinationState.collectAsState()
                val distance = viewModel.distanceState.collectAsState()


                ElevatedCard(
                    modifier = Modifier
                        .safeDrawingPadding()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SearchBox(
                            modifier = Modifier.fillMaxWidth(),
                            query = homeState.value,
                            searchResults = homeSearchResults.value,
                            onQueryChanged = viewModel::onHomeChanged,
                            onItemClick = viewModel::onHomeSelected,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                if (homeState.value.isNotBlank()) {
                                    Icon(
                                        modifier = Modifier.clickable(onClick = viewModel::onHomeCleared),
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                        SearchBox(
                            modifier = Modifier.fillMaxWidth(),
                            query = destinationState.value,
                            searchResults = destinationSearchResults.value,
                            onQueryChanged = viewModel::onDestinationChanged,
                            onItemClick = viewModel::onDestinationSelected,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                if (destinationState.value.isNotBlank()) {
                                    Icon(
                                        modifier = Modifier.clickable(onClick = viewModel::onDestinationCleared),
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ModeOfTravel,
                                    contentDescription = null
                                )
                            },
                            value = distance.value,
                            onValueChange = {},
                            enabled = false
                        )
                    }
                }
            }
        }
    }
}