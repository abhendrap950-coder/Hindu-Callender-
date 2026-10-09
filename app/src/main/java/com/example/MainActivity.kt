package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.panchang.ui.AppScreen
import com.example.panchang.ui.MainViewModel
import com.example.panchang.ui.components.AppDrawerContent
import com.example.panchang.ui.screens.*
import com.example.ui.theme.HinduPanchangTheme
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val themeSetting by viewModel.themeFlow.collectAsState()

            val isDarkTheme = when (themeSetting) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }

            HinduPanchangTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HinduPanchangApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun HinduPanchangApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val selectedYearMonth by viewModel.calendarYearMonth.collectAsState()
    val dailyPanchangDate by viewModel.dailyPanchangDate.collectAsState()

    val themeSetting by viewModel.themeFlow.collectAsState()
    val audioEnabled by viewModel.audioEnabledFlow.collectAsState()
    val notifFestivals by viewModel.notifFestivalsFlow.collectAsState()
    val notifVrats by viewModel.notifVratsFlow.collectAsState()
    val openDailyCard by viewModel.openDailyCardFlow.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // 1. OPENING SCREEN: Daily Premium 9:16 Card
    if (currentScreen == AppScreen.DAILY_CARD) {
        val todayPanchang = remember(currentLocation) {
            viewModel.getTodayPanchang()
        }
        DailyCardScreen(
            panchang = todayPanchang,
            onNavigateHome = {
                viewModel.navigateTo(AppScreen.HOME)
            }
        )
    } else {
        // Main App with Drawer Navigation
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawerContent(
                    currentScreen = currentScreen,
                    onNavigate = { screen ->
                        viewModel.navigateTo(screen)
                    },
                    onCloseDrawer = {
                        coroutineScope.launch { drawerState.close() }
                    }
                )
            }
        ) {
            when (currentScreen) {
                AppScreen.HOME -> {
                    val todayPanchang = remember(currentLocation) {
                        viewModel.getTodayPanchang()
                    }
                    val monthDays = remember(selectedYearMonth, currentLocation) {
                        viewModel.getCalendarMonthDays()
                    }
                    val monthFestivals = remember(selectedYearMonth, currentLocation) {
                        viewModel.getCalendarMonthFestivals()
                    }

                    HomeScreen(
                        currentPanchang = todayPanchang,
                        monthDays = monthDays,
                        monthFestivals = monthFestivals,
                        selectedYearMonth = selectedYearMonth,
                        onPreviousMonth = { viewModel.previousCalendarMonth() },
                        onNextMonth = { viewModel.nextCalendarMonth() },
                        onDateSelected = { date ->
                            viewModel.selectDateAndOpenDetail(date)
                        },
                        onOpenMenu = {
                            coroutineScope.launch { drawerState.open() }
                        },
                        onOpenLocationSettings = {
                            viewModel.navigateTo(AppScreen.SETTINGS)
                        },
                        onOpenDailyCard = {
                            viewModel.navigateTo(AppScreen.DAILY_CARD)
                        }
                    )
                }

                AppScreen.DATE_DETAIL -> {
                    val datePanchang = remember(viewModel.selectedDate.collectAsState().value, currentLocation) {
                        viewModel.getSelectedDatePanchang()
                    }
                    DateDetailScreen(
                        panchang = datePanchang,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.CALENDAR -> {
                    val monthDays = remember(selectedYearMonth, currentLocation) {
                        viewModel.getCalendarMonthDays()
                    }
                    val monthFestivals = remember(selectedYearMonth, currentLocation) {
                        viewModel.getCalendarMonthFestivals()
                    }

                    CalendarScreen(
                        selectedYearMonth = selectedYearMonth,
                        monthDays = monthDays,
                        monthFestivals = monthFestivals,
                        onPreviousMonth = { viewModel.previousCalendarMonth() },
                        onNextMonth = { viewModel.nextCalendarMonth() },
                        onDateSelected = { date ->
                            viewModel.selectDateAndOpenDetail(date)
                        },
                        onFestivalSelected = { festival ->
                            // Find the date for this festival or open directly
                            val matchDay = monthDays.find { it.festivals.any { f -> f.id == festival.id } }
                            if (matchDay != null) {
                                val matchDate = LocalDate.of(matchDay.year, matchDay.monthOfYear, matchDay.dayOfMonth)
                                viewModel.selectDateAndOpenDetail(matchDate)
                            }
                        },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.VRAT_FESTIVALS -> {
                    val vrats = remember(selectedYearMonth, currentLocation) {
                        viewModel.getMonthVrats()
                    }
                    val festivals = remember(selectedYearMonth, currentLocation) {
                        viewModel.getCalendarMonthFestivals()
                    }

                    VratFestivalsScreen(
                        vrats = vrats,
                        festivals = festivals,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.DAILY_PANCHANG -> {
                    val dailyPanchang = remember(dailyPanchangDate, currentLocation) {
                        viewModel.getDailyPanchangForDate()
                    }

                    DailyPanchangScreen(
                        currentDate = dailyPanchangDate,
                        panchang = dailyPanchang,
                        onPreviousDay = { viewModel.previousDailyPanchangDay() },
                        onToday = { viewModel.resetDailyPanchangToToday() },
                        onNextDay = { viewModel.nextDailyPanchangDay() },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.SETTINGS -> {
                    SettingsScreen(
                        currentLocation = currentLocation,
                        onCitySelected = { loc -> viewModel.setLocation(loc) },
                        theme = themeSetting,
                        onThemeChanged = { th -> viewModel.setTheme(th) },
                        audioEnabled = audioEnabled,
                        onAudioEnabledChanged = { en -> viewModel.setAudioEnabled(en) },
                        notifFestivals = notifFestivals,
                        onNotifFestivalsChanged = { en -> viewModel.setNotifFestivals(en) },
                        notifVrats = notifVrats,
                        onNotifVratsChanged = { en -> viewModel.setNotifVrats(en) },
                        openDailyCard = openDailyCard,
                        onOpenDailyCardChanged = { en -> viewModel.setOpenDailyCard(en) },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.ABOUT -> {
                    AboutScreen(
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                else -> {}
            }
        }
    }
}
