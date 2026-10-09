package com.example.panchang.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Festival
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.panchang.model.*
import com.example.panchang.ui.components.GreetingCardExporter
import com.example.panchang.ui.components.getDeityDrawableResource
import com.example.panchang.util.CoilImageLoaderProvider
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.MaroonPrimary
import com.example.ui.theme.SaffronSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardGeneratorScreen(
    currentPanchang: DailyPanchang,
    initialFestival: FestivalItem? = null,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val imageLoader = remember(context) { CoilImageLoaderProvider.getImageLoader(context) }

    // State for Category
    var selectedCategory by remember {
        mutableStateOf(
            if (initialFestival != null) CardCategory.FESTIVAL_GREETING else CardCategory.DEVOTIONAL_QUOTE
        )
    }

    // Selected Theme & Aspect Ratio
    var selectedTheme by remember { mutableStateOf(CardThemePalette.ROYAL_MAROON) }
    var selectedAspectRatio by remember { mutableStateOf(CardAspectRatio.PORTRAIT_4_5) }

    // Selected Deity
    var selectedDeityType by remember {
        mutableStateOf(
            if (initialFestival != null) {
                when {
                    initialFestival.nameHindi.contains("शिव") -> DeityType.SHIVA
                    initialFestival.nameHindi.contains("गणेश") -> DeityType.GANESHA
                    initialFestival.nameHindi.contains("हनुमान") -> DeityType.HANUMAN
                    initialFestival.nameHindi.contains("दीपावली") || initialFestival.nameHindi.contains("लक्ष्मी") -> DeityType.LAKSHMI
                    else -> currentPanchang.deity.type
                }
            } else {
                currentPanchang.deity.type
            }
        )
    }

    // Active Quote or Festival Template
    val defaultQuote = GreetingCardCatalog.DEVOTIONAL_QUOTES.first()
    val defaultFest = if (initialFestival != null) {
        GreetingCardCatalog.FESTIVAL_GREETINGS.find { it.festivalNameHindi.contains(initialFestival.nameHindi) || initialFestival.nameHindi.contains(it.festivalNameHindi) }
            ?: FestivalGreetingTemplate(
                id = "custom_fest",
                festivalNameHindi = initialFestival.nameHindi,
                greetingTitle = "${initialFestival.nameHindi} की हार्दिक शुभकामनाएं",
                wishesHindi = initialFestival.shortDesc.ifBlank { "भगवान के आशीर्वाद से आपके समस्त परिवार को सुख, शांति, समृद्धि और आनंद की प्राप्ति हो।" },
                sacredMantra = "शुभ ${initialFestival.nameHindi}",
                defaultDeityType = selectedDeityType
            )
    } else {
        GreetingCardCatalog.FESTIVAL_GREETINGS.first()
    }

    var selectedQuotePreset by remember { mutableStateOf(defaultQuote) }
    var selectedFestivalPreset by remember { mutableStateOf(defaultFest) }

    // Editable text values
    var cardTitle by remember {
        mutableStateOf(
            if (selectedCategory == CardCategory.DEVOTIONAL_QUOTE) defaultQuote.titleHindi else defaultFest.greetingTitle
        )
    }
    var cardBodyText by remember {
        mutableStateOf(
            if (selectedCategory == CardCategory.DEVOTIONAL_QUOTE) defaultQuote.quoteHindi else defaultFest.wishesHindi
        )
    }
    var cardSanskritShloka by remember {
        mutableStateOf(
            if (selectedCategory == CardCategory.DEVOTIONAL_QUOTE) defaultQuote.sanskritShloka else ""
        )
    }
    var cardMantra by remember {
        mutableStateOf(
            if (selectedCategory == CardCategory.DEVOTIONAL_QUOTE) defaultQuote.sacredMantra else defaultFest.sacredMantra
        )
    }
    var senderSignature by remember { mutableStateOf("सपरिवार") }

    // Export progress state
    var isExporting by remember { mutableStateOf(false) }
    var showCustomEditDialog by remember { mutableStateOf(false) }

    val dateAndTithiString = remember(currentPanchang) {
        "${currentPanchang.gregorianFormatted} (${currentPanchang.weekdayHindi}), ${currentPanchang.hinduMonthHindi} ${currentPanchang.tithi.name}"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🎴 सुविचार एवं शुभकामना कार्ड",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("card_generator_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "वापस जाएं"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showCustomEditDialog = true },
                        modifier = Modifier.testTag("card_generator_edit_text_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "टेक्स्ट बदलें",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Save to Gallery Button
                    OutlinedButton(
                        onClick = {
                            if (!isExporting) {
                                isExporting = true
                                coroutineScope.launch {
                                    val bmp = GreetingCardExporter.generateCardBitmap(
                                        context = context,
                                        categoryHindi = if (selectedCategory == CardCategory.DEVOTIONAL_QUOTE) "दैनिक सुविचार" else "शुभ पर्व मंगलकामना",
                                        titleText = cardTitle,
                                        sanskritShloka = cardSanskritShloka,
                                        bodyText = cardBodyText,
                                        sacredMantra = cardMantra,
                                        deityType = selectedDeityType,
                                        themePalette = selectedTheme,
                                        aspectRatio = selectedAspectRatio,
                                        senderSignature = senderSignature,
                                        dateAndTithi = dateAndTithiString
                                    )
                                    GreetingCardExporter.saveCardToGallery(context, bmp, cardTitle)
                                    isExporting = false
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("save_card_button"),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isExporting
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "गैलरी में सहेजें",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    // Share Card Button
                    Button(
                        onClick = {
                            if (!isExporting) {
                                isExporting = true
                                coroutineScope.launch {
                                    val bmp = GreetingCardExporter.generateCardBitmap(
                                        context = context,
                                        categoryHindi = if (selectedCategory == CardCategory.DEVOTIONAL_QUOTE) "दैनिक सुविचार" else "शुभ पर्व मंगलकामना",
                                        titleText = cardTitle,
                                        sanskritShloka = cardSanskritShloka,
                                        bodyText = cardBodyText,
                                        sacredMantra = cardMantra,
                                        deityType = selectedDeityType,
                                        themePalette = selectedTheme,
                                        aspectRatio = selectedAspectRatio,
                                        senderSignature = senderSignature,
                                        dateAndTithi = dateAndTithiString
                                    )
                                    val caption = buildString {
                                        append("🚩 $cardTitle 🚩\n")
                                        if (cardSanskritShloka.isNotBlank()) append("$cardSanskritShloka\n\n")
                                        append("\"$cardBodyText\"\n\n")
                                        if (cardMantra.isNotBlank()) append("॥ $cardMantra ॥\n")
                                        if (senderSignature.isNotBlank()) append("सप्रेम प्रेषक: $senderSignature\n")
                                        append("हिंदू पंचांग")
                                    }
                                    GreetingCardExporter.shareCard(context, bmp, caption)
                                    isExporting = false
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("share_card_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaroonPrimary
                        ),
                        enabled = !isExporting
                    ) {
                        if (isExporting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "कार्ड शेयर करें",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
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
            // Category Switcher: सुविचार vs त्योहार
            SingleChoiceSegmentedControl(
                selectedCategory = selectedCategory,
                onCategoryChange = { newCat ->
                    selectedCategory = newCat
                    if (newCat == CardCategory.DEVOTIONAL_QUOTE) {
                        cardTitle = selectedQuotePreset.titleHindi
                        cardBodyText = selectedQuotePreset.quoteHindi
                        cardSanskritShloka = selectedQuotePreset.sanskritShloka
                        cardMantra = selectedQuotePreset.sacredMantra
                        selectedDeityType = selectedQuotePreset.deityType
                    } else {
                        cardTitle = selectedFestivalPreset.greetingTitle
                        cardBodyText = selectedFestivalPreset.wishesHindi
                        cardSanskritShloka = ""
                        cardMantra = selectedFestivalPreset.sacredMantra
                        selectedDeityType = selectedFestivalPreset.defaultDeityType
                    }
                }
            )

            // LIVE CARD PREVIEW CONTAINER
            LiveCardPreview(
                categoryHindi = if (selectedCategory == CardCategory.DEVOTIONAL_QUOTE) "दैनिक सुविचार" else "शुभ पर्व मंगलकामना",
                titleText = cardTitle,
                sanskritShloka = cardSanskritShloka,
                bodyText = cardBodyText,
                sacredMantra = cardMantra,
                deityType = selectedDeityType,
                theme = selectedTheme,
                aspectRatio = selectedAspectRatio,
                senderSignature = senderSignature,
                dateAndTithi = dateAndTithiString,
                imageLoader = imageLoader,
                onCardClick = { showCustomEditDialog = true }
            )

            // PRESETS CAROUSEL (Quotes or Festivals)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (selectedCategory == CardCategory.DEVOTIONAL_QUOTE) "📜 सुविचार एवं श्लोक चुनें" else "🪔 पर्व एवं त्योहार चुनें",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (selectedCategory == CardCategory.DEVOTIONAL_QUOTE) {
                            GreetingCardCatalog.DEVOTIONAL_QUOTES.forEach { quote ->
                                val isSelected = quote.id == selectedQuotePreset.id
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedQuotePreset = quote
                                        cardTitle = quote.titleHindi
                                        cardBodyText = quote.quoteHindi
                                        cardSanskritShloka = quote.sanskritShloka
                                        cardMantra = quote.sacredMantra
                                        selectedDeityType = quote.deityType
                                    },
                                    label = { Text(quote.titleHindi, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaroonPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        } else {
                            GreetingCardCatalog.FESTIVAL_GREETINGS.forEach { fest ->
                                val isSelected = fest.id == selectedFestivalPreset.id
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedFestivalPreset = fest
                                        cardTitle = fest.greetingTitle
                                        cardBodyText = fest.wishesHindi
                                        cardSanskritShloka = ""
                                        cardMantra = fest.sacredMantra
                                        selectedDeityType = fest.defaultDeityType
                                    },
                                    label = { Text(fest.festivalNameHindi, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaroonPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // DEITY SELECTION
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "🕉️ आराध्य देव / देवी चुनें",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    val deities = listOf(
                        DeityType.SHIVA to "भगवान शिव",
                        DeityType.HANUMAN to "श्री हनुमान",
                        DeityType.GANESHA to "श्री गणेश",
                        DeityType.LAKSHMI to "माँ लक्ष्मी"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        deities.forEach { (type, name) ->
                            val isSelected = selectedDeityType == type
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedDeityType = type },
                                label = { Text(name, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SaffronSecondary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // THEME COLOR & ASPECT RATIO
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Theme Palettes
                    Text(
                        text = "🎨 कार्ड रंग एवं शैली (Theme)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CardThemePalette.values().forEach { theme ->
                            val isSelected = theme == selectedTheme
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { selectedTheme = theme }
                                    .padding(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                listOf(theme.topGradientColor, theme.bottomGradientColor)
                                            )
                                        )
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) GoldAccent else Color.Gray.copy(alpha = 0.4f),
                                            shape = CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = theme.titleHindi,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                    // Aspect Ratios
                    Text(
                        text = "📐 कार्ड आकार (Aspect Ratio)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CardAspectRatio.values().forEach { ratio ->
                            val isSelected = ratio == selectedAspectRatio
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedAspectRatio = ratio },
                                label = { Text(ratio.labelHindi, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // SENDER NAME INPUT
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "✍️ प्रेषक का नाम (Sender Name)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(
                        value = senderSignature,
                        onValueChange = { senderSignature = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("sender_name_input"),
                        placeholder = { Text("उदा. आपका नाम या सपरिवार") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // DIALOG FOR CUSTOM TEXT EDITING
    if (showCustomEditDialog) {
        AlertDialog(
            onDismissRequest = { showCustomEditDialog = false },
            title = {
                Text(
                    text = "कार्ड का संदेश संपादित करें",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = cardTitle,
                        onValueChange = { cardTitle = it },
                        label = { Text("शीर्षक (Title)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = cardSanskritShloka,
                        onValueChange = { cardSanskritShloka = it },
                        label = { Text("संस्कृत श्लोक (वैकल्पिक)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = cardBodyText,
                        onValueChange = { cardBodyText = it },
                        label = { Text("विचार / शुभकामना संदेश") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = cardMantra,
                        onValueChange = { cardMantra = it },
                        label = { Text("पावन मंत्र / नारा") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showCustomEditDialog = false }) {
                    Text("सहेजें")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomEditDialog = false }) {
                    Text("रद्द करें")
                }
            }
        )
    }
}

@Composable
fun SingleChoiceSegmentedControl(
    selectedCategory: CardCategory,
    onCategoryChange: (CardCategory) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val isQuote = selectedCategory == CardCategory.DEVOTIONAL_QUOTE
            Surface(
                color = if (isQuote) MaroonPrimary else Color.Transparent,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onCategoryChange(CardCategory.DEVOTIONAL_QUOTE) }
                    .padding(vertical = 8.dp),
                contentColor = if (isQuote) Color.White else MaterialTheme.colorScheme.onSurface
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "दैनिक सुविचार",
                        fontWeight = if (isQuote) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.5.sp
                    )
                }
            }

            val isFest = selectedCategory == CardCategory.FESTIVAL_GREETING
            Surface(
                color = if (isFest) MaroonPrimary else Color.Transparent,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onCategoryChange(CardCategory.FESTIVAL_GREETING) }
                    .padding(vertical = 8.dp),
                contentColor = if (isFest) Color.White else MaterialTheme.colorScheme.onSurface
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Festival,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "पर्व शुभकामना",
                        fontWeight = if (isFest) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LiveCardPreview(
    categoryHindi: String,
    titleText: String,
    sanskritShloka: String,
    bodyText: String,
    sacredMantra: String,
    deityType: DeityType,
    theme: CardThemePalette,
    aspectRatio: CardAspectRatio,
    senderSignature: String,
    dateAndTithi: String,
    imageLoader: coil.ImageLoader,
    onCardClick: () -> Unit
) {
    val deityRes = getDeityDrawableResource(deityType)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(if (aspectRatio == CardAspectRatio.STORY_9_16) 0.85f else 0.95f)
                .aspectRatio(aspectRatio.ratioValue)
                .shadow(12.dp, RoundedCornerShape(20.dp))
                .clickable { onCardClick() }
                .testTag("live_greeting_card_preview"),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(2.5.dp, GoldAccent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(theme.topGradientColor, theme.bottomGradientColor)
                        )
                    )
                    .padding(14.dp)
            ) {
                // Inner Decorative Border
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            1.dp,
                            GoldAccent.copy(alpha = 0.5f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top Header / Crest
                        Text(
                            text = "॥ ॐ ॥  $categoryHindi  ॥ ॐ ॥",
                            color = GoldAccent,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        // Deity Photo & Diyas
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            // Left Diya
                            AsyncImage(
                                model = R.drawable.diya_lamp,
                                contentDescription = null,
                                imageLoader = imageLoader,
                                modifier = Modifier.size(34.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            // Framed Deity Portrait
                            Box(
                                modifier = Modifier
                                    .size(if (aspectRatio == CardAspectRatio.SQUARE_1_1) 78.dp else 95.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(2.dp, GoldAccent, RoundedCornerShape(16.dp))
                            ) {
                                AsyncImage(
                                    model = deityRes,
                                    contentDescription = null,
                                    imageLoader = imageLoader,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))
                            // Right Diya
                            AsyncImage(
                                model = R.drawable.diya_lamp,
                                contentDescription = null,
                                imageLoader = imageLoader,
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        // Title Text
                        Text(
                            text = titleText,
                            fontSize = if (aspectRatio == CardAspectRatio.SQUARE_1_1) 14.sp else 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFF8E1),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )

                        // Sanskrit Shloka (if present)
                        if (sanskritShloka.isNotBlank()) {
                            Surface(
                                color = Color.Black.copy(alpha = 0.28f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(0.8.dp, GoldAccent.copy(alpha = 0.5f)),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text(
                                    text = sanskritShloka,
                                    fontSize = 10.5.sp,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFFFE082),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    lineHeight = 14.sp
                                )
                            }
                        }

                        // Body Quote / Message
                        Text(
                            text = bodyText,
                            fontSize = if (aspectRatio == CardAspectRatio.SQUARE_1_1) 11.5.sp else 12.5.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 17.sp,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )

                        // Sacred Mantra Pill
                        if (sacredMantra.isNotBlank()) {
                            Surface(
                                color = Color(0xFF4A0804).copy(alpha = 0.65f),
                                shape = RoundedCornerShape(20.dp),
                                border = androidx.compose.foundation.BorderStroke(1.2.dp, GoldAccent)
                            ) {
                                Text(
                                    text = "॥ $sacredMantra ॥",
                                    color = Color(0xFFFFF9C4),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Footer: Date & Signature
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = dateAndTithi,
                                color = Color(0xFFE0E0E0),
                                fontSize = 9.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = if (senderSignature.isNotBlank()) "सप्रेम प्रेषक: $senderSignature  |  हिंदू पंचांग" else "हिंदू पंचांग सेवा",
                                color = GoldAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
