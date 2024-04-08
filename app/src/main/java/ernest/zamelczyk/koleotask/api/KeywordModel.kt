package ernest.zamelczyk.koleotask.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KeywordModel(
    @SerialName("id") val id: Long,
    @SerialName("keyword") val keyword: String,
    @SerialName("station_id") val stationId: Long
)
