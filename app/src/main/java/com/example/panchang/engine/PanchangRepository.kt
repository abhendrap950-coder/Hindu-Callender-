package com.example.panchang.engine

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.panchang.model.CityLocation
import com.example.panchang.model.DailyPanchang
import com.example.panchang.model.FestivalItem
import com.example.panchang.model.VratItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.YearMonth
import java.util.concurrent.ConcurrentHashMap

private val Context.dataStore by preferencesDataStore(name = "panchang_preferences")

class PanchangRepository(private val context: Context) {

    private val panchangCache = ConcurrentHashMap<String, DailyPanchang>()

    private val _currentLocation = MutableStateFlow(CityLocation.DEFAULT)
    val currentLocation: StateFlow<CityLocation> = _currentLocation.asStateFlow()

    // Settings keys
    private val KEY_CITY_ID = stringPreferencesKey("city_id")
    private val KEY_THEME = stringPreferencesKey("app_theme") // "system", "light", "dark"
    private val KEY_AUDIO_ENABLED = booleanPreferencesKey("audio_enabled")
    private val KEY_NOTIF_FESTIVALS = booleanPreferencesKey("notif_festivals")
    private val KEY_NOTIF_VRATS = booleanPreferencesKey("notif_vrats")
    private val KEY_OPEN_DAILY_CARD = booleanPreferencesKey("open_daily_card")

    val themeFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_THEME] ?: "system"
    }

    val audioEnabledFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_AUDIO_ENABLED] ?: true
    }

    val notifFestivalsFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_NOTIF_FESTIVALS] ?: true
    }

    val notifVratsFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_NOTIF_VRATS] ?: true
    }

    val openDailyCardFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_OPEN_DAILY_CARD] ?: true
    }

    suspend fun loadSavedCity() {
        context.dataStore.data.collect { prefs ->
            val savedCityId = prefs[KEY_CITY_ID] ?: "prayagraj"
            val match = CityLocation.MAJOR_CITIES.find { it.id == savedCityId } ?: CityLocation.DEFAULT
            _currentLocation.value = match
        }
    }

    suspend fun setLocation(location: CityLocation) {
        _currentLocation.value = location
        panchangCache.clear()
        context.dataStore.edit { prefs ->
            prefs[KEY_CITY_ID] = location.id
        }
    }

    suspend fun setCustomLocation(name: String, state: String, lat: Double, lon: Double) {
        val customLoc = CityLocation(
            id = "custom_${lat}_${lon}",
            nameHindi = name,
            nameEnglish = name,
            stateHindi = state,
            latitude = lat,
            longitude = lon
        )
        setLocation(customLoc)
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME] = theme
        }
    }

    suspend fun setAudioEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_AUDIO_ENABLED] = enabled
        }
    }

    suspend fun setNotifFestivals(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NOTIF_FESTIVALS] = enabled
        }
    }

    suspend fun setNotifVrats(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NOTIF_VRATS] = enabled
        }
    }

    suspend fun setOpenDailyCard(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_OPEN_DAILY_CARD] = enabled
        }
    }

    fun getPanchang(date: LocalDate, location: CityLocation = _currentLocation.value): DailyPanchang {
        val key = "${date}_${location.id}"
        return panchangCache.computeIfAbsent(key) {
            AstronomicalCalculator.calculatePanchang(date, location)
        }
    }

    fun getMonthPanchang(yearMonth: YearMonth, location: CityLocation = _currentLocation.value): List<DailyPanchang> {
        val daysInMonth = yearMonth.lengthOfMonth()
        val list = ArrayList<DailyPanchang>(daysInMonth)
        for (day in 1..daysInMonth) {
            val date = yearMonth.atDay(day)
            list.add(getPanchang(date, location))
        }
        return list
    }

    fun getMonthFestivals(yearMonth: YearMonth, location: CityLocation = _currentLocation.value): List<FestivalItem> {
        val monthDays = getMonthPanchang(yearMonth, location)
        val allFestivals = mutableListOf<FestivalItem>()
        for (day in monthDays) {
            allFestivals.addAll(day.festivals)
        }
        return allFestivals.distinctBy { it.id }
    }

    fun getMonthVrats(yearMonth: YearMonth, location: CityLocation = _currentLocation.value): List<VratItem> {
        val monthDays = getMonthPanchang(yearMonth, location)
        val allVrats = mutableListOf<VratItem>()
        for (day in monthDays) {
            allVrats.addAll(day.vrats)
        }
        return allVrats.distinctBy { it.id }
    }
}
