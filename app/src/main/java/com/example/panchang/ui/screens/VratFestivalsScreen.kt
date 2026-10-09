package com.example.panchang.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.panchang.model.FestivalItem
import com.example.panchang.model.VratItem
import com.example.ui.theme.MaroonPrimary
import com.example.ui.theme.SaffronSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VratFestivalsScreen(
    vrats: List<VratItem>,
    festivals: List<FestivalItem>,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedVratForDetail by remember { mutableStateOf<VratItem?>(null) }
    var selectedFestForDetail by remember { mutableStateOf<FestivalItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🪔 व्रत एवं त्योहार",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("vrat_fest_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "वापस जाएं"
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
        ) {
            // Two tabs at the top: व्रत | त्योहार
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.testTag("vrat_fest_tab_row")
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Text(
                            text = "व्रत (Vrat)",
                            fontSize = 15.sp,
                            fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("tab_vrat")
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Text(
                            text = "त्योहार (Festivals)",
                            fontSize = 15.sp,
                            fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("tab_festivals")
                )
            }

            // Tab Content
            if (selectedTabIndex == 0) {
                // Vrat Tab List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(vrats) { item ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedVratForDetail = item }
                                .testTag("vrat_card_${item.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.nameHindi,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Surface(
                                        color = MaroonPrimary.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = item.dateFormatted,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaroonPrimary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "देवता: ${item.associatedDeity}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SaffronSecondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.shortDesc,
                                    fontSize = 12.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            } else {
                // Festival Tab List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(festivals) { item ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedFestForDetail = item }
                                .testTag("festival_card_${item.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.nameHindi,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Surface(
                                        color = SaffronSecondary.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = item.dateFormatted,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SaffronSecondary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "संबंधित: ${item.associatedDeity}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.shortDesc,
                                    fontSize = 12.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet / Detail Dialog for Vrat
    selectedVratForDetail?.let { item ->
        AlertDialog(
            onDismissRequest = { selectedVratForDetail = null },
            confirmButton = {
                TextButton(onClick = { selectedVratForDetail = null }) {
                    Text("ठीक है (Close)")
                }
            },
            title = {
                Text(
                    text = item.nameHindi,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "📅 तिथि: ${item.dateFormatted}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = "🕉️ आराध्य: ${item.associatedDeity}", color = SaffronSecondary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    HorizontalDivider()
                    Text(text = "महत्व:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    Text(text = item.significance, fontSize = 12.5.sp)
                    Text(text = "पूजन विधि:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    Text(text = item.pujaVidhi, fontSize = 12.5.sp)
                    Text(text = "शुभ मुहूर्त:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    Text(text = item.timings, fontSize = 12.5.sp)
                    Text(text = "पारण समय:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaroonPrimary)
                    Text(text = item.paranaTime, fontSize = 12.5.sp)
                    HorizontalDivider()
                    Text(text = "📖 पावन व्रत कथा:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    Text(text = item.vratKatha, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    // Modal Bottom Sheet / Detail Dialog for Festival
    selectedFestForDetail?.let { item ->
        AlertDialog(
            onDismissRequest = { selectedFestForDetail = null },
            confirmButton = {
                TextButton(onClick = { selectedFestForDetail = null }) {
                    Text("ठीक है (Close)")
                }
            },
            title = {
                Text(
                    text = item.nameHindi,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "📅 पर्व तिथि: ${item.dateFormatted}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = "🪔 आराध्य: ${item.associatedDeity}", color = SaffronSecondary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    HorizontalDivider()
                    Text(text = "धार्मिक महत्व:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    Text(text = item.significance, fontSize = 12.5.sp)
                    Text(text = "पूजन विधि व नियम:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    Text(text = item.pujaVidhi, fontSize = 12.5.sp)
                    Text(text = "शुभ पूजन मुहूर्त:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    Text(text = item.timings, fontSize = 12.5.sp)
                    HorizontalDivider()
                    Text(text = "📖 पौराणिक पृष्ठभूमि एवं कथा:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    Text(text = item.backgroundStory, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}
