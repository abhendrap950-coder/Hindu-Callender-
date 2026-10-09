package com.example.panchang.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.panchang.model.DailyPanchang
import com.example.panchang.ui.components.PosterExporter
import com.example.panchang.ui.components.RoyalPanchangCard
import kotlinx.coroutines.launch

@Composable
fun DailyCardScreen(
    panchang: DailyPanchang,
    onNavigateHome: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isExporting by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF080202))
            .systemBarsPadding()
            .testTag("daily_card_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 9:16 Aspect Ratio Royal Poster Card Container
            Box(
                modifier = Modifier
                    .weight(1f)
                    .widthIn(max = 440.dp)
                    .aspectRatio(9f / 16f, matchHeightConstraintsFirst = true)
                    .clip(RoundedCornerShape(22.dp))
                    .testTag("daily_poster_card")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    RoyalPanchangCard(
                        panchang = panchang,
                        onLocationClick = onNavigateHome,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ONLY THREE ICONS IMMEDIATELY BELOW THE CARD:
            // 1. Save / Download
            // 2. Home
            // 3. Share
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Save / Download
                IconButton(
                    onClick = {
                        if (!isExporting) {
                            isExporting = true
                            coroutineScope.launch {
                                val bitmap = PosterExporter.generatePosterBitmap(context, panchang)
                                PosterExporter.savePosterToGallery(context, bitmap)
                                isExporting = false
                            }
                        }
                    },
                    modifier = Modifier
                        .size(54.dp)
                        .background(Color(0xFF240604), CircleShape)
                        .border(1.5.dp, Color(0xFFFFB300), CircleShape)
                        .testTag("daily_card_save_button")
                ) {
                    if (isExporting) {
                        CircularProgressIndicator(
                            color = Color(0xFFFFD54F),
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "गैलरी में सहेजें (Save to Gallery)",
                            tint = Color(0xFFFFD54F),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                // 2. Home (Center Button)
                IconButton(
                    onClick = onNavigateHome,
                    modifier = Modifier
                        .size(62.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFFFFB300), Color(0xFFC67C00), Color(0xFF8E1408))
                            ),
                            shape = CircleShape
                        )
                        .border(1.5.dp, Color(0xFFFFF0B8), CircleShape)
                        .shadow(8.dp, CircleShape)
                        .testTag("daily_card_home_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "मुख्य पृष्ठ (Go to Home)",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // 3. Share
                IconButton(
                    onClick = {
                        if (!isExporting) {
                            isExporting = true
                            coroutineScope.launch {
                                val bitmap = PosterExporter.generatePosterBitmap(context, panchang)
                                PosterExporter.sharePoster(context, bitmap)
                                isExporting = false
                            }
                        }
                    },
                    modifier = Modifier
                        .size(54.dp)
                        .background(Color(0xFF240604), CircleShape)
                        .border(1.5.dp, Color(0xFFFFB300), CircleShape)
                        .testTag("daily_card_share_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "दैनिक पंचांग शेयर करें (Share)",
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}
