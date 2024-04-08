package ernest.zamelczyk.koleotask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import ernest.zamelczyk.koleotask.ui.screens.StationsScreen
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

        setContent {
            KoleoTaskTheme {
                val stationsState = viewModel.state.collectAsState()
                StationsScreen(state = stationsState.value)
            }
        }
    }
}