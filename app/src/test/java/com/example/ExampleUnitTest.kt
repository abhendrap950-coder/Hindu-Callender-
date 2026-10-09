package com.example

import com.example.panchang.engine.AstronomicalCalculator
import com.example.panchang.engine.FestivalEngine
import com.example.panchang.model.CityLocation
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class ExampleUnitTest {
    @Test
    fun testPanchangCalculation() {
        val date = LocalDate.of(2026, 10, 8)
        val panchang = AstronomicalCalculator.calculatePanchang(date, CityLocation.DEFAULT)

        assertNotNull(panchang)
        assertEquals("गुरुवार", panchang.weekdayHindi)
        assertTrue(panchang.tithi.number in 1..30)
        assertNotNull(panchang.tithi.name)
        assertTrue(panchang.nakshatra.number in 1..27)
        assertTrue(panchang.yoga.number in 1..27)
        assertNotNull(panchang.sunrise)
        assertNotNull(panchang.sunset)
        assertNotNull(panchang.abhijitMuhurat.timeRange)
        assertNotNull(panchang.rahuKaal.timeRange)
        assertNotNull(panchang.deity.nameHindi)
    }

    @Test
    fun testFestivalEngine() {
        val date = LocalDate.of(2026, 10, 20)
        val result = FestivalEngine.getEventsForDate(
            date = date,
            hinduMonth = "आश्विन",
            paksha = "शुक्ल पक्ष",
            tithiNumber = 10,
            tithiName = "दशमी",
            weekdayHindi = "मंगलवार"
        )
        assertTrue(result.festivals.any { it.nameHindi.contains("दशहरा") || it.nameHindi.contains("विजयादशमी") })
    }
}
