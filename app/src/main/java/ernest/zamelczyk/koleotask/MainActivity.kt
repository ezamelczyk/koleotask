package ernest.zamelczyk.koleotask

import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ernest.zamelczyk.koleotask.api.KoleoHeaderInterceptor
import ernest.zamelczyk.koleotask.api.KoleoService
import ernest.zamelczyk.koleotask.api.Station
import ernest.zamelczyk.koleotask.ui.theme.KoleoTaskTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchStations()
            }
        }

        setContent {
            KoleoTaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state = viewModel.state.collectAsState()
                    when (val capturedState = state.value) {
                        MainState.Error -> ErrorState()
                        MainState.Loading -> LoadingState()
                        is MainState.Stations -> Stations(stations = capturedState.stations)
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorState() {
    Text(text = "Error loading items")
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun Stations(stations: List<Station>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ){
        items(stations) {
            StationItem(station = it)
        }
    }
}

@Composable
fun StationItem(station: Station) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = station.name)
    }
}