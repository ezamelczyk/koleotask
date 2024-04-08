package ernest.zamelczyk.koleotask.api

import retrofit2.http.GET

interface KoleoService {
    @GET("stations")
    suspend fun getStations(): List<StationModel>

    @GET("station_keywords")
    suspend fun getKeywords(): List<KeywordModel>

}