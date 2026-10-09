package com.example.panchang.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.panchang.model.CityLocation
import com.google.android.gms.location.LocationServices
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentLocation: CityLocation,
    onCitySelected: (CityLocation) -> Unit,
    theme: String,
    onThemeChanged: (String) -> Unit,
    audioEnabled: Boolean,
    onAudioEnabledChanged: (Boolean) -> Unit,
    notifFestivals: Boolean,
    onNotifFestivalsChanged: (Boolean) -> Unit,
    notifVrats: Boolean,
    onNotifVratsChanged: (Boolean) -> Unit,
    openDailyCard: Boolean,
    onOpenDailyCardChanged: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    var showCityDialog by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("हिंदी") }
    var audioVoice by remember { mutableStateOf("पुरुष स्वर (Male)") }

    // GPS location launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fineGranted || coarseGranted) {
            fetchDeviceLocation(context) { lat, lon, name ->
                val gpsLoc = CityLocation(
                    id = "gps_${lat}_${lon}",
                    nameHindi = name,
                    nameEnglish = name,
                    stateHindi = "वर्तमान स्थान",
                    latitude = lat,
                    longitude = lon
                )
                onCitySelected(gpsLoc)
                Toast.makeText(context, "स्थान सफलतापूर्वक अद्यतित किया गया: $name", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "स्थान अनुमति अस्वीकृत। आप सूची से शहर चुन सकते हैं।", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "⚙️ सेटिंग्स",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("settings_back_btn")
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Location Settings (स्थान)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "स्थान (Location)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "📍 वर्तमान स्थान: ${currentLocation.displayLabel}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "सटीक सूर्योदय, सूर्यास्त और मुहूर्त गणना आपके स्थान पर निर्भर करती है।",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showCityDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_select_city")
                        ) {
                            Text(text = "शहर बदलें", fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                if (hasFine || hasCoarse) {
                                    fetchDeviceLocation(context) { lat, lon, name ->
                                        val gpsLoc = CityLocation(
                                            id = "gps_${lat}_${lon}",
                                            nameHindi = name,
                                            nameEnglish = name,
                                            stateHindi = "वर्तमान स्थान",
                                            latitude = lat,
                                            longitude = lon
                                        )
                                        onCitySelected(gpsLoc)
                                        Toast.makeText(context, "स्थान अद्यतित: $name", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_gps_location")
                        ) {
                            Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "वर्तमान GPS", fontSize = 13.sp)
                        }
                    }
                }
            }

            // 2. Notifications Settings (सूचनाएँ)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "सूचनाएँ (Notifications)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "प्रमुख त्योहारों की सूचना", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Text(text = "दिवाली, होली, नवरात्रि आदि के आगमन पर", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = notifFestivals,
                            onCheckedChange = onNotifFestivalsChanged,
                            modifier = Modifier.testTag("switch_fest_notif")
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "एकादशी एवं प्रदोष व्रत सूचना", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Text(text = "उपवास व पारण के समय की पूर्व सूचना", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = notifVrats,
                            onCheckedChange = onNotifVratsChanged,
                            modifier = Modifier.testTag("switch_vrat_notif")
                        )
                    }
                }
            }

            // 3. Audio / Mantra Settings (श्लोक/मंत्र की आवाज़)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "श्लोक / मंत्र की आवाज़",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "दैनिक मंत्र व श्लोक ध्वनि चालू रखें", fontSize = 14.sp)
                        Switch(
                            checked = audioEnabled,
                            onCheckedChange = onAudioEnabledChanged,
                            modifier = Modifier.testTag("switch_audio")
                        )
                    }

                    if (audioEnabled) {
                        Text(text = "आवाज़ का चयन (Voice Option):", fontSize = 12.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            FilterChip(
                                selected = audioVoice == "पुरुष स्वर (Male)",
                                onClick = { audioVoice = "पुरुष स्वर (Male)" },
                                label = { Text("पुरुष स्वर (Male)") }
                            )
                            FilterChip(
                                selected = audioVoice == "स्त्री स्वर (Female)",
                                onClick = { audioVoice = "स्त्री स्वर (Female)" },
                                label = { Text("स्त्री स्वर (Female)") }
                            )
                        }
                    }
                }
            }

            // 4. Language Settings (भाषा)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "भाषा (Language)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterChip(
                            selected = selectedLanguage == "हिंदी",
                            onClick = { selectedLanguage = "हिंदी" },
                            label = { Text("हिंदी (Hindi)") }
                        )
                        FilterChip(
                            selected = selectedLanguage == "English",
                            onClick = { selectedLanguage = "English" },
                            label = { Text("English") }
                        )
                    }
                }
            }

            // 5. Theme Settings (थीम)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "थीम (Theme)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = theme == "system",
                            onClick = { onThemeChanged("system") },
                            label = { Text("सिस्टम (Default)") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = theme == "light",
                            onClick = { onThemeChanged("light") },
                            label = { Text("लाइट (Light)") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = theme == "dark",
                            onClick = { onThemeChanged("dark") },
                            label = { Text("डार्क (Dark)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // 6. Daily Card Settings
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "दैनिक स्टेटस कार्ड (Daily Card)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "ऐप खोलते ही दैनिक कार्ड दिखाएं", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Text(text = "9:16 स्टेटस और शेयर हेतु सुंदर पंचांग पोस्टर", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = openDailyCard,
                            onCheckedChange = onOpenDailyCardChanged,
                            modifier = Modifier.testTag("switch_open_daily_card")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Manual City Selection Dialog
    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) {
                    Text("रद्द करें (Cancel)")
                }
            },
            title = {
                Text(
                    text = "📍 तीर्थ एवं प्रमुख शहर चुनें",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CityLocation.MAJOR_CITIES.forEach { city ->
                        val isSelected = city.id == currentLocation.id
                        Surface(
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCitySelected(city)
                                    showCityDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = city.nameHindi,
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${city.nameEnglish}, ${city.stateHindi}",
                                        fontSize = 11.5.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@SuppressLint("MissingPermission")
private fun fetchDeviceLocation(
    context: Context,
    onLocationFound: (Double, Double, String) -> Unit
) {
    try {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
            if (loc != null) {
                var cityName = "वर्तमान स्थान"
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val addr = addresses[0]
                        cityName = addr.locality ?: addr.subAdminArea ?: addr.adminArea ?: "वर्तमान स्थान"
                    }
                } catch (e: Exception) {
                    // Geocoding fallback
                }
                onLocationFound(loc.latitude, loc.longitude, cityName)
            } else {
                Toast.makeText(context, "स्थान प्राप्त नहीं हो सका। कृपया GPS ऑन करें।", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context, "स्थान त्रुटि: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
