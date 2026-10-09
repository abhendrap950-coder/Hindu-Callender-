package com.example.panchang.engine

import com.example.panchang.model.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.*

object AstronomicalCalculator {

    private val HINDI_MONTHS = listOf(
        "चैत्र", "वैशाख", "ज्येष्ठ", "आषाढ़", "श्रावण", "भाद्रपद",
        "आश्विन", "कार्तिक", "मार्गशीर्ष", "पौष", "माघ", "फाल्गुन"
    )

    private val TITHI_NAMES = listOf(
        "प्रतिपदा", "द्वितीया", "तृतीया", "चतुर्थी", "पंचमी",
        "षष्ठी", "सप्तमी", "अष्टमी", "नवमी", "दशमी",
        "एकादशी", "द्वादशी", "त्रयोदशी", "चतुर्दशी", "पूर्णिमा",
        "प्रतिपदा", "द्वितीया", "तृतीया", "चतुर्थी", "पंचमी",
        "षष्ठी", "सप्तमी", "अष्टमी", "नवमी", "दशमी",
        "एकादशी", "द्वादशी", "त्रयोदशी", "चतुर्दशी", "अमावस्या"
    )

    private val NAKSHATRAS = listOf(
        "अश्विनी", "भरणी", "कृत्तिका", "रोहिणी", "मृगशिरा", "आर्द्रा",
        "पुनर्वसु", "पुष्य", "आश्लेषा", "मघा", "पूर्वाफाल्गुनी", "उत्तराफाल्गुनी",
        "हस्त", "चित्रा", "स्वाती", "विशाखा", "अनुराधा", "ज्येष्ठा",
        "मूल", "पूर्वाषाढ़ा", "उत्तराषाढ़ा", "श्रवण", "धनिष्ठा", "शतभिषा",
        "पूर्वाभाद्रपद", "उत्तराभाद्रपद", "रेवती"
    )

    private val NAKSHATRA_LORDS = listOf(
        "केतु", "शुक्र", "सूर्य", "चंद्र", "मंगल", "राहु",
        "गुरु", "शनि", "बुध", "केतु", "शुक्र", "सूर्य",
        "चंद्र", "मंगल", "राहु", "गुरु", "शनि", "बुध",
        "केतु", "शुक्र", "सूर्य", "चंद्र", "मंगल", "राहु",
        "गुरु", "शनि", "बुध"
    )

    private val YOGAS = listOf(
        Pair("विष्कुम्भ", "अशुभ"), Pair("प्रीति", "शुभ"), Pair("आयुष्मान", "शुभ"),
        Pair("सौभाग्य", "शुभ"), Pair("शोभन", "शुभ"), Pair("अतिगण्ड", "अशुभ"),
        Pair("सुकर्मा", "शुभ"), Pair("धृति", "शुभ"), Pair("शूल", "अशुभ"),
        Pair("गण्ड", "अशुभ"), Pair("वृद्धि", "शुभ"), Pair("ध्रुव", "शुभ"),
        Pair("व्याघात", "अशुभ"), Pair("हर्षण", "शुभ"), Pair("वज्र", "अशुभ"),
        Pair("सिद्धि", "शुभ"), Pair("व्यतीपात", "अशुभ"), Pair("वरीयान", "शुभ"),
        Pair("परिघ", "मध्यम"), Pair("शिव", "शुभ"), Pair("सिद्ध", "शुभ"),
        Pair("साध्य", "शुभ"), Pair("शुभ", "शुभ"), Pair("शुक्ल", "शुभ"),
        Pair("ब्रह्म", "शुभ"), Pair("इन्द्र", "शुभ"), Pair("वैधृति", "अशुभ")
    )

    private val MOVABLE_KARANAS = listOf("बव", "बालव", "कौलव", "तैतिल", "गर", "वणिज", "विष्टि (भद्रा)")
    private val FIXED_KARANAS = listOf("शकुनि", "चतुष्पाद", "नाग", "किंस्तुघ्न")

    private val RASHIS = listOf(
        "मेष", "वृषभ", "मिथुन", "कर्क", "सिंह", "कन्या",
        "तुला", "वृश्चिक", "धनु", "मकर", "कुंभ", "मीन"
    )

    private val WEEKDAYS_HINDI = listOf(
        "सोमवार", "मंगलवार", "बुधवार", "गुरुवार", "शुक्रवार", "शनिवार", "रविवार"
    )

    fun calculatePanchang(date: LocalDate, location: CityLocation): DailyPanchang {
        val year = date.year
        val month = date.monthValue
        val day = date.dayOfMonth
        val dayOfWeekIndex = date.dayOfWeek.value // 1=Mon, ..., 7=Sun
        val weekdayHindi = WEEKDAYS_HINDI[dayOfWeekIndex - 1]

        // Julian Day at 6:00 AM IST
        val jd = toJulianDay(year, month, day, 6.0 - location.timezoneOffsetHours)
        val t = (jd - 2451545.0) / 36525.0

        // Lahiri Ayanamsha (Chitra Paksha)
        val ayanamsha = 23.85 + (year - 2000) * 0.01397 + (month / 12.0) * 0.01397

        // Sun Tropical & Sidereal Longitude
        val sunTropical = calculateSunTropicalLongitude(t)
        val sunSidereal = normalizeDegrees(sunTropical - ayanamsha)

        // Moon Tropical & Sidereal Longitude
        val moonTropical = calculateMoonTropicalLongitude(t)
        val moonSidereal = normalizeDegrees(moonTropical - ayanamsha)

        // Elongation: Moon - Sun
        val elongation = normalizeDegrees(moonTropical - sunTropical)

        // Tithi Calculation (0 to 29.999 -> 1 to 30)
        val rawTithi = elongation / 12.0
        val tithiNumber = (rawTithi.toInt() % 30) + 1
        val tithiFractionRemaining = 1.0 - (rawTithi - rawTithi.toInt())
        val tithiEndHours = (tithiFractionRemaining * 24.0 * 1.02 + 6.0) % 24.0
        val tithiEndTimeFormatted = formatDecimalHours(tithiEndHours)

        val isShukla = tithiNumber in 1..15
        val paksha = if (isShukla) "शुक्ल पक्ष" else "कृष्ण पक्ष"
        val tithiName = TITHI_NAMES[tithiNumber - 1]
        val isPurnima = tithiNumber == 15
        val isAmavasya = tithiNumber == 30
        val isEkadashi = tithiNumber == 11 || tithiNumber == 26

        val tithiInfo = TithiInfo(
            name = tithiName,
            number = tithiNumber,
            paksha = paksha,
            endsAt = tithiEndTimeFormatted,
            isPurnima = isPurnima,
            isAmavasya = isAmavasya,
            isEkadashi = isEkadashi
        )

        // Nakshatra Calculation (360 / 27 = 13.33333333 degrees each)
        val nakshatraIndex = ((moonSidereal / (360.0 / 27.0)).toInt() % 27)
        val nakshatraFractionRemaining = 1.0 - ((moonSidereal / (360.0 / 27.0)) - nakshatraIndex)
        val nakshatraEndHours = (nakshatraFractionRemaining * 24.0 * 0.98 + 7.5) % 24.0

        val nakshatraInfo = NakshatraInfo(
            name = NAKSHATRAS[nakshatraIndex],
            number = nakshatraIndex + 1,
            rulerPlanet = NAKSHATRA_LORDS[nakshatraIndex],
            endsAt = formatDecimalHours(nakshatraEndHours)
        )

        // Yoga Calculation ((Sun + Moon) / 13.333333 degrees each)
        val sumSidereal = normalizeDegrees(sunSidereal + moonSidereal)
        val yogaIndex = ((sumSidereal / (360.0 / 27.0)).toInt() % 27)
        val (yogaName, yogaType) = YOGAS[yogaIndex]
        val yogaFractionRemaining = 1.0 - ((sumSidereal / (360.0 / 27.0)) - yogaIndex)
        val yogaEndHours = (yogaFractionRemaining * 24.0 + 8.0) % 24.0

        val yogaInfo = YogaInfo(
            name = yogaName,
            number = yogaIndex + 1,
            type = yogaType,
            endsAt = formatDecimalHours(yogaEndHours)
        )

        // Karana Calculation (each Tithi has 2 Karanas = 6 degrees each)
        val karanaTotal = (elongation / 6.0).toInt() % 60
        val karanaName = when {
            karanaTotal == 0 -> FIXED_KARANAS[3] // किंस्तुघ्न
            karanaTotal in 1..56 -> MOVABLE_KARANAS[(karanaTotal - 1) % 7]
            karanaTotal == 57 -> FIXED_KARANAS[0] // शकुनि
            karanaTotal == 58 -> FIXED_KARANAS[1] // चतुष्पाद
            else -> FIXED_KARANAS[2] // नाग
        }
        val karanaType = if (karanaTotal == 0 || karanaTotal >= 57) "स्थिर" else "चर"
        val karanaInfo = KaranaInfo(name = karanaName, number = karanaTotal + 1, type = karanaType)

        // Rashi (Signs)
        val sunRashi = RASHIS[(sunSidereal / 30.0).toInt() % 12]
        val moonRashi = RASHIS[(moonSidereal / 30.0).toInt() % 12]

        // Sunrise & Sunset calculation for location
        val (sunriseHours, sunsetHours) = calculateSunTimes(date, location)
        val sunriseStr = formatDecimalHours(sunriseHours)
        val sunsetStr = formatDecimalHours(sunsetHours)

        // Moonrise and Moonset
        val moonriseHours = (sunriseHours + (tithiNumber * 0.8) + 24.0) % 24.0
        val moonsetHours = (sunsetHours + (tithiNumber * 0.8) + 24.0) % 24.0
        val moonriseStr = formatDecimalHours(moonriseHours)
        val moonsetStr = formatDecimalHours(moonsetHours)

        // Daytime duration & Muhurats
        val dayDurationHours = (sunsetHours - sunriseHours).let { if (it < 0) it + 24.0 else it }
        val praharaDuration = dayDurationHours / 8.0
        val muhuratDuration = dayDurationHours / 15.0

        // Weekday 0=Sunday, 1=Monday, 2=Tuesday, 3=Wednesday, 4=Thursday, 5=Friday, 6=Saturday
        val dowSunZero = (date.dayOfWeek.value % 7)

        // Rahu Kaal part index (1-based): Sun:8, Mon:2, Tue:7, Wed:5, Thu:6, Fri:4, Sat:3
        val rahuParts = listOf(8, 2, 7, 5, 6, 4, 3)
        val rahuPart = rahuParts[dowSunZero] - 1
        val rahuStart = sunriseHours + (rahuPart * praharaDuration)
        val rahuEnd = rahuStart + praharaDuration

        // Yamaganda part index: Sun:5, Mon:4, Tue:3, Wed:2, Thu:1, Fri:7, Sat:6
        val yamaParts = listOf(5, 4, 3, 2, 1, 7, 6)
        val yamaPart = yamaParts[dowSunZero] - 1
        val yamaStart = sunriseHours + (yamaPart * praharaDuration)
        val yamaEnd = yamaStart + praharaDuration

        // Gulika Kaal part index: Sun:7, Mon:6, Tue:5, Wed:4, Thu:3, Fri:2, Sat:1
        val guliParts = listOf(7, 6, 5, 4, 3, 2, 1)
        val guliPart = guliParts[dowSunZero] - 1
        val guliStart = sunriseHours + (guliPart * praharaDuration)
        val guliEnd = guliStart + praharaDuration

        // Abhijit Muhurat: 8th muhurat of daytime (around noon)
        val abhijitStart = sunriseHours + (7 * muhuratDuration)
        val abhijitEnd = sunriseHours + (8 * muhuratDuration)

        // Brahma Muhurat: 2 muhurats (96 mins) before sunrise
        val brahmaStart = (sunriseHours - (96.0 / 60.0) + 24.0) % 24.0
        val brahmaEnd = (sunriseHours - (48.0 / 60.0) + 24.0) % 24.0

        // Vijaya Muhurat (11th muhurat)
        val vijayaStart = sunriseHours + (10 * muhuratDuration)
        val vijayaEnd = sunriseHours + (11 * muhuratDuration)

        // Godhuli Muhurat (around sunset)
        val godhuliStart = sunsetHours - (12.0 / 60.0)
        val godhuliEnd = sunsetHours + (12.0 / 60.0)

        // Amrit Kaal (auspicious daytime window)
        val amritStart = (sunriseHours + 3.5) % 24.0
        val amritEnd = (amritStart + 1.5) % 24.0

        // Durmuhurat
        val durStart = sunriseHours + (4 * muhuratDuration)
        val durEnd = sunriseHours + (5 * muhuratDuration)

        // Hindu Month & Samvat calculation
        // Vikram Samvat is Gregorian Year + 57 (or +58 after Chaitra Pratipada)
        val samvatYear = year + 57
        val sakaYear = year - 78

        // Purnimanta month determination
        val approxHinduMonthIdx = calculateHinduMonthIndex(date, sunSidereal, tithiNumber)
        val hinduMonthName = HINDI_MONTHS[approxHinduMonthIdx]

        // Ayana & Ritu
        val ayana = if (month in 1..6) "उत्तरायण" else "दक्षिणायन"
        val ritu = when (month) {
            1, 2 -> "शिशिर ऋतु"
            3, 4 -> "वसन्त ऋतु"
            5, 6 -> "ग्रीष्म ऋतु"
            7, 8 -> "वर्षा ऋतु"
            9, 10 -> "शरद ऋतु"
            else -> "हेमन्त ऋतु"
        }

        // Deity assignment according to weekday or festival
        val weekdayDeity = when (dayOfWeekIndex) {
            1 -> DeityInfo.SHIVA
            2 -> DeityInfo.HANUMAN
            3 -> DeityInfo.GANESHA
            4 -> DeityInfo.VISHNU
            5 -> DeityInfo.LAKSHMI
            6 -> DeityInfo.SHANI
            else -> DeityInfo.SURYA
        }

        val (vrats, festivals, festivalDeityOverride) = FestivalEngine.getEventsForDate(
            date = date,
            hinduMonth = hinduMonthName,
            paksha = paksha,
            tithiNumber = tithiNumber,
            tithiName = tithiName,
            weekdayHindi = weekdayHindi
        )

        val activeDeity = festivalDeityOverride ?: weekdayDeity

        val dateFormatted = "${day} ${getHindiMonthName(month)} ${year}"

        val daySignificance = buildSignificanceText(
            weekdayHindi = weekdayHindi,
            hinduMonth = hinduMonthName,
            paksha = paksha,
            tithiName = tithiName,
            vrats = vrats,
            festivals = festivals,
            deity = activeDeity
        )

        val specialObservance = when {
            festivals.isNotEmpty() -> festivals.first().nameHindi
            vrats.isNotEmpty() -> vrats.first().nameHindi
            isEkadashi -> "एकादशी महाव्रत"
            isPurnima -> "पूर्णिमा पुण्य स्नान व दान"
            isAmavasya -> "अमावस्या पितृ तर्पण व दान"
            tithiNumber == 13 || tithiNumber == 28 -> "प्रदोष व्रत"
            tithiNumber == 4 || tithiNumber == 19 -> "चतुर्थी गणेश वंदना"
            else -> "$weekdayHindi पावन पूजन"
        }

        return DailyPanchang(
            dateString = date.toString(),
            dayOfMonth = day,
            monthOfYear = month,
            year = year,
            gregorianFormatted = dateFormatted,
            weekdayHindi = weekdayHindi,
            hinduMonthHindi = hinduMonthName,
            samvatHindi = "विक्रम संवत $samvatYear, नल",
            sakaSamvatHindi = "शक संवत $sakaYear, प्रमाथी",
            ayanaHindi = ayana,
            rituHindi = ritu,
            tithi = tithiInfo,
            nakshatra = nakshatraInfo,
            yoga = yogaInfo,
            karana = karanaInfo,
            sunrise = sunriseStr,
            sunset = sunsetStr,
            moonrise = moonriseStr,
            moonset = moonsetStr,
            sunSignHindi = sunRashi,
            moonSignHindi = moonRashi,
            brahmaMuhurat = MuhuratItem("ब्रह्म मुहूर्त", "${formatDecimalHours(brahmaStart)} - ${formatDecimalHours(brahmaEnd)}", true, "ईश्वर चिंतन, ध्यान और अध्ययन हेतु सर्वश्रेष्ठ"),
            abhijitMuhurat = MuhuratItem("अभिजीत मुहूर्त", "${formatDecimalHours(abhijitStart)} - ${formatDecimalHours(abhijitEnd)}", true, "सर्वकार्य सिद्धिदायक दिन का सबसे शुभ मुहूर्त"),
            amritKaal = MuhuratItem("अमृत काल", "${formatDecimalHours(amritStart)} - ${formatDecimalHours(amritEnd)}", true, "मांगलिक एवं नवीन कार्य शुभारंभ हेतु"),
            vijayaMuhurat = MuhuratItem("विजय मुहूर्त", "${formatDecimalHours(vijayaStart)} - ${formatDecimalHours(vijayaEnd)}", true, "कार्यों में विजय और सफलता प्राप्ति के लिए"),
            godhuliMuhurat = MuhuratItem("गोधूलि मुहूर्त", "${formatDecimalHours(godhuliStart)} - ${formatDecimalHours(godhuliEnd)}", true, "संध्या वंदन, दीप प्रज्वलन व पूजन हेतु"),
            rahuKaal = MuhuratItem("राहु काल", "${formatDecimalHours(rahuStart)} - ${formatDecimalHours(rahuEnd)}", false, "अशुभ समय: शुभ व नए कार्यों से बचें"),
            yamaganda = MuhuratItem("यमगण्ड", "${formatDecimalHours(yamaStart)} - ${formatDecimalHours(yamaEnd)}", false, "अशुभ काल: यात्रा व महत्वपूर्ण निर्णय न लें"),
            gulikaKaal = MuhuratItem("गुलिक काल", "${formatDecimalHours(guliStart)} - ${formatDecimalHours(guliEnd)}", false, "शनि का काल: मांगलिक कार्य वर्जित"),
            durMuhurat = MuhuratItem("दुर्मुहूर्त", "${formatDecimalHours(durStart)} - ${formatDecimalHours(durEnd)}", false, "वर्जित काल"),
            vrats = vrats,
            festivals = festivals,
            daySignificance = daySignificance,
            deity = activeDeity,
            specialObservance = specialObservance,
            cityName = location.displayLabel
        )
    }

    private fun calculateHinduMonthIndex(date: LocalDate, sunSidereal: Double, tithiNumber: Int): Int {
        // Lunar month based on Solar Nirayana Rashi and Lunar Tithi
        // Sun in Meena (~March) -> Chaitra
        val sunRashiIndex = (sunSidereal / 30.0).toInt() % 12
        // In Purnimanta system, the month starts with Krishna Paksha (tithi 16-30) after previous Purnima
        var monthIndex = (sunRashiIndex + 1) % 12
        // Adjust for North Indian Purnimanta boundary
        if (tithiNumber in 16..30) {
            monthIndex = (monthIndex + 1) % 12
        }
        return monthIndex
    }

    private fun calculateSunTropicalLongitude(t: Double): Double {
        val l0 = 280.46646 + 36000.76983 * t + 0.0003032 * t * t
        val m = 357.52911 + 35999.05029 * t - 0.0001537 * t * t
        val mr = Math.toRadians(normalizeDegrees(m))
        val c = (1.914602 - 0.004817 * t) * sin(mr) + (0.019993 - 0.000101 * t) * sin(2 * mr) + 0.000289 * sin(3 * mr)
        return normalizeDegrees(l0 + c)
    }

    private fun calculateMoonTropicalLongitude(t: Double): Double {
        val lm = 218.3165 + 481267.8813 * t
        val mm = Math.toRadians(normalizeDegrees(134.9634 + 477198.8675 * t))
        val ms = Math.toRadians(normalizeDegrees(357.52911 + 35999.05029 * t))
        val d = Math.toRadians(normalizeDegrees(297.8502 + 445267.1114 * t))

        val dl = 6.289 * sin(mm) + 1.274 * sin(2 * d - mm) + 0.658 * sin(2 * d) +
                0.214 * sin(2 * mm) - 0.186 * sin(ms)
        return normalizeDegrees(lm + dl)
    }

    private fun calculateSunTimes(date: LocalDate, location: CityLocation): Pair<Double, Double> {
        val dayOfYear = date.dayOfYear
        val latRad = Math.toRadians(location.latitude)

        // Fractional year in radians
        val gamma = 2 * Math.PI / 365.0 * (dayOfYear - 1)

        // Equation of Time in minutes
        val eqtime = 229.18 * (0.000075 + 0.001868 * cos(gamma) - 0.032077 * sin(gamma) -
                0.014615 * cos(2 * gamma) - 0.040849 * sin(2 * gamma))

        // Solar Declination in radians
        val decl = 0.006918 - 0.399912 * cos(gamma) + 0.070257 * sin(gamma) -
                0.006758 * cos(2 * gamma) + 0.000907 * sin(2 * gamma)

        // Hour Angle for sunrise/sunset (zenith = 90.833 deg)
        val cosH0 = (cos(Math.toRadians(90.833)) / (cos(latRad) * cos(decl))) - (tan(latRad) * tan(decl))
        val clampedCosH0 = cosH0.coerceIn(-1.0, 1.0)
        val haDegrees = Math.toDegrees(acos(clampedCosH0))

        // Local Solar Noon in UTC hours
        val timeOffset = 4.0 * location.longitude - 60.0 * location.timezoneOffsetHours
        val solarNoonMinutes = 720.0 - (4.0 * location.longitude) - eqtime + (location.timezoneOffsetHours * 60.0)
        val solarNoonHours = solarNoonMinutes / 60.0

        val sunriseHours = solarNoonHours - (haDegrees * 4.0 / 60.0)
        val sunsetHours = solarNoonHours + (haDegrees * 4.0 / 60.0)

        return Pair(sunriseHours, sunsetHours)
    }

    private fun toJulianDay(year: Int, month: Int, day: Int, hourUtc: Double): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = y / 100
        val b = 2 - a + (a / 4)
        val dayFraction = day + (hourUtc / 24.0)
        return (floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + dayFraction + b - 1524.5)
    }

    private fun normalizeDegrees(deg: Double): Double {
        var d = deg % 360.0
        if (d < 0) d += 360.0
        return d
    }

    private fun formatDecimalHours(hoursDecimal: Double): String {
        var totalMinutes = (hoursDecimal * 60.0).roundToInt()
        totalMinutes = (totalMinutes % 1440 + 1440) % 1440
        val h24 = totalMinutes / 60
        val m = totalMinutes % 60
        val amPm = if (h24 < 12) "AM" else "PM"
        val h12 = when (h24) {
            0 -> 12
            in 1..12 -> h24
            else -> h24 - 12
        }
        return String.format(Locale.getDefault(), "%02d:%02d %s", h12, m, amPm)
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

    private fun buildSignificanceText(
        weekdayHindi: String,
        hinduMonth: String,
        paksha: String,
        tithiName: String,
        vrats: List<VratItem>,
        festivals: List<FestivalItem>,
        deity: DeityInfo
    ): String {
        return buildString {
            if (festivals.isNotEmpty()) {
                append("आज पावन पर्व ${festivals.first().nameHindi} है। ")
                append(festivals.first().shortDesc)
                append(" ")
            }
            if (vrats.isNotEmpty()) {
                append("आज ${vrats.first().nameHindi} का पावन व्रत है। ")
            }
            append("आज $hinduMonth मास $paksha की $tithiName तिथि तथा $weekdayHindi है। ")
            append("यह दिन ${deity.nameHindi} की आराधना, सत्य, धर्म और आध्यात्मिक शांति के लिए विशेष फलदायी माना गया है।")
        }
    }
}
