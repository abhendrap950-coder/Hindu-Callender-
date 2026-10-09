package com.example.panchang.model

enum class DeityType {
    SHIVA,
    HANUMAN,
    GANESHA,
    VISHNU,
    LAKSHMI,
    SHANI,
    SURYA,
    DURGA,
    KRISHNA,
    RAM
}

data class DeityInfo(
    val type: DeityType,
    val nameHindi: String,
    val titleHindi: String,
    val sacredShloka: String,
    val shlokaMeaning: String,
    val sacredMantra: String,
    val devotionalNote: String
) {
    companion object {
        val SHIVA = DeityInfo(
            type = DeityType.SHIVA,
            nameHindi = "भगवान शिव",
            titleHindi = "देवाधिदेव महादेव",
            sacredShloka = "कर्पूरगौरं करुणावतारं संसारसारम् भुजगेन्द्रहारम्।\nसदावसन्तं हृदयारविन्दे भवं भवानीसहितं नमामि॥",
            shlokaMeaning = "जो कर्पूर के समान धवल, करुणा के अवतार और संसार के सार हैं, उन भगवान शिव को माता पार्वती सहित नमस्कार है।",
            sacredMantra = "ॐ नमः शिवाय",
            devotionalNote = "सोमवार भगवान भोलेनाथ की आराधना का पावन दिन है।"
        )

        val HANUMAN = DeityInfo(
            type = DeityType.HANUMAN,
            nameHindi = "श्री हनुमान",
            titleHindi = "संकटमोचन पवनपुत्र हनुमान",
            sacredShloka = "मनोजवं मारुततुल्यवेगं जितेन्द्रियं बुद्धिमतां वरिष्ठम्।\nवातात्मजं वानरयूथमुख्यं श्रीरामदूतं शरणं प्रपद्ये॥",
            shlokaMeaning = "मन के समान वेग वाले, मारुततुल्य, जितेन्द्रिय और बुद्धिमानों में श्रेष्ठ श्रीरामदूत हनुमान जी को हम नमन करते हैं।",
            sacredMantra = "ॐ हं हनुमते नमः",
            devotionalNote = "मंगलवार को श्री हनुमान जी के पूजन से सभी संकटों का नाश होता है।"
        )

        val GANESHA = DeityInfo(
            type = DeityType.GANESHA,
            nameHindi = "श्री गणेश",
            titleHindi = "विघ्नहर्ता रिद्धि-सिद्धि दाता",
            sacredShloka = "वक्रतुण्ड महाकाय सूर्यकोटि समप्रभ।\nनिर्विघ्नं कुरु मे देव सर्वकार्येषु सर्वदा॥",
            shlokaMeaning = "विशाल शरीर, वक्र सूंड और करोड़ों सूर्यों के समान तेजस्वी देव! हमारे समस्त कार्यों को सदा निर्विघ्न सम्पन्न करें।",
            sacredMantra = "ॐ गं गणपतये नमः",
            devotionalNote = "बुधवार को प्रथम पूज्य भगवान श्री गणेश जी की वंदना से सभी बाधाएं दूर होती हैं।"
        )

        val VISHNU = DeityInfo(
            type = DeityType.VISHNU,
            nameHindi = "भगवान श्री हरि विष्णु",
            titleHindi = "जगत पालक नारायण",
            sacredShloka = "शान्ताकारं भुजगशयनं पद्मनाभं सुरेशं\nविश्वाधारं गगनसदृशं मेघवर्णं शुभाङ्गम्।\nलक्ष्मीकान्तं कमलनयनं योगिभिर्ध्यानगम्यम्\nवन्दे विष्णुं भवभयहरं सर्वलोकैकनाथम्॥",
            shlokaMeaning = "शान्त स्वरूप, शेषनाग की शय्या पर विराजमान, कमल नयन, जगत के आधार भगवान विष्णु को सादर नमन।",
            sacredMantra = "ॐ नमो भगवते वासुदेवाय",
            devotionalNote = "गुरुवार को भगवान विष्णु व देवगुरु बृहस्पति की कृपा से विद्या, सुख और समृद्धि प्राप्त होती है।"
        )

        val LAKSHMI = DeityInfo(
            type = DeityType.LAKSHMI,
            nameHindi = "माँ महालक्ष्मी",
            titleHindi = "धन-धान्य और ऐश्वर्य प्रदायिनी",
            sacredShloka = "नमस्तेऽस्तु महामाये श्रीपीठे सुरपूजिते।\nशङ्खचक्रगदाहस्ते महालक्ष्मि नमोऽस्तु ते॥",
            shlokaMeaning = "हे महामाया, देवताओं द्वारा पूजित और शंख, चक्र, गदा धारण करने वाली भगवती महालक्ष्मी, आपको कोटि-कोटि प्रणाम।",
            sacredMantra = "ॐ श्रीं ह्रीं क्लीं महालक्ष्म्यै नमः",
            devotionalNote = "शुक्रवार माँ भगवती महालक्ष्मी की कृपा और गृह में सुख-समृद्धि का पावन दिवस है।"
        )

        val SHANI = DeityInfo(
            type = DeityType.SHANI,
            nameHindi = "शनिदेव",
            titleHindi = "न्यायप्रिय कर्मफलदाता",
            sacredShloka = "नीलांजन समाभासं रविपुत्रं यमाग्रजम्।\nछायामार्तण्ड संभूतं तं नमामि शनैश्चरम्॥",
            shlokaMeaning = "नीले अंजन के समान कान्ति वाले, सूर्यपुत्र, यमराज के अग्रज छायापुत्र शनिदेव को हम नमन करते हैं।",
            sacredMantra = "ॐ शं शनैश्चराय नमः",
            devotionalNote = "शनिवार को शनिदेव व श्री हनुमान जी की उपासना से कर्म शुद्ध होते हैं और शांति मिलती है।"
        )

        val SURYA = DeityInfo(
            type = DeityType.SURYA,
            nameHindi = "भगवान सूर्य नारायण",
            titleHindi = "प्रत्यक्ष देवता जगत चक्षु",
            sacredShloka = "आदित्यस्य नमस्कारं ये कुर्वन्ति दिने दिने।\nजन्मान्तरसहस्रेषु दारिद्र्यं नोपजायते॥",
            shlokaMeaning = "जो नित्य प्रातःकाल भगवान सूर्य को अर्घ्य देकर प्रणाम करते हैं, उनके जीवन में कभी तेज और तेजस्विता की कमी नहीं होती।",
            sacredMantra = "ॐ सूर्याय नमः",
            devotionalNote = "रविवार को भगवान सूर्य देव को जल अर्पण करने से आरोग्य, तेज और आत्मबल बढ़ता है।"
        )

        val DURGA = DeityInfo(
            type = DeityType.DURGA,
            nameHindi = "माँ जगदम्बा दुर्गा",
            titleHindi = "सर्वमंगल मांगल्ये शिवे सर्वार्थ साधिके",
            sacredShloka = "सर्वमङ्गलमाङ्गल्ये शिवे सर्वार्थसाधिके।\nशरण्ये त्र्यम्बके गौरि नारायणि नमोऽस्तु ते॥",
            shlokaMeaning = "सब मंगलों में मंगल रूप, कल्याणकारी, समस्त मनोरथ सिद्ध करने वाली हे नारायणी! आपको नमस्कार है।",
            sacredMantra = "ॐ दुं दुर्गायै नमः",
            devotionalNote = "नवरात्रि व दुर्गा अष्टमी के पावन अवसर पर शक्ति स्वरूपा माँ दुर्गा की कृपा बरसती है।"
        )

        val KRISHNA = DeityInfo(
            type = DeityType.KRISHNA,
            nameHindi = "भगवान श्री कृष्ण",
            titleHindi = "योगेश्वर परमब्रह्म",
            sacredShloka = "वसुदेवसुतं देवं कंसचाणूरमर्दनम्।\nदेवकीपरमानन्दं कृष्णं वन्दे जगद्गुरुम्॥",
            shlokaMeaning = "कंस और चाणूर का संहार करने वाले, देवकी को परमानंद देने वाले जगद्गुरु श्रीकृष्ण को हम वंदन करते हैं।",
            sacredMantra = "हरे कृष्ण हरे कृष्ण कृष्ण कृष्ण हरे हरे।\nहरे राम हरे राम राम राम हरे हरे॥",
            devotionalNote = "भगवान श्री कृष्ण के स्मरण से जीवन में प्रेम, धर्म और ज्ञान का संचार होता है।"
        )

        val RAM = DeityInfo(
            type = DeityType.RAM,
            nameHindi = "मर्यादा पुरुषोत्तम श्री राम",
            titleHindi = "रघुकुल नंदन श्रीराम",
            sacredShloka = "रामाय रामभद्राय रामचंद्राय वेधसे।\nरघुनाथाय नाथाय सीतायाः पतये नमः॥",
            shlokaMeaning = "रघुनाथ, रामचंद्र, समस्त जगत के स्वामी और माता सीता के पति मर्यादा पुरुषोत्तम प्रभु राम को नमन।",
            sacredMantra = "श्री राम जय राम जय जय राम",
            devotionalNote = "प्रभु श्री राम का नाम स्मरण सभी विघ्नों को हरने वाला और मोक्षदायक है।"
        )
    }
}

data class TithiInfo(
    val name: String,
    val number: Int,
    val paksha: String,
    val endsAt: String,
    val isPurnima: Boolean = false,
    val isAmavasya: Boolean = false,
    val isEkadashi: Boolean = false
)

data class NakshatraInfo(
    val name: String,
    val number: Int,
    val rulerPlanet: String,
    val endsAt: String
)

data class YogaInfo(
    val name: String,
    val number: Int,
    val type: String,
    val endsAt: String
)

data class KaranaInfo(
    val name: String,
    val number: Int,
    val type: String
)

data class MuhuratItem(
    val nameHindi: String,
    val timeRange: String,
    val isAuspicious: Boolean,
    val description: String
)

data class VratItem(
    val id: String,
    val nameHindi: String,
    val dateFormatted: String,
    val associatedDeity: String,
    val shortDesc: String,
    val significance: String,
    val pujaVidhi: String,
    val timings: String,
    val paranaTime: String,
    val vratKatha: String
)

data class FestivalItem(
    val id: String,
    val nameHindi: String,
    val dateFormatted: String,
    val associatedDeity: String,
    val shortDesc: String,
    val significance: String,
    val pujaVidhi: String,
    val timings: String,
    val backgroundStory: String
)

data class DailyPanchang(
    val dateString: String,
    val dayOfMonth: Int,
    val monthOfYear: Int,
    val year: Int,
    val gregorianFormatted: String,
    val weekdayHindi: String,
    val hinduMonthHindi: String,
    val samvatHindi: String,
    val sakaSamvatHindi: String,
    val ayanaHindi: String,
    val rituHindi: String,
    val tithi: TithiInfo,
    val nakshatra: NakshatraInfo,
    val yoga: YogaInfo,
    val karana: KaranaInfo,
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val sunSignHindi: String,
    val moonSignHindi: String,
    val brahmaMuhurat: MuhuratItem,
    val abhijitMuhurat: MuhuratItem,
    val amritKaal: MuhuratItem,
    val vijayaMuhurat: MuhuratItem,
    val godhuliMuhurat: MuhuratItem,
    val rahuKaal: MuhuratItem,
    val yamaganda: MuhuratItem,
    val gulikaKaal: MuhuratItem,
    val durMuhurat: MuhuratItem,
    val vrats: List<VratItem>,
    val festivals: List<FestivalItem>,
    val daySignificance: String,
    val deity: DeityInfo,
    val specialObservance: String,
    val cityName: String
)
