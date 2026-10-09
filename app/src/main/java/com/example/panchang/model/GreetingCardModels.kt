package com.example.panchang.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AuspiciousGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.MaroonPrimary
import com.example.ui.theme.SaffronSecondary

enum class CardCategory {
    DEVOTIONAL_QUOTE,   // दैनिक सुविचार एवं पावन मंत्र
    FESTIVAL_GREETING   // त्योहार एवं पर्व शुभकामना
}

enum class CardThemePalette(
    val titleHindi: String,
    val topGradientColor: Color,
    val bottomGradientColor: Color,
    val accentColor: Color,
    val cardSurfaceColor: Color,
    val topGradientArgb: Int,
    val bottomGradientArgb: Int,
    val accentArgb: Int
) {
    ROYAL_MAROON(
        titleHindi = "शाही महरून",
        topGradientColor = Color(0xFF4A0804),
        bottomGradientColor = Color(0xFF1E0302),
        accentColor = GoldAccent,
        cardSurfaceColor = Color(0xFF2C0503),
        topGradientArgb = android.graphics.Color.rgb(74, 8, 4),
        bottomGradientArgb = android.graphics.Color.rgb(30, 3, 2),
        accentArgb = android.graphics.Color.rgb(226, 196, 117)
    ),
    SACRED_SAFFRON(
        titleHindi = "दिव्य भगवा",
        topGradientColor = Color(0xFF8B2500),
        bottomGradientColor = Color(0xFF380E00),
        accentColor = Color(0xFFFFD54F),
        cardSurfaceColor = Color(0xFF531703),
        topGradientArgb = android.graphics.Color.rgb(139, 37, 0),
        bottomGradientArgb = android.graphics.Color.rgb(56, 14, 0),
        accentArgb = android.graphics.Color.rgb(255, 213, 79)
    ),
    PEACEFUL_EMERALD(
        titleHindi = "शांत पन्ना",
        topGradientColor = Color(0xFF0A3326),
        bottomGradientColor = Color(0xFF031610),
        accentColor = Color(0xFFE8D49E),
        cardSurfaceColor = Color(0xFF0D251C),
        topGradientArgb = android.graphics.Color.rgb(10, 51, 38),
        bottomGradientArgb = android.graphics.Color.rgb(3, 22, 16),
        accentArgb = android.graphics.Color.rgb(232, 212, 158)
    ),
    CELESTIAL_NAVY(
        titleHindi = "नक्षत्र नील",
        topGradientColor = Color(0xFF0B1938),
        bottomGradientColor = Color(0xFF030712),
        accentColor = Color(0xFFFFE082),
        cardSurfaceColor = Color(0xFF0F1E3D),
        topGradientArgb = android.graphics.Color.rgb(11, 25, 56),
        bottomGradientArgb = android.graphics.Color.rgb(3, 7, 18),
        accentArgb = android.graphics.Color.rgb(255, 224, 130)
    )
}

enum class CardAspectRatio(
    val labelHindi: String,
    val ratioValue: Float,
    val pixelWidth: Int,
    val pixelHeight: Int
) {
    PORTRAIT_4_5("४:५ पोस्ट", 4f / 5f, 1080, 1350),
    STORY_9_16("९:१६ स्टेटस", 9f / 16f, 1080, 1920),
    SQUARE_1_1("१:१ वर्ग", 1f, 1080, 1080)
}

data class DevotionalQuoteItem(
    val id: String,
    val titleHindi: String,
    val quoteHindi: String,
    val sanskritShloka: String,
    val sacredMantra: String,
    val deityType: DeityType,
    val deityName: String
)

data class FestivalGreetingTemplate(
    val id: String,
    val festivalNameHindi: String,
    val greetingTitle: String,
    val wishesHindi: String,
    val sacredMantra: String,
    val defaultDeityType: DeityType
)

object GreetingCardCatalog {

    val DEVOTIONAL_QUOTES = listOf(
        DevotionalQuoteItem(
            id = "shiva_peace",
            titleHindi = "शिव कृपा एवं आत्मिक शांति",
            quoteHindi = "सकारात्मक विचार, ईश्वर का स्मरण और निष्काम सत्कर्म – यही जीवन को सफल और परमानंदमय बनाते हैं।",
            sanskritShloka = "कर्पूरगौरं करुणावतारं संसारसारम् भुजगेन्द्रहारम्।\nसदावसन्तं हृदयारविन्दे भवं भवानीसहितं नमामि॥",
            sacredMantra = "ॐ नमः शिवाय",
            deityType = DeityType.SHIVA,
            deityName = "भगवान शिव"
        ),
        DevotionalQuoteItem(
            id = "hanuman_sankatmochan",
            titleHindi = "संकट मोचन एवं अभय",
            quoteHindi = "मन के हारे हार है, मन के जीते जीत। प्रभु श्री हनुमान के चरणों में समर्पित मन कभी भयभीत नहीं होता।",
            sanskritShloka = "मनोजवं मारुततुल्यवेगं जितेन्द्रियं बुद्धिमतां वरिष्ठम्।\nवातात्मजं वानरयूथमुख्यं श्रीरामदूतं शरणं प्रपद्ये॥",
            sacredMantra = "ॐ हं हनुमते नमः",
            deityType = DeityType.HANUMAN,
            deityName = "श्री हनुमान"
        ),
        DevotionalQuoteItem(
            id = "ganesha_vighnaharta",
            titleHindi = "विघ्नहर्ता एवं शुभ आरंभ",
            quoteHindi = "जिस कार्य का आरंभ प्रभु स्मरण और शुद्ध अंतःकरण से होता है, उसमें समस्त विघ्न स्वतः विलीन हो जाते हैं।",
            sanskritShloka = "वक्रतुण्ड महाकाय सूर्यकोटि समप्रभ।\nनिर्विघ्नं कुरु मे देव सर्वकार्येषु सर्वदा॥",
            sacredMantra = "ॐ गं गणपतये नमः",
            deityType = DeityType.GANESHA,
            deityName = "श्री गणेश"
        ),
        DevotionalQuoteItem(
            id = "lakshmi_prosperity",
            titleHindi = "सुख, शांति एवं समृद्धि",
            quoteHindi = "सच्चा ऐश्वर्य हृदय की शुद्धि, परिवार में प्रेम और निष्ठापूर्वक किए गए पुरुषार्थ में ही वास करता है।",
            sanskritShloka = "नमस्तेऽस्तु महामाये श्रीपीठे सुरपूजिते।\nशङ्खचक्रगदाहस्ते महालक्ष्मि नमोऽस्तु ते॥",
            sacredMantra = "ॐ श्रीं महालक्ष्म्यै नमः",
            deityType = DeityType.LAKSHMI,
            deityName = "माँ महालक्ष्मी"
        ),
        DevotionalQuoteItem(
            id = "krishna_karmayoga",
            titleHindi = "कर्मयोग एवं निष्काम भक्ति",
            quoteHindi = "कर्म करो पर फल की चिंता त्याग दो। जब कर्म ईश्वर को समर्पित होता है, तो हर क्षण साधना बन जाता है।",
            sanskritShloka = "कर्मण्येवाधिकारस्ते मा फलेषु कदाचन।\nमा कर्मफलहेतुर्भूर्मा ते सङ्गोऽस्त्वकर्मणि॥",
            sacredMantra = "हरे कृष्ण हरे कृष्ण कृष्ण कृष्ण हरे हरे",
            deityType = DeityType.SHIVA,
            deityName = "भगवान श्री कृष्ण"
        ),
        DevotionalQuoteItem(
            id = "ram_dharma",
            titleHindi = "मर्यादा एवं धर्म का मार्ग",
            quoteHindi = "सत्य और धर्म की राह पर चलने वाले को जीवन की कोई भी विपदा विचलित नहीं कर सकती।",
            sanskritShloka = "रामाय रामभद्राय रामचंद्राय वेधसे।\nरघुनाथाय नाथाय सीतायाः पतये नमः॥",
            sacredMantra = "श्री राम जय राम जय जय राम",
            deityType = DeityType.HANUMAN,
            deityName = "प्रभु श्री राम"
        ),
        DevotionalQuoteItem(
            id = "shiva_mahamrityunjaya",
            titleHindi = "महामृत्युंजय आरोग्य कृपा",
            quoteHindi = "प्रभु भोलेनाथ की असीम अनुकंपा से आपके जीवन में आरोग्य, दीर्घायु और आत्मिक शांति का सदैव वास रहे।",
            sanskritShloka = "त्र्यम्बकं यजामहे सुगन्धिं पुष्टिवर्धनम्।\nउर्वारुकमिव बन्धनान्मृत्योर्मुक्षीय मामृतात्॥",
            sacredMantra = "ॐ त्र्यम्बकं यजामहे",
            deityType = DeityType.SHIVA,
            deityName = "महाकाल शिव"
        ),
        DevotionalQuoteItem(
            id = "hanuman_balbuddhi",
            titleHindi = "बल, बुद्धि एवं विद्या प्रदाता",
            quoteHindi = "विद्यावान गुनी अति चातुर। राम काज करिबे को आतुर। प्रभु की कृपा से ज्ञान और पराक्रम की प्राप्ति हो।",
            sanskritShloka = "बलं बुद्धिं यशो धैर्यं निर्भयत्वमरोगताम्।\nअजाड्यं वाक्पटुत्वं च हनुमत्स्मरणाद्भवेत्॥",
            sacredMantra = "जय बजरंगबली",
            deityType = DeityType.HANUMAN,
            deityName = "पवनपुत्र हनुमान"
        )
    )

    val FESTIVAL_GREETINGS = listOf(
        FestivalGreetingTemplate(
            id = "mahashivaratri",
            festivalNameHindi = "महाशिवरात्रि",
            greetingTitle = "महाशिवरात्रि की हार्दिक शुभकामनाएं",
            wishesHindi = "देवाधिदेव महादेव और माता भवानी की असीम कृपा से आपके जीवन में सुख, शांति, समृद्धि और उत्तम स्वास्थ्य का वास हो।",
            sacredMantra = "हर हर महादेव • ॐ नमः शिवाय",
            defaultDeityType = DeityType.SHIVA
        ),
        FestivalGreetingTemplate(
            id = "ganesh_chaturthi",
            festivalNameHindi = "श्री गणेश चतुर्थी",
            greetingTitle = "गणेश चतुर्थी की अनंत मंगलकामनाएं",
            wishesHindi = "विघ्नहर्ता भगवान श्री गणेश आपके समस्त कष्टों को दूर कर जीवन में रिद्धि-सिद्धि, सफलता और आनंद का वरदान दें।",
            sacredMantra = "गणपति बप्पा मोरया • ॐ गं गणपतये नमः",
            defaultDeityType = DeityType.GANESHA
        ),
        FestivalGreetingTemplate(
            id = "diwali",
            festivalNameHindi = "दीपावली महापर्व",
            greetingTitle = "दीपावली महापर्व की हार्दिक बधाई",
            wishesHindi = "माँ महालक्ष्मी और प्रभु श्री राम की कृपा से आपके घर-आंगन में सदा सुख, ऐश्वर्य, उल्लास और मंगलकारी प्रकाश बना रहे।",
            sacredMantra = "शुभ दीपावली • ॐ महालक्ष्म्यै नमः",
            defaultDeityType = DeityType.LAKSHMI
        ),
        FestivalGreetingTemplate(
            id = "hanuman_jayanti",
            festivalNameHindi = "श्री हनुमान जयंती",
            greetingTitle = "हनुमान जयंती की पावन शुभकामनाएं",
            wishesHindi = "संकटमोचन पवनपुत्र श्री हनुमान जी आपके सभी संकटों का नाश करें तथा अपार बल, बुद्धि, विद्या और तेज प्रदान करें।",
            sacredMantra = "जय श्री राम • जय हनुमान",
            defaultDeityType = DeityType.HANUMAN
        ),
        FestivalGreetingTemplate(
            id = "ram_navami",
            festivalNameHindi = "श्री राम नवमी",
            greetingTitle = "श्री राम नवमी की हार्दिक शुभकामनाएं",
            wishesHindi = "मर्यादा पुरुषोत्तम भगवान श्री राम का आशीर्वाद आपके परिवार पर सदैव बना रहे। आपके जीवन में धर्म और सत्य का प्रकाश फैले।",
            sacredMantra = "सियावर रामचंद्र की जय",
            defaultDeityType = DeityType.HANUMAN
        ),
        FestivalGreetingTemplate(
            id = "krishna_janmashtami",
            festivalNameHindi = "श्री कृष्ण जन्माष्टमी",
            greetingTitle = "श्री कृष्ण जन्माष्टमी की मंगलकामनाएं",
            wishesHindi = "माखनचोर, नटखट नंदलाल भगवान श्री कृष्ण आपके जीवन को प्रेम, भक्ति, आनंद और सद्गुणों से परिपूर्ण करें।",
            sacredMantra = "जय श्री कृष्णा • राधे राधे",
            defaultDeityType = DeityType.SHIVA
        ),
        FestivalGreetingTemplate(
            id = "navratri",
            festivalNameHindi = "शारदीय/चैत्र नवरात्रि",
            greetingTitle = "शुभ नवरात्रि की पावन शुभकामनाएं",
            wishesHindi = "माँ जगदम्बा नवदुर्गा आपके घर में रिद्धि-सिद्धि, आरोग्य और सकारात्मक ऊर्जा का संचार करें। शक्ति स्वरूपा माँ का आशीर्वाद मिले।",
            sacredMantra = "जय माता दी • ॐ दुं दुर्गायै नमः",
            defaultDeityType = DeityType.LAKSHMI
        ),
        FestivalGreetingTemplate(
            id = "makar_sankranti",
            festivalNameHindi = "मकर संक्रांति",
            greetingTitle = "मकर संक्रांति की हार्दिक शुभकामनाएं",
            wishesHindi = "भगवान सूर्य नारायण का पावन तेज आपके जीवन में नई उमंग, ऊर्जा, आरोग्य और प्रगति का मार्ग प्रशस्त करे।",
            sacredMantra = "ॐ सूर्याय नमः • शुभ संक्रांति",
            defaultDeityType = DeityType.SHIVA
        ),
        FestivalGreetingTemplate(
            id = "holi",
            festivalNameHindi = "होली एवं वसंतोत्सव",
            greetingTitle = "होली महापर्व की रंगोत्सव शुभकामनाएं",
            wishesHindi = "प्रेम, सौहार्द और उल्लास के पावन रंगों से आपका जीवन सदा महकता रहे। समस्त परिवार में खुशहाली आए।",
            sacredMantra = "शुभ होली • प्रेम एवं सौहार्द",
            defaultDeityType = DeityType.GANESHA
        ),
        FestivalGreetingTemplate(
            id = "ekadashi",
            festivalNameHindi = "पावन एकादशी व्रत",
            greetingTitle = "पवित्र एकादशी की हार्दिक मंगलकामनाएं",
            wishesHindi = "जगतपालक भगवान श्री हरि विष्णु की कृपा से आपके समस्त पापों का क्षय हो और अंतःकरण में भक्ति और शांति का वास हो।",
            sacredMantra = "ॐ नमो भगवते वासुदेवाय",
            defaultDeityType = DeityType.LAKSHMI
        ),
        FestivalGreetingTemplate(
            id = "pradosh",
            festivalNameHindi = "प्रदोष व्रत",
            greetingTitle = "पावन प्रदोष व्रत की शुभकामनाएं",
            wishesHindi = "संध्या काल में भगवान भोलेनाथ और माता पार्वती की अराधना से आपके सभी मनोरथ सिद्ध हों और कष्ट दूर हों।",
            sacredMantra = "ॐ नमः शिवाय • हर हर महादेव",
            defaultDeityType = DeityType.SHIVA
        )
    )
}
