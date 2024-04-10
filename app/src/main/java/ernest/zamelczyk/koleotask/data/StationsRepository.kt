package ernest.zamelczyk.koleotask.data

import ernest.zamelczyk.koleotask.Keyword
import ernest.zamelczyk.koleotask.Station
import ernest.zamelczyk.koleotask.StationsDb
import ernest.zamelczyk.koleotask.api.KoleoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StationsRepository @Inject constructor(
    private val koleoService: KoleoService,
    private val database: StationsDb,
    private val prefs: AppPreferences
) {

    suspend fun getStations() = withContext(Dispatchers.IO) {
        val currentTimestamp = System.currentTimeMillis() / 1000L

        // if time since last successful request > 24h perform another request
        if (currentTimestamp - prefs.lastStationsFetchTimestamp > 86_400) {
            val stationsRequest = async { koleoService.getStations() }
            val keywordsRequest = async { koleoService.getKeywords() }
            val stations = try {
                stationsRequest.await()
            } catch (ex: Exception) {
                null
            }
            val keywords = try {
                keywordsRequest.await()
            } catch (ex: Exception) {
                null
            }
            if (stations != null && keywords != null) {
                replaceStationsInDatabase(stations)
                replaceKeywordsInDatabase(keywords)
                prefs.lastStationsFetchTimestamp = currentTimestamp
            }
        }
        database.stationQueries.get().executeAsList()
    }

    suspend fun query(query: String) = withContext(Dispatchers.IO) {
        database.stationQueries.query(query = query, mapper = ::Station).executeAsList()
    }

    suspend fun getById(id: Long) = withContext(Dispatchers.IO) {
        database.stationQueries.getById(id).executeAsOne()
    }

    private fun replaceStationsInDatabase(stations: List<ernest.zamelczyk.koleotask.api.StationModel>) {
        database.stationQueries.transaction {
            database.stationQueries.clearStations()
            stations.forEach {
                database.stationQueries.insertStation(
                    Station(
                        it.id,
                        it.name,
                        it.nameSlug,
                        it.latitude,
                        it.longitude,
                        it.hits,
                        it.ibnr,
                        it.city,
                        it.region,
                        it.country,
                        it.localisedName,
                        it.isGroup,
                        it.hasAnnouncements,
                        it.isNearbyStationEnabled
                    )
                )
            }
        }
    }

    private fun replaceKeywordsInDatabase(keywords: List<ernest.zamelczyk.koleotask.api.KeywordModel>) {
        database.stationQueries.transaction {
            database.stationQueries.clearKeywords()
            keywords.forEach {
                database.stationQueries.insertKeyword(
                    Keyword(it.id, it.keyword, it.stationId)
                )
            }
        }
    }

}