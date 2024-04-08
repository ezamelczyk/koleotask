package ernest.zamelczyk.koleotask.data

import android.content.SharedPreferences
import ernest.zamelczyk.koleotask.di.qualifiers.AppPrefs
import javax.inject.Inject

class AppPreferences @Inject constructor(
    @AppPrefs private val prefs: SharedPreferences
) {

    var lastStationsFetchTimestamp: Long
        get() = prefs.getLong(TIMESTAMP, 0)
        set(value) = prefs.edit().putLong(TIMESTAMP, value).apply()


    companion object {
        private const val TIMESTAMP = "last_stations_fetch_timestamp"
    }
}