package com.example.panchang.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.panchang.util.CoilImageLoaderProvider
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.panchang.model.DailyPanchang
import com.example.panchang.model.DeityType
import com.example.ui.theme.*

@Composable
fun RoyalPanchangCard(
    panchang: DailyPanchang,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color(0xFFFFB300),
                ambientColor = Color(0xFF3E0502)
            )
            .border(
                width = 2.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldLight,
                        GoldDark,
                        AntiqueGold,
                        GoldLight,
                        Color(0xFF8B5A00)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .clip(RoundedCornerShape(24.dp))
            .testTag("royal_panchang_hero_card"),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF380502), // Deep Royal Maroon
                            Color(0xFF520704),
                            Color(0xFF081C1E), // Deep Royal Teal/Navy
                            Color(0xFF041214)
                        )
                    )
                )
                .padding(bottom = 12.dp)
        ) {
            // ==========================================
            // 1. TOP HERO AREA: Real Devotional Deity Artwork & Temple Header
            // ==========================================
            HeroDevotionalHeader(panchang = panchang)

            // ==========================================
            // 2. MAIN "आज का पंचांग" TITLE & DATE CREST
            // ==========================================
            MainTitleAndDateCrest(panchang = panchang)

            Spacer(modifier = Modifier.height(8.dp))

            // ==========================================
            // 3. SUN & MOON INFORMATION (4 CELESTIAL CARDS)
            // ==========================================
            CelestialCardsRow(panchang = panchang)

            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 4. PANCHANG DETAILS (PARCHMENT MANUSCRIPT PANEL)
            // ==========================================
            PanchangParchmentPanel(panchang = panchang)

            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 5. SPECIAL OCCASIONS ("आज के विशेष अवसर")
            // ==========================================
            SpecialOccasionsPanel(panchang = panchang)

            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 6. SHUBH MUHURAT ("शुभ एवं अशुभ मुहूर्त")
            // ==========================================
            ShubhMuhuratPanel(panchang = panchang)

            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 7. DAILY SPIRITUAL MESSAGE / QUOTE SCROLL
            // ==========================================
            DailySpiritualQuoteScroll(panchang = panchang)

            Spacer(modifier = Modifier.height(8.dp))

            // ==========================================
            // 8. LOCATION FOOTER
            // ==========================================
            LocationFooterBar(
                locationName = panchang.cityName,
                onClick = onLocationClick
            )
        }
    }
}

// ----------------------------------------------------
// Section 1: Top Hero Devotional Header with Real Photo & Arched Frame
// ----------------------------------------------------
@Composable
private fun HeroDevotionalHeader(panchang: DailyPanchang) {
    val context = LocalContext.current
    val deityRes = getDeityDrawableResource(panchang.deity.type)
    val imageLoader = remember(context) { CoilImageLoaderProvider.getImageLoader(context) }
    val imageRequest = remember(deityRes, context) {
        CoilImageLoaderProvider.buildDeityImageRequest(context, deityRes)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        // High-Quality Deity Portrait (Lord Shiva / Day's Deity) with object-fit: cover, loaded and cached via Coil
        AsyncImage(
            model = imageRequest,
            imageLoader = imageLoader,
            contentDescription = panchang.deity.nameHindi,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Devotional Vignette Gradient: Transparent in middle, fading to deep royal maroon at bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                            Color(0x88380502),
                            Color(0xFF380502)
                        )
                    )
                )
        )

        // Ornate Golden Arch Frame along Top
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        listOf(GoldLight, GoldDark, Color.Transparent)
                    ),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        )

        // Top Centered Sacred Mantra Crest: "॥ ॐ नमः शिवाय ॥"
        Surface(
            color = Color(0xDD5A0803),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, GoldAccent),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp)
        ) {
            Text(
                text = "॥ ${panchang.deity.sacredMantra} ॥",
                color = Color(0xFFFFF9E6),
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
            )
        }

        // Sacred Brass Diya Lamps on Left & Right base
        Image(
            painter = painterResource(id = R.drawable.diya_lamp),
            contentDescription = "दीपक",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(46.dp)
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, bottom = 4.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.diya_lamp),
            contentDescription = "दीपक",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(46.dp)
                .align(Alignment.BottomEnd)
                .padding(end = 8.dp, bottom = 4.dp)
        )

        // Devotional Couplet Inscribed at Bottom of Hero
        Text(
            text = when (panchang.deity.type) {
                DeityType.SHIVA -> "हर दिन एक नई शुरुआत है, जब शिव का नाम साथ है ॥"
                DeityType.HANUMAN -> "संकट कटे मिटे सब पीरा, जो सुमिरै हनुमत बलबीरा ॥"
                DeityType.GANESHA -> "विघ्न हरण मंगल करन, श्री गणपति महाराज ॥"
                DeityType.LAKSHMI -> "माँ महालक्ष्मी की कृपा से सदा सुख-समृद्धि बनी रहे ॥"
                else -> "सद्कर्म और ईश्वर स्मरण से जीवन मंगलमय होता है ॥"
            },
            color = Color(0xFFFFF8E1),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 6.dp, start = 48.dp, end = 48.dp)
        )
    }
}

// ----------------------------------------------------
// Section 2: Main Title Banner & Date Display
// ----------------------------------------------------
@Composable
private fun MainTitleAndDateCrest(panchang: DailyPanchang) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Royal Maroon Ornamental Crest
        Surface(
            color = MaroonCrest,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(
                    listOf(GoldDark, GoldLight, GoldAccent, GoldLight, GoldDark)
                )
            ),
            shadowElevation = 6.dp,
            modifier = Modifier.fillMaxWidth(0.92f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🔱",
                        fontSize = 14.sp,
                        color = GoldAccent
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "आज का पंचांग",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFF9E6),
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🔱",
                        fontSize = 14.sp,
                        color = GoldAccent
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Date Display & Samvat Subtitle
        Text(
            text = "${panchang.weekdayHindi}, ${panchang.gregorianFormatted}",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "${panchang.hinduMonthHindi}  |  ${panchang.samvatHindi}  |  ${panchang.sakaSamvatHindi}",
            fontSize = 12.sp,
            color = GoldLight,
            fontWeight = FontWeight.Medium
        )
    }
}

// ----------------------------------------------------
// Section 3: Sun & Moon Information (4 Celestial Cards)
// ----------------------------------------------------
@Composable
private fun CelestialCardsRow(panchang: DailyPanchang) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        CelestialTimeCard(
            type = CelestialType.SUNRISE,
            label = "सूर्योदय",
            time = panchang.sunrise,
            modifier = Modifier.weight(1f)
        )
        CelestialTimeCard(
            type = CelestialType.SUNSET,
            label = "सूर्यास्त",
            time = panchang.sunset,
            modifier = Modifier.weight(1f)
        )
        CelestialTimeCard(
            type = CelestialType.MOONRISE,
            label = "चंद्रोदय",
            time = panchang.moonrise,
            modifier = Modifier.weight(1f)
        )
        CelestialTimeCard(
            type = CelestialType.MOONSET,
            label = "चंद्रास्त",
            time = panchang.moonset,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CelestialTimeCard(
    type: CelestialType,
    label: String,
    time: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF0A2B2F),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.5f)),
        shadowElevation = 4.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 7.dp, horizontal = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CelestialBadge(type = type, modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD54F)
            )
            Text(
                text = time,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

// ----------------------------------------------------
// Section 4: Panchang Details (Traditional Golden Parchment Panel)
// ----------------------------------------------------
@Composable
private fun PanchangParchmentPanel(panchang: DailyPanchang) {
    Surface(
        color = ParchmentLight,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, ParchmentScrollEdge),
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            ParchmentLight,
                            ParchmentBase,
                            ParchmentLight
                        )
                    )
                )
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ParchmentItemRow(
                        icon = "📅",
                        label = "तिथि :",
                        value = "${panchang.tithi.name} (${panchang.tithi.endsAt} तक)"
                    )
                    ParchmentItemRow(
                        icon = "🌙",
                        label = "पक्ष :",
                        value = panchang.tithi.paksha
                    )
                    ParchmentItemRow(
                        icon = "⭐",
                        label = "नक्षत्र :",
                        value = "${panchang.nakshatra.name} (${panchang.nakshatra.endsAt} तक)"
                    )
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .width(1.dp)
                        .height(78.dp)
                        .background(ParchmentScrollEdge)
                )

                // Right Column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ParchmentItemRow(
                        icon = "🌸",
                        label = "योग :",
                        value = "${panchang.yoga.name} (${panchang.yoga.endsAt} तक)"
                    )
                    ParchmentItemRow(
                        icon = "⚙️",
                        label = "करण :",
                        value = panchang.karana.name
                    )
                    ParchmentItemRow(
                        icon = "🏛️",
                        label = "वार :",
                        value = panchang.weekdayHindi
                    )
                }
            }
        }
    }
}

@Composable
private fun ParchmentItemRow(icon: String, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = icon, fontSize = 12.sp)
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextParchmentSecondary
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = value,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = TextParchmentPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ----------------------------------------------------
// Section 5: Special Occasions ("आज के विशेष अवसर")
// ----------------------------------------------------
@Composable
private fun SpecialOccasionsPanel(panchang: DailyPanchang) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        // Section Header Ribbon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = MaroonCrest,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent),
                shadowElevation = 3.dp
            ) {
                Text(
                    text = "🪔 आज के विशेष अवसर",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFF9E6),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 3.dp)
                )
            }
        }

        Surface(
            color = ParchmentLight,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ParchmentBorder),
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Miniature Deity Frame on left with real photo
                val context = LocalContext.current
                val deityRes = getDeityDrawableResource(panchang.deity.type)
                val imageLoader = remember(context) { CoilImageLoaderProvider.getImageLoader(context) }
                val imageRequest = remember(deityRes, context) {
                    CoilImageLoaderProvider.buildDeityImageRequest(context, deityRes)
                }
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF5E0B07))
                        .border(1.5.dp, GoldAccent, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageRequest,
                        imageLoader = imageLoader,
                        contentDescription = panchang.deity.nameHindi,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Middle Bullet Points
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if (panchang.festivals.isNotEmpty()) {
                        panchang.festivals.forEach { fest ->
                            BulletPointItem(text = fest.nameHindi, isHighlight = true)
                        }
                    } else if (panchang.vrats.isNotEmpty()) {
                        panchang.vrats.forEach { vrat ->
                            BulletPointItem(text = vrat.nameHindi, isHighlight = true)
                        }
                    } else {
                        BulletPointItem(text = "आज कोई प्रमुख त्योहार नहीं", isHighlight = false)
                    }

                    BulletPointItem(text = "${panchang.weekdayHindi} पावन दिन", isHighlight = false)
                    BulletPointItem(text = "${panchang.deity.nameHindi} की विशेष पूजा", isHighlight = false)
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Right: "आज का सुझाव" Box
                Surface(
                    color = Color(0xFFFFF1D6),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2C475)),
                    modifier = Modifier.width(115.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            color = MaroonCrest,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "आज का सुझाव",
                                color = GoldLight,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Image(
                            painter = painterResource(id = R.drawable.diya_lamp),
                            contentDescription = "दीपक",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = when (panchang.deity.type) {
                                DeityType.SHIVA -> "शिवलिंग पर जल व बिल्वपत्र अर्पित करें।"
                                DeityType.HANUMAN -> "हनुमान चालीसा का पाठ करें।"
                                DeityType.GANESHA -> "भगवान गणेश को दूर्वा अर्पित करें।"
                                DeityType.LAKSHMI -> "श्री सूक्त का पाठ करें।"
                                else -> "ईश्वर का ध्यान व सत्कर्म करें।"
                            },
                            color = TextParchmentPrimary,
                            fontSize = 8.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 11.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BulletPointItem(text: String, isHighlight: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .background(if (isHighlight) SaffronSecondary else MaroonPrimary, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 10.5.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlight) MaroonPrimary else TextParchmentPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ----------------------------------------------------
// Section 6: Shubh Muhurat ("शुभ एवं अशुभ मुहूर्त") Panel
// ----------------------------------------------------
@Composable
private fun ShubhMuhuratPanel(panchang: DailyPanchang) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        // Section Header Ribbon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = MaroonCrest,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent),
                shadowElevation = 3.dp
            ) {
                Text(
                    text = "⏰ शुभ एवं अशुभ मुहूर्त",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFF9E6),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 3.dp)
                )
            }
        }

        // 5 Pastel Mini-Cards
        Surface(
            color = ParchmentLight,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ParchmentBorder),
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                MuhuratMiniCard(
                    title = "ब्रह्म मुहूर्त",
                    timeRange = panchang.brahmaMuhurat.timeRange,
                    bgColor = MuhuratBrahmaBg,
                    textColor = MuhuratBrahmaText,
                    modifier = Modifier.weight(1f)
                )
                MuhuratMiniCard(
                    title = "अभिजीत",
                    timeRange = panchang.abhijitMuhurat.timeRange,
                    bgColor = MuhuratAbhijitBg,
                    textColor = MuhuratAbhijitText,
                    modifier = Modifier.weight(1f)
                )
                MuhuratMiniCard(
                    title = "अमृत काल",
                    timeRange = panchang.amritKaal.timeRange,
                    bgColor = MuhuratAmritBg,
                    textColor = MuhuratAmritText,
                    modifier = Modifier.weight(1f)
                )
                MuhuratMiniCard(
                    title = "राहुकाल",
                    timeRange = panchang.rahuKaal.timeRange,
                    bgColor = MuhuratRahuBg,
                    textColor = MuhuratRahuText,
                    modifier = Modifier.weight(1f)
                )
                MuhuratMiniCard(
                    title = "यमगण्ड",
                    timeRange = panchang.yamaganda.timeRange,
                    bgColor = MuhuratYamaBg,
                    textColor = MuhuratYamaText,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MuhuratMiniCard(
    title: String,
    timeRange: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val times = timeRange.split(" - ")
    val startTime = times.getOrNull(0) ?: timeRange
    val endTime = times.getOrNull(1) ?: ""

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(0.8.dp, textColor.copy(alpha = 0.35f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = startTime,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center
            )
            if (endTime.isNotEmpty()) {
                Text(
                    text = "से $endTime",
                    fontSize = 7.5.sp,
                    color = textColor.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

// ----------------------------------------------------
// Section 7: Daily Spiritual Message / Quote Scroll
// ----------------------------------------------------
@Composable
private fun DailySpiritualQuoteScroll(panchang: DailyPanchang) {
    Surface(
        color = ParchmentLight,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, ParchmentScrollEdge),
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            ParchmentScrollEdge.copy(alpha = 0.3f),
                            ParchmentLight,
                            ParchmentLight,
                            ParchmentScrollEdge.copy(alpha = 0.3f)
                        )
                    )
                )
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.diya_lamp),
                contentDescription = "दीपक",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "॥ ${panchang.deity.sacredMantra} ॥",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldDark
                )
                Text(
                    text = "\"सकारात्मक विचार, ईश्वर का स्मरण और सत्कर्म – यही जीवन को सफल बनाते हैं।\"",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextParchmentPrimary,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 14.sp
                )
            }

            Image(
                painter = painterResource(id = R.drawable.diya_lamp),
                contentDescription = "दीपक",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ----------------------------------------------------
// Section 8: Location Footer Bar
// ----------------------------------------------------
@Composable
private fun LocationFooterBar(
    locationName: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(top = 4.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "स्थान",
            tint = Color(0xFFFF5252),
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "स्थान: $locationName",
            color = Color(0xFFFFF3A1),
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(2.dp))
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "स्थान बदलें",
            tint = Color(0xFFFFF3A1),
            modifier = Modifier.size(15.dp)
        )
    }
}
