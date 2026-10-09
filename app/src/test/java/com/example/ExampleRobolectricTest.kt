package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Hindu Panchang", appName)
  }

  @Test
  fun `test greeting card catalog and models`() {
    val quotes = com.example.panchang.model.GreetingCardCatalog.DEVOTIONAL_QUOTES
    org.junit.Assert.assertTrue(quotes.isNotEmpty())
    org.junit.Assert.assertTrue(quotes.any { it.deityType == com.example.panchang.model.DeityType.SHIVA })
    org.junit.Assert.assertTrue(quotes.any { it.deityType == com.example.panchang.model.DeityType.HANUMAN })
    org.junit.Assert.assertTrue(quotes.any { it.deityType == com.example.panchang.model.DeityType.GANESHA })
    org.junit.Assert.assertTrue(quotes.any { it.deityType == com.example.panchang.model.DeityType.LAKSHMI })

    val festivals = com.example.panchang.model.GreetingCardCatalog.FESTIVAL_GREETINGS
    org.junit.Assert.assertTrue(festivals.isNotEmpty())
    org.junit.Assert.assertTrue(festivals.any { it.festivalNameHindi.contains("महाशिवरात्रि") })
    org.junit.Assert.assertTrue(festivals.any { it.festivalNameHindi.contains("दीपावली") })

    val themes = com.example.panchang.model.CardThemePalette.values()
    assertEquals(4, themes.size)

    val ratios = com.example.panchang.model.CardAspectRatio.values()
    assertEquals(3, ratios.size)
  }
}
