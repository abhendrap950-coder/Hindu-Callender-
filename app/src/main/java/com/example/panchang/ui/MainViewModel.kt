package com.example.panchang.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.panchang.engine.PanchangRepository
import com.example.panchang.model.CityLocation
import com.example.panchang.model.DailyPanchang
import com.example.panchang.model.FestivalItem
import com.example.panchang.model.VratItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

enum class AppScreen {
    DAILY_CARD,
    HOME,
    DATE_DETAIL,
    CALENDAR,
    VRAT_FESTIVALS,
    DAILY_PANCHANG,
    DEVOTIONAL_CARDS,
    SETTINGS,
    ABOUT
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val repository = PanchangRepository(application)

    private val _currentScreen = MutableStateFlow(AppScreen.DAILY_CARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _calendarYearMonth = MutableStateFlow(YearMonth.now())
    val calendarYearMonth: StateFlow<YearMonth> = _calendarYearMonth.asStateFlow()

    private val _dailyPanchangDate = MutableStateFlow(LocalDate.now())
    val dailyPanchangDate: StateFlow<LocalDate> = _dailyPanchangDate.asStateFlow()

    private val _selectedFestivalForCard = MutableStateFlow<FestivalItem?>(null)
    val selectedFestivalForCard: StateFlow<FestivalItem?> = _selectedFestivalForCard.asStateFlow()

    val currentLocation: StateFlow<CityLocation> = repository.currentLocation

    val themeFlow = repository.themeFlow.stateIn(viewModelScope, SharingStarted.Eagerly, "system")
    val audioEnabledFlow = repository.audioEnabledFlow.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val notifFestivalsFlow = repository.notifFestivalsFlow.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val notifVratsFlow = repository.notifVratsFlow.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val openDailyCardFlow = repository.openDailyCardFlow.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    init {
        viewModelScope.launch {
            repository.loadSavedCity()
        }
    }

    fun navigateTo(screen: AppScreen) {
        if (screen != AppScreen.DEVOTIONAL_CARDS) {
            _selectedFestivalForCard.value = null
        }
        _currentScreen.value = screen
    }

    fun openCardGenerator(festival: FestivalItem? = null) {
        _selectedFestivalForCard.value = festival
        _currentScreen.value = AppScreen.DEVOTIONAL_CARDS
    }

    fun selectDateAndOpenDetail(date: LocalDate) {
        _selectedDate.value = date
        _currentScreen.value = AppScreen.DATE_DETAIL
    }

    fun previousCalendarMonth() {
        _calendarYearMonth.value = _calendarYearMonth.value.minusMonths(1)
    }

    fun nextCalendarMonth() {
        _calendarYearMonth.value = _calendarYearMonth.value.plusMonths(1)
    }

    fun previousDailyPanchangDay() {
        _dailyPanchangDate.value = _dailyPanchangDate.value.minusDays(1)
    }

    fun nextDailyPanchangDay() {
        _dailyPanchangDate.value = _dailyPanchangDate.value.plusDays(1)
    }

    fun resetDailyPanchangToToday() {
        _dailyPanchangDate.value = LocalDate.now()
    }

    fun setLocation(location: CityLocation) {
        viewModelScope.launch {
            repository.setLocation(location)
        }
    }

    fun setTheme(theme: String) {
        viewModelScope.launch { repository.setTheme(theme) }
    }

    fun setAudioEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setAudioEnabled(enabled) }
    }

    fun setNotifFestivals(enabled: Boolean) {
        viewModelScope.launch { repository.setNotifFestivals(enabled) }
    }

    fun setNotifVrats(enabled: Boolean) {
        viewModelScope.launch { repository.setNotifVrats(enabled) }
    }

    fun setOpenDailyCard(enabled: Boolean) {
        viewModelScope.launch { repository.setOpenDailyCard(enabled) }
    }

    fun getTodayPanchang(): DailyPanchang {
        return repository.getPanchang(LocalDate.now(), currentLocation.value)
    }

    fun getSelectedDatePanchang(): DailyPanchang {
        return repository.getPanchang(_selectedDate.value, currentLocation.value)
    }

    fun getDailyPanchangForDate(): DailyPanchang {
        return repository.getPanchang(_dailyPanchangDate.value, currentLocation.value)
    }

    fun getCalendarMonthDays(): List<DailyPanchang> {
        return repository.getMonthPanchang(_calendarYearMonth.value, currentLocation.value)
    }

    fun getCalendarMonthFestivals(): List<FestivalItem> {
        return repository.getMonthFestivals(_calendarYearMonth.value, currentLocation.value)
    }

    fun getMonthVrats(): List<VratItem> {
        return repository.getMonthVrats(_calendarYearMonth.value, currentLocation.value)
    }
}
