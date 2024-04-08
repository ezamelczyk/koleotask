package ernest.zamelczyk.koleotask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ernest.zamelczyk.koleotask.api.KoleoHeaderInterceptor
import ernest.zamelczyk.koleotask.api.KoleoService
import ernest.zamelczyk.koleotask.api.Station
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

sealed class MainState {
    object Error: MainState()
    object Loading: MainState()
    data class Stations(val stations: List<Station>): MainState()
}

class MainViewModel: ViewModel() {
    private val json = Json { ignoreUnknownKeys = true }
    private val koleoService = Retrofit.Builder()
        .client(OkHttpClient.Builder().addNetworkInterceptor(KoleoHeaderInterceptor()).build())
        .baseUrl("https://koleo.pl/api/v2/main/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(KoleoService::class.java)

    private val mutableState: MutableStateFlow<MainState> = MutableStateFlow(MainState.Loading)
    val state: StateFlow<MainState> = mutableState.asStateFlow()

    fun fetchStations() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mutableState.emit(MainState.Stations(koleoService.getStations()))
            } catch (e: Exception) {
                e.printStackTrace()
                mutableState.emit(MainState.Error)
            }
        }
    }

}