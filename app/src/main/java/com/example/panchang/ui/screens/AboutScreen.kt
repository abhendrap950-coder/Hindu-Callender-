package com.example.panchang.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.MaroonPrimary
import com.example.ui.theme.SaffronSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    var showPrivacyPolicy by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ℹ️ ऐप के बारे में",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("about_back_btn")
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App Icon & Title Header
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF8E1408))
                    .border(2.dp, GoldAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ॐ",
                    color = GoldAccent,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "🕉️ Hindu Panchang (हिंदू पंचांग)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"आपकी दैनिक हिंदू पंचांग एवं कैलेंडर सेवा\"",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SaffronSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "App Version: 1.0.0",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // About Explanation Card
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
                    Text(
                        text = "📖 परिचय (About)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "यह ऐप सनातन धर्म की वैदिक परंपरा, खगोलीय गणना और भारतीय ज्योतिष शास्त्र के अनुसार प्रामाणिक हिंदू कैलेंडर, दैनिक पंचांग, तिथि, व्रत, त्योहार और शुभ-अशुभ मुहूर्त की संपूर्ण जानकारी एक ही स्थान पर सुलभ कराता है।",
                        fontSize = 13.sp,
                        lineHeight = 21.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Panchang Location Note
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "📍 पंचांग एवं समय संबंधी सूचना (Location Note)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "सूर्योदय, सूर्यास्त, चंद्रोदय और विभिन्न मुहूर्तों का सटीक समय भौगोलिक अक्षांश एवं देशांतर (स्थान) पर निर्भर करता है। अपने नगर का यथार्थ पंचांग देखने हेतु सेटिंग्स से सही स्थान का चयन करें।",
                        fontSize = 12.5.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Action Buttons: Rating, Share, Privacy Policy
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    // Rating
                    ListItem(
                        headlineContent = { Text("⭐ हमें Rating दें (Rate Us)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp) },
                        leadingContent = { Icon(Icons.Default.Star, contentDescription = null, tint = GoldAccent) },
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                Toast.makeText(context, "आपके पावन समर्थन और रेटिंग के लिए धन्यवाद! 🙏", Toast.LENGTH_SHORT).show()
                            }
                            .testTag("btn_rate_app")
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                    // Share
                    ListItem(
                        headlineContent = { Text("📤 ऐप Share करें (Share App)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp) },
                        leadingContent = { Icon(Icons.Default.Share, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Hindu Panchang App")
                                    putExtra(Intent.EXTRA_TEXT, "🕉️ दैनिक हिंदू पंचांग, शुभ मुहूर्त, व्रत एवं त्योहारों की प्रामाणिक जानकारी के लिए 'Hindu Panchang' ऐप देखें।\n॥ धर्मो रक्षति रक्षितः ॥")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "ऐप शेयर करें"))
                            }
                            .testTag("btn_share_app")
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                    // Privacy Policy
                    ListItem(
                        headlineContent = { Text("📜 Privacy Policy (गोपनीयता नीति)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp) },
                        leadingContent = { Icon(Icons.Default.Policy, contentDescription = null, tint = SaffronSecondary) },
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showPrivacyPolicy = true }
                            .testTag("btn_privacy_policy")
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sacred Footer
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Made with 🙏 for Dharma & Culture",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Made by Ravi Kumar Kannaujiya",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Privacy Policy Dialog
    if (showPrivacyPolicy) {
        AlertDialog(
            onDismissRequest = { showPrivacyPolicy = false },
            confirmButton = {
                TextButton(onClick = { showPrivacyPolicy = false }) {
                    Text("स्वीकार है (Close)")
                }
            },
            title = {
                Text(
                    text = "📜 गोपनीयता नीति (Privacy Policy)",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "1. स्थान डेटा (Location Data):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "ऐप द्वारा स्थान अनुमति केवल सूर्योदय, सूर्यास्त और स्थानीय पंचांग गणना हेतु ली जाती है। स्थान डेटा किसी बाह्य सर्वर पर न तो भेजा जाता है और न ही ट्रैक किया जाता है।",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "2. डेटा गोपनीयता (Data Privacy):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "ऐप आपकी कोई व्यक्तिगत जानकारी, फोन नंबर या ईमेल एकत्रित नहीं करता। सभी सेटिंग्स आपके फोन में ही सुरक्षित रहती हैं।",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "3. ऑफ़लाइन कार्यक्षमता (Offline Use):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "सभी पंचांग और खगोलीय गणनाएं आपके फोन पर ही स्वतः निष्पादित होती हैं, जिसके लिए सतत इंटरनेट की आवश्यकता नहीं होती।",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}
