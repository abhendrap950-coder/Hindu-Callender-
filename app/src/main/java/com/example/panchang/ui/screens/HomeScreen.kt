package com.example.panchang.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.panchang.model.DailyPanchang
import com.example.panchang.model.FestivalItem
import com.example.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentPanchang: DailyPanchang,
    monthDays: List<DailyPanchang>,
    monthFestivals: List<FestivalItem>,
    selectedYearMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onOpenMenu: () -> Unit,
    onOpenLocationSettings: () -> Unit,
    onOpenDailyCard: () -> Unit
) {
    val weekdaysHindi = listOf("रवि", "सोम", "मंगल", "बुध", "गुरु", "शुक्र", "शनि")
    val today = LocalDate.now()

    // First day of month offset
    val firstDayOfMonth = selectedYearMonth.atDay(1)
    val firstDayOfWeekIndex = (firstDayOfMonth.dayOfWeek.value % 7)
    val totalGridCells = firstDayOfWeekIndex + monthDays.size

    // Selected date in the calendar (defaults to today if same month, or 1st of month)
    var selectedCalendarDate by remember(selectedYearMonth) {
        val initial = if (today.year == selectedYearMonth.year && today.monthValue == selectedYearMonth.monthValue) {
            today
        } else {
            selectedYearMonth.atDay(1)
        }
        mutableStateOf(initial)
    }

    val selectedDayPanchang = remember(selectedCalendarDate, monthDays) {
        monthDays.find {
            it.dayOfMonth == selectedCalendarDate.dayOfMonth &&
                    it.monthOfYear == selectedCalendarDate.monthValue &&
                    it.year == selectedCalendarDate.year
        } ?: currentPanchang
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onOpenLocationSettings() }
                            .padding(vertical = 4.dp)
                            .testTag("home_location_selector")
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "स्थान",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentPanchang.cityName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onOpenMenu,
                        modifier = Modifier.testTag("home_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "मेनू (Navigation Menu)",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    FilledTonalButton(
                        onClick = onOpenDailyCard,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("home_top_daily_card_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "आज का पंचांग",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ========================================================
            // 1. "आज का पंचांग देखें" PROMINENT ACTION BANNER / BUTTON
            // ========================================================
            Button(
                onClick = onOpenDailyCard,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("home_open_daily_panchang_card_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaroonPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🪔",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "आज का पंचांग देखें",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFF9E6),
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // ========================================================
            // 2. GENERAL MONTHLY CALENDAR (मासिक कैलेंडर)
            // ========================================================
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.2.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_monthly_calendar_card")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header with Month / Year and Prev / Next Arrow Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onPreviousMonth,
                            modifier = Modifier.testTag("home_calendar_prev_month_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "पिछला महीना",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${getHindiMonthName(selectedYearMonth.monthValue)} ${selectedYearMonth.year}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            if (monthDays.isNotEmpty()) {
                                Text(
                                    text = "${monthDays.first().hinduMonthHindi} - ${monthDays.last().hinduMonthHindi} मास",
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(
                            onClick = onNextMonth,
                            modifier = Modifier.testTag("home_calendar_next_month_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "अगला महीना",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 7 Weekday Columns (रवि से शनि)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        weekdaysHindi.forEachIndexed { index, day ->
                            val isSun = index == 0
                            Text(
                                text = day,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSun) InauspiciousRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Calendar Grid of Dates (7 columns)
                    Column {
                        val numRows = (totalGridCells + 6) / 7
                        for (row in 0 until numRows) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                for (col in 0 until 7) {
                                    val cellIndex = row * 7 + col
                                    val dayIndex = cellIndex - firstDayOfWeekIndex
                                    if (dayIndex in monthDays.indices) {
                                        val panchangItem = monthDays[dayIndex]
                                        val isToday = panchangItem.dayOfMonth == today.dayOfMonth &&
                                                panchangItem.monthOfYear == today.monthValue &&
                                                panchangItem.year == today.year

                                        val cellDate = LocalDate.of(
                                            panchangItem.year,
                                            panchangItem.monthOfYear,
                                            panchangItem.dayOfMonth
                                        )

                                        val isSelected = cellDate == selectedCalendarDate

                                        CalendarDateCell(
                                            panchang = panchangItem,
                                            isToday = isToday,
                                            isSelected = isSelected,
                                            isSunday = col == 0,
                                            onClick = {
                                                selectedCalendarDate = cellDate
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(58.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Legend Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                LegendItem(color = AuspiciousGreen, label = "एकादशी / पूर्णिमा")
                LegendItem(color = SaffronSecondary, label = "पर्व / व्रत")
                LegendItem(color = MaroonPrimary, label = "आज की तारीख")
            }

            // ========================================================
            // 3. SELECTED DATE PANCHANG QUICK SUMMARY & OPTION TO VIEW PANCHANG
            // ========================================================
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_selected_date_card")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "चुनी गई तारीख: ${selectedDayPanchang.gregorianFormatted} (${selectedDayPanchang.weekdayHindi})",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${selectedDayPanchang.hinduMonthHindi} मास, ${selectedDayPanchang.tithi.paksha} - ${selectedDayPanchang.tithi.name}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (selectedCalendarDate == today) {
                            Surface(
                                color = AuspiciousGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "आज",
                                    color = AuspiciousGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Key Tithi, Nakshatra, Muhurat Details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        QuickInfoTag(label = "तिथि", value = selectedDayPanchang.tithi.name)
                        QuickInfoTag(label = "नक्षत्र", value = selectedDayPanchang.nakshatra.name)
                        QuickInfoTag(label = "सूर्योदय", value = selectedDayPanchang.sunrise)
                        QuickInfoTag(label = "सूर्यास्त", value = selectedDayPanchang.sunset)
                    }

                    if (selectedDayPanchang.festivals.isNotEmpty()) {
                        Text(
                            text = "🎉 पर्व / त्योहार: ${selectedDayPanchang.festivals.joinToString { it.nameHindi }}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Option to view Panchang for this date
                    Button(
                        onClick = { onDateSelected(selectedCalendarDate) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_view_selected_date_panchang_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "इस तारीख का पंचांग देखें",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            // Month Festivals List preview (if any in this month)
            if (monthFestivals.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "🪔 इस माह के प्रमुख त्योहार एवं पर्व",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        monthFestivals.take(5).forEach { festival ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val matchDay = monthDays.find { it.festivals.any { f -> f.id == festival.id } }
                                        if (matchDay != null) {
                                            val d = LocalDate.of(matchDay.year, matchDay.monthOfYear, matchDay.dayOfMonth)
                                            onDateSelected(d)
                                        }
                                    }
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "• ${festival.nameHindi}",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = festival.dateFormatted,
                                    fontSize = 12.sp,
                                    color = SaffronSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CalendarDateCell(
    panchang: DailyPanchang,
    isToday: Boolean,
    isSelected: Boolean = false,
    isSunday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tithiLabel = when {
        panchang.festivals.isNotEmpty() -> panchang.festivals.first().nameHindi.take(4)
        panchang.tithi.isEkadashi -> "एकादशी"
        panchang.tithi.isPurnima -> "पूर्णिमा"
        panchang.tithi.isAmavasya -> "अमावस्या"
        panchang.tithi.number == 13 || panchang.tithi.number == 28 -> "प्रदोष"
        panchang.tithi.number == 4 || panchang.tithi.number == 19 -> "चतुर्थी"
        else -> panchang.tithi.name.take(4)
    }

    val isSpecial = panchang.tithi.isEkadashi || panchang.tithi.isPurnima ||
            panchang.tithi.isAmavasya || panchang.festivals.isNotEmpty()

    val cellBackground = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        isToday -> MaroonPrimary.copy(alpha = 0.18f)
        isSpecial -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        else -> Color.Transparent
    }

    val cellBorderColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isToday -> MaroonPrimary
        isSpecial -> GoldAccent.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
    }

    val cellBorderWidth = when {
        isSelected -> 2.dp
        isToday -> 1.5.dp
        else -> 0.5.dp
    }

    Box(
        modifier = modifier
            .height(58.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(cellBackground)
            .border(
                width = cellBorderWidth,
                color = cellBorderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .testTag("calendar_date_${panchang.dayOfMonth}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                text = "${panchang.dayOfMonth}",
                fontSize = 13.5.sp,
                fontWeight = if (isToday || isSelected || isSpecial) FontWeight.Bold else FontWeight.Medium,
                color = when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isToday -> MaroonPrimary
                    isSunday -> InauspiciousRed
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
            Text(
                text = tithiLabel,
                fontSize = 8.5.sp,
                fontWeight = if (isSpecial || isSelected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isSpecial -> SaffronSecondary
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun QuickInfoTag(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun getHindiMonthName(monthNumber: Int): String {
    return when (monthNumber) {
        1 -> "जनवरी"
        2 -> "फरवरी"
        3 -> "मार्च"
        4 -> "अप्रैल"
        5 -> "मई"
        6 -> "जून"
        7 -> "जुलाई"
        8 -> "अगस्त"
        9 -> "सितम्बर"
        10 -> "अक्टूबर"
        11 -> "नवम्बर"
        else -> "दिसम्बर"
    }
}
