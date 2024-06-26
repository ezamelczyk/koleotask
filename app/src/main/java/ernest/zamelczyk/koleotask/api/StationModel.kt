package ernest.zamelczyk.koleotask.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StationModel(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("name_slug") val nameSlug: String,
    @SerialName("latitude") val latitude: Double?,
    @SerialName("longitude") val longitude: Double?,
    @SerialName("hits") val hits: Long,
    @SerialName("ibnr") val ibnr: Long?,
    @SerialName("city") val city: String,
    @SerialName("region") val region: String,
    @SerialName("country") val country: String,
    @SerialName("localised_name") val localisedName: String?,
    @SerialName("is_group") val isGroup: Boolean,
    @SerialName("has_announcements") val hasAnnouncements: Boolean,
    @SerialName("is_nearby_station_enabled") val isNearbyStationEnabled: Boolean
)
