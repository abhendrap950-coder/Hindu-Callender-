package com.example.panchang.engine

import com.example.panchang.model.DeityInfo
import com.example.panchang.model.FestivalItem
import com.example.panchang.model.VratItem
import java.time.LocalDate

object FestivalEngine {

    data class EventResult(
        val vrats: List<VratItem>,
        val festivals: List<FestivalItem>,
        val overrideDeity: DeityInfo?
    )

    fun getEventsForDate(
        date: LocalDate,
        hinduMonth: String,
        paksha: String,
        tithiNumber: Int,
        tithiName: String,
        weekdayHindi: String
    ): EventResult {
        val vrats = mutableListOf<VratItem>()
        val festivals = mutableListOf<FestivalItem>()
        var deityOverride: DeityInfo? = null

        val isShukla = paksha.contains("शुक्ल")
        val isKrishna = paksha.contains("कृष्ण")
        val isEkadashi = tithiNumber == 11 || tithiNumber == 26
        val isTrayodashi = tithiNumber == 13 || tithiNumber == 28
        val isChaturthi = tithiNumber == 4 || tithiNumber == 19
        val isPurnima = tithiNumber == 15
        val isAmavasya = tithiNumber == 30
        val isChaturdashi = tithiNumber == 14 || tithiNumber == 29
        val isAshtami = tithiNumber == 8 || tithiNumber == 23
        val isNavami = tithiNumber == 9 || tithiNumber == 24

        // 1. Ekadashi Vrat
        if (isEkadashi) {
            val ekadashiName = getEkadashiName(hinduMonth, isShukla)
            vrats.add(
                VratItem(
                    id = "ekadashi_${date}",
                    nameHindi = "$ekadashiName एकादशी",
                    dateFormatted = "${date.dayOfMonth} ${date.month.name}",
                    associatedDeity = "भगवान श्री हरि विष्णु",
                    shortDesc = "समस्त पापों का नाश कर मोक्ष एवं श्री हरि की कृपा दिलाने वाला श्रेष्ठ व्रत।",
                    significance = "पद्म पुराण के अनुसार एकादशी व्रत से अश्वमेध यज्ञ के समान पुण्यफल मिलता है और भगवान नारायण की अनन्य कृपा प्राप्त होती है।",
                    pujaVidhi = "प्रातः स्नानोपरांत भगवान विष्णु का पंचामृत से अभिषेक करें, पीले पुष्प, तुलसी दल व नैवेद्य अर्पित करें। 'ॐ नमो भगवते वासुदेवाय' का जाप करें।",
                    timings = "पूजा मुहूर्त: प्रातः 06:15 से 10:30 बजे तक",
                    paranaTime = "द्वादशी तिथि के सूर्योदय पश्चात पारण करें",
                    vratKatha = "एकादशी व्रत की महिमा युधिष्ठिर को स्वयं भगवान श्री कृष्ण ने बतलाई थी। यह समस्त व्रतों में शिरोमणि व्रत है।"
                )
            )
            deityOverride = DeityInfo.VISHNU
        }

        // 2. Pradosh Vrat
        if (isTrayodashi) {
            val pradoshPrefix = when (weekdayHindi) {
                "सोमवार" -> "सोम प्रदोष"
                "मंगलवार" -> "भौम प्रदोष"
                "शनिवार" -> "शनि प्रदोष"
                else -> "प्रदोष"
            }
            vrats.add(
                VratItem(
                    id = "pradosh_${date}",
                    nameHindi = "$pradoshPrefix व्रत",
                    dateFormatted = "${date.dayOfMonth} ${date.month.name}",
                    associatedDeity = "भगवान शिव एवं माता पार्वती",
                    shortDesc = "शाम के समय भगवान शिव की उपासना का अत्यंत पुण्य फलदायी प्रदोष व्रत।",
                    significance = "प्रदोष काल में भगवान आशुतोष कैलाश पर आनंद तांडव करते हैं, इस समय पूजन से समस्त कष्ट और रोग दूर होते हैं।",
                    pujaVidhi = "सायंकाल सूर्यास्त से 45 मिनट पूर्व व पश्चात भगवान शिव का जलाभिषेक करें, बेलपत्र, धतूरा और अक्षत अर्पित करें।",
                    timings = "प्रदोष काल पूजा: सायं 05:45 से 08:15 बजे तक",
                    paranaTime = "अगले दिन प्रातःकाल स्नान-पूजन उपरांत",
                    vratKatha = "विदर्भ देश के धर्मात्मा राजा और ब्राह्मण बालक की शिव भक्ति से जुड़ी पावन प्रदोष व्रत कथा।"
                )
            )
            deityOverride = DeityInfo.SHIVA
        }

        // 3. Chaturthi Vrat
        if (isChaturthi) {
            if (isKrishna) {
                vrats.add(
                    VratItem(
                        id = "sankashti_${date}",
                        nameHindi = "संकष्टी चतुर्थी व्रत",
                        dateFormatted = "${date.dayOfMonth} ${date.month.name}",
                        associatedDeity = "विघ्नहर्ता श्री गणेश",
                        shortDesc = "संकटों के निवारण हेतु संकष्टी श्री गणेश चतुर्थी व्रत।",
                        significance = "भगवान गणेश जी की कृपा से समस्त जीवन के संकट, भय और बाधाओं का नाश होता है।",
                        pujaVidhi = "दिनभर उपवास रखकर चंद्रोदय के समय भगवान गणेश और चंद्र देव को अर्घ्य देकर मोदक का भोग लगाएं।",
                        timings = "गणेश पूजा: सायं 06:00 से | चंद्र दर्शन व अर्घ्य: रात्रि 08:30 बजे",
                        paranaTime = "चंद्रदर्शन एवं अर्घ्य के पश्चात",
                        vratKatha = "राजा हरिश्चंद्र और दुर्वासा ऋषि से संबंधित संकष्टनाशन गणेश कथा।"
                    )
                )
                deityOverride = DeityInfo.GANESHA
            } else {
                vrats.add(
                    VratItem(
                        id = "vinayaka_${date}",
                        nameHindi = "विनायक चतुर्थी व्रत",
                        dateFormatted = "${date.dayOfMonth} ${date.month.name}",
                        associatedDeity = "भगवान श्री गणेश",
                        shortDesc = "शुभता और सफलता प्रदायिनी विनायक चतुर्थी।",
                        significance = "विद्या, बुद्धि और धन की प्राप्ति हेतु विनायक चतुर्थी का व्रत रखा जाता है।",
                        pujaVidhi = "दोपहर के समय भगवान गणेश को दूर्वा, लाल सिंदूर, मोदक और लाल पुष्प अर्पित करें।",
                        timings = "मध्याह्न पूजा मुहूर्त: 11:30 AM से 01:45 PM",
                        paranaTime = "सायंकाल पूजा उपरांत",
                        vratKatha = "भगवान गणेश के जन्म और विघ्नविनाशक स्वरूप की पावन कथा।"
                    )
                )
                deityOverride = DeityInfo.GANESHA
            }
        }

        // 4. Masik Shivratri
        if (isKrishna && isChaturdashi) {
            vrats.add(
                VratItem(
                    id = "shivratri_${date}",
                    nameHindi = "मासिक शिवरात्रि",
                    dateFormatted = "${date.dayOfMonth} ${date.month.name}",
                    associatedDeity = "भगवान शिव",
                    shortDesc = "हर माह कृष्ण पक्ष की चतुर्दशी को शिव कृपा प्राप्ति का मासिक शिवरात्रि व्रत।",
                    significance = "शिवरात्रि के दिन शिवलिङ्ग पूजन से सभी मनोकामनाएं पूर्ण होती हैं।",
                    pujaVidhi = "रात्रि के चारों प्रहर में भगवान शिव का रुद्राभिषेक करें, 'महामृत्युंजय मंत्र' का जप करें।",
                    timings = "निशीथ काल पूजा: रात्रि 11:45 से 12:35 तक",
                    paranaTime = "अगले दिन प्रातःकाल",
                    vratKatha = "शिवपुराण में वर्णित भगवान शिव के ज्योतिर्लिंग प्राकट्य की पावन कथा।"
                )
            )
            deityOverride = DeityInfo.SHIVA
        }

        // 5. Purnima & Amavasya
        if (isPurnima) {
            festivals.add(
                FestivalItem(
                    id = "purnima_${date}",
                    nameHindi = "$hinduMonth पूर्णिमा",
                    dateFormatted = "${date.dayOfMonth} ${date.month.name}",
                    associatedDeity = "श्री सत्यनारायण भगवान व चंद्र देव",
                    shortDesc = "पवित्र स्नान, दान, जप और श्री सत्यनारायण पूजन का महापुण्य दिवस।",
                    significance = "पूर्णिमा के दिन गंगा स्नान और सत्यनारायण कथा श्रवण से घर में सुख, शांति और समृद्धि आती है।",
                    pujaVidhi = "सत्यनारायण भगवान की चौकी सजाकर कथा श्रवण करें, पंजीरी और पंचामृत का भोग लगाएं।",
                    timings = "पूजन मुहूर्त: प्रातः 08:00 से दोपहर 12:00 तक | चंद्र अर्घ्य: सायं 06:45",
                    backgroundStory = "स्कंद पुराण के रेवाखंड में वर्णित भगवान विष्णु की सत्यनारायण कथा।"
                )
            )
            deityOverride = DeityInfo.VISHNU
        }

        if (isAmavasya) {
            festivals.add(
                FestivalItem(
                    id = "amavasya_${date}",
                    nameHindi = "$hinduMonth अमावस्या",
                    dateFormatted = "${date.dayOfMonth} ${date.month.name}",
                    associatedDeity = "पितृ देव एवं भगवान सूर्य",
                    shortDesc = "पितृ तर्पण, श्राद्ध, दान और आध्यात्मिक शांति का दिन।",
                    significance = "अमावस्या पर पितरों के निमित्त तर्पण, पिंडदान और ब्राह्मण भोजन कराने से पितृदोष दूर होता है।",
                    pujaVidhi = "प्रातः जल में काले तिल डालकर सूर्य व पितरों को अर्घ्य दें, पीपल वृक्ष पर जल चढ़ाएं और दीप जलाएं।",
                    timings = "पितृ तर्पण समय: प्रातः 11:30 से 12:45 तक",
                    backgroundStory = "महाभारत व गरुड़ पुराण में वर्णित पितृ ऋण मुक्ति की विधि।"
                )
            )
            deityOverride = DeityInfo.SURYA
        }

        // 6. Major Monthly / Seasonal Festivals according to Hindu Month & Tithi:
        when (hinduMonth) {
            "आश्विन" -> {
                if (isKrishna && tithiNumber == 30) {
                    festivals.add(
                        FestivalItem(
                            id = "sarva_pitru_amavasya",
                            nameHindi = "सर्वपितृ अमावस्या",
                            dateFormatted = "${date.dayOfMonth} अक्टूबर",
                            associatedDeity = "समस्त पितृगण",
                            shortDesc = "पितृ पक्ष का समापन दिवस, सभी ज्ञात-अज्ञात पितरों के तर्पण का महादिन।",
                            significance = "इस दिन तर्पण व श्राद्ध से सभी पूर्वज तृप्त होकर वंशजों को सुख, समृद्धि का आशीर्वाद देते हैं।",
                            pujaVidhi = "पितरों के लिए खीर-पूरी का भोग, पंचबलि (गाय, कुत्ता, कौआ, देव, चींटी) और दान।",
                            timings = "कुतुप व रौहिण मुहूर्त: 11:45 AM - 01:15 PM",
                            backgroundStory = "गरुड़ पुराण अनुसार पितृ पक्ष के अंत में पितर अपने लोक लौटते हैं।"
                        )
                    )
                }
                if (isShukla && tithiNumber == 1) {
                    festivals.add(
                        FestivalItem(
                            id = "navratri_start",
                            nameHindi = "शारदीय नवरात्रि घटस्थापना",
                            dateFormatted = "${date.dayOfMonth} अक्टूबर",
                            associatedDeity = "माँ शैलपुत्री / माँ जगदम्बा दुर्गा",
                            shortDesc = "शारदीय नवरात्रि का प्रथम पावन दिन, कलश स्थापना एवं माँ शैलपुत्री पूजन।",
                            significance = "नवरात्रि में माँ दुर्गा की नौ रूपों में आराधना से समस्त पापों का संहार और शक्ति की प्राप्ति होती है।",
                            pujaVidhi = "शुभ मुहूर्त में घट स्थापना, अखंड ज्योति प्रज्वलन, जौ बोना और दुर्गा सप्तशती पाठ।",
                            timings = "घटस्थापना मुहूर्त: प्रातः 06:18 से 10:14 तक",
                            backgroundStory = "महिषासुरमर्दिनी माँ भगवती दुर्गा के प्राकट्य और विजय का पर्व।"
                        )
                    )
                    deityOverride = DeityInfo.DURGA
                }
                if (isShukla && tithiNumber == 8) {
                    festivals.add(
                        FestivalItem(
                            id = "durga_ashtami",
                            nameHindi = "दुर्गा महाअष्टमी (महागौरी पूजन)",
                            dateFormatted = "${date.dayOfMonth} अक्टूबर",
                            associatedDeity = "माँ महागौरी",
                            shortDesc = "शारदीय नवरात्रि की महाअष्टमी, महागौरी पूजन, कन्या पूजन व संधि पूजा।",
                            significance = "माँ महागौरी भक्तों के सभी संचित पापों को धोकर परम पद और सौभाग्य प्रदान करती हैं।",
                            pujaVidhi = "माँ को नारियल, हलवा, चना व चुनरी अर्पित करें, नौ कन्याओं और एक बटुक का पूजन करें।",
                            timings = "अष्टमी पूजन: प्रातः 07:00 से 11:30 बजे",
                            backgroundStory = "कठोर तपस्या के उपरांत भगवान शिव द्वारा गंगा जल से माँ पार्वती को महागौरी स्वरूप प्रदान किया गया।"
                        )
                    )
                    deityOverride = DeityInfo.DURGA
                }
                if (isShukla && tithiNumber == 9) {
                    festivals.add(
                        FestivalItem(
                            id = "maha_navami",
                            nameHindi = "महानवमी (माँ सिद्धिदात्री पूजन)",
                            dateFormatted = "${date.dayOfMonth} अक्टूबर",
                            associatedDeity = "माँ सिद्धिदात्री",
                            shortDesc = "नवरात्रि की पावन नवमी, समस्त सिद्धियों की दात्री माँ सिद्धिदात्री पूजन।",
                            significance = "नवरात्रि अनुष्ठान की पूर्णाहुति, हवन व कन्या भोज का अत्यंत पावन अवसर।",
                            pujaVidhi = "माँ सिद्धिदात्री को कमल पुष्प, नैवेद्य अर्पित कर नवरात्रि हवन संपन्न करें।",
                            timings = "हवन एवं कन्या पूजन: प्रातः 08:00 से 12:30",
                            backgroundStory = "भगवान शिव ने भी माँ सिद्धिदात्री की कृपा से समस्त सिद्धियां प्राप्त की थीं।"
                        )
                    )
                    deityOverride = DeityInfo.DURGA
                }
                if (isShukla && tithiNumber == 10) {
                    festivals.add(
                        FestivalItem(
                            id = "dussehra",
                            nameHindi = "विजयादशमी (दशहरा)",
                            dateFormatted = "${date.dayOfMonth} अक्टूबर",
                            associatedDeity = "मर्यादा पुरुषोत्तम प्रभु श्री राम",
                            shortDesc = "अधर्म पर धर्म और असत्य पर सत्य की विजय का महापर्व दशहरा।",
                            significance = "प्रभु श्री राम द्वारा रावण वध तथा माँ दुर्गा द्वारा महिषासुर संहार का विजय उत्सव।",
                            pujaVidhi = "शमी वृक्ष पूजन, अपराजिता पूजन, अस्त्र-शस्त्र पूजन और रावण दहन।",
                            timings = "विजय मुहूर्त: दोपहर 01:58 से 02:44 तक",
                            backgroundStory = "लंका पर विजय प्राप्त कर धर्म की पुनः स्थापना का गौरवमयी दिन।"
                        )
                    )
                    deityOverride = DeityInfo.RAM
                }
                if (isShukla && tithiNumber == 15) {
                    festivals.add(
                        FestivalItem(
                            id = "sharad_purnima",
                            nameHindi = "शरद पूर्णिमा (कोजागरी पूर्णिमा)",
                            dateFormatted = "${date.dayOfMonth} अक्टूबर",
                            associatedDeity = "माँ महालक्ष्मी एवं भगवान श्री कृष्ण",
                            shortDesc = "अमृत वर्षा की रात्रि, महालक्ष्मी जी का प्राकट्य दिवस एवं महारास उत्सव।",
                            significance = "इस रात्रि चंद्रमा की किरणों से अमृत बरसता है। माँ लक्ष्मी पृथ्वी पर भ्रमण कर 'को जागर्ति' (कौन जाग रहा है) पूछती हैं।",
                            pujaVidhi = "रात को खुले आकाश के नीचे खीर रखें और अगले दिन प्रसाद रूप में ग्रहण करें।",
                            timings = "लक्ष्मी पूजा: निशीथ काल रात्रि 11:40 से 12:30 तक",
                            backgroundStory = "गोलोक में भगवान श्री कृष्ण द्वारा गोपियों संग दिव्य महारास का अलौकिक दिन।"
                        )
                    )
                    deityOverride = DeityInfo.LAKSHMI
                }
            }

            "कार्तिक" -> {
                if (isKrishna && tithiNumber == 19) { // 4th of Krishna
                    festivals.add(
                        FestivalItem(
                            id = "karwa_chauth",
                            nameHindi = "करवा चौथ (करक चतुर्थी)",
                            dateFormatted = "${date.dayOfMonth} कार्तिक",
                            associatedDeity = "माँ पार्वती, भगवान शिव व चंद्र देव",
                            shortDesc = "सुहागिन महिलाओं द्वारा पति की दीर्घायु और अखंड सौभाग्य हेतु निर्जला व्रत।",
                            significance = "पति-पत्नी के पावन प्रेम और दाम्पत्य जीवन में सुख, शांति का प्रतीक।",
                            pujaVidhi = "करवा चौथ माता का चित्र बनाकर पूजन, कथा श्रवण, छलनी से चंद्र दर्शन और पति का आशीर्वाद।",
                            timings = "पूजा समय: सायं 05:45 - 07:05 | चंद्रोदय: रात्रि 08:15",
                            backgroundStory = "वीरवती और करवा नामक पतिव्रता स्त्री की अमर कथा।"
                        )
                    )
                    deityOverride = DeityInfo.SHIVA
                }
                if (isKrishna && tithiNumber == 23) { // 8th of Krishna
                    festivals.add(
                        FestivalItem(
                            id = "ahoi_ashtami",
                            nameHindi = "अहोई अष्टमी व्रत",
                            dateFormatted = "${date.dayOfMonth} कार्तिक",
                            associatedDeity = "अहोई माता (माँ पार्वती)",
                            shortDesc = "संतान की दीर्घायु, उत्तम स्वास्थ्य और मंगल कामना हेतु माताओं का निर्जला व्रत।",
                            significance = "अहोई माता की कृपा से संतान पर आने वाले सभी संकट टल जाते हैं।",
                            pujaVidhi = "सायंकाल अहोई माता की पूजा और तारों को अर्घ्य देकर व्रत का पारण।",
                            timings = "तारों को अर्घ्य देने का समय: सायं 06:15",
                            backgroundStory = "सात पुत्रों की माता की साही के बच्चे और अहोई माता के आशीर्वाद की कथा।"
                        )
                    )
                    deityOverride = DeityInfo.DURGA
                }
                if (isKrishna && tithiNumber == 28) { // 13th of Krishna
                    festivals.add(
                        FestivalItem(
                            id = "dhanteras",
                            nameHindi = "धनतेरस (धनत्रयोदशी)",
                            dateFormatted = "${date.dayOfMonth} कार्तिक",
                            associatedDeity = "भगवान धन्वंतरि एवं कुबेर देव",
                            shortDesc = "आरोग्य के देवता भगवान धन्वंतरि जयंती, यम दीपदान एवं नवीन वस्तुओं का क्रय।",
                            significance = "समुद्र मंथन से भगवान धन्वंतरि अमृत कलश लेकर प्रकट हुए थे। इस दिन सोना, चांदी या बर्तन खरीदना अत्यंत शुभ माना जाता है।",
                            pujaVidhi = "भगवान धन्वंतरि, कुबेर देव व माँ लक्ष्मी का पूजन। दक्षिण दिशा में यमराज हेतु चार मुखी दीपक।",
                            timings = "प्रदोष काल मुहूर्त: सायं 05:50 से 08:15 तक",
                            backgroundStory = "आयुर्वेद के जनक भगवान धन्वंतरि का अमृत कलश सहित अवतरण।"
                        )
                    )
                    deityOverride = DeityInfo.LAKSHMI
                }
                if (isKrishna && tithiNumber == 29) { // 14th of Krishna
                    festivals.add(
                        FestivalItem(
                            id = "chhoti_diwali",
                            nameHindi = "नरक चतुर्दशी (छोटी दीपावली / रूप चौदस)",
                            dateFormatted = "${date.dayOfMonth} कार्तिक",
                            associatedDeity = "भगवान श्री कृष्ण एवं यमराज",
                            shortDesc = "नरकासुर वध का स्मृति दिवस, रूप निखार, उबटन स्नान और यम दीपदान।",
                            significance = "नरकासुर के भय से मुक्ति और 16,100 कन्याओं का उद्धार कर सत्यभामा-कृष्ण द्वारा अधर्म का विनाश।",
                            pujaVidhi = "सूर्योदय पूर्व तैल व उबटन स्नान, सायं 14 दीपक जलाकर यम तर्पण।",
                            timings = "अभ्यंग स्नान: प्रातः 05:00 से 06:15 | दीपदान: सायं 06:00",
                            backgroundStory = "भगवान श्री कृष्ण द्वारा अत्याचारी नरकासुर का वध।"
                        )
                    )
                    deityOverride = DeityInfo.KRISHNA
                }
                if (isKrishna && tithiNumber == 30) { // Amavasya
                    festivals.add(
                        FestivalItem(
                            id = "diwali",
                            nameHindi = "दीपावली (महालक्ष्मी पूजन)",
                            dateFormatted = "${date.dayOfMonth} कार्तिक",
                            associatedDeity = "माँ महालक्ष्मी, श्री गणेश एवं कुबेर देव",
                            shortDesc = "सनातन धर्म का सबसे प्रमुख दीपोत्सव, महालक्ष्मी पूजन, प्रकाश और आनंद का महापर्व।",
                            significance = "समुद्र मंथन से माँ लक्ष्मी का प्राकट्य तथा 14 वर्ष के वनवास उपरांत प्रभु श्री राम का अयोध्या आगमन।",
                            pujaVidhi = "कमल, खीर, बताशे, पंचामृत से लक्ष्मी-गणेश-कुबेर पूजन, बहीखाता पूजन, घर-घर दीप प्रज्वलन।",
                            timings = "प्रदोष काल मुहूर्त: 05:42 PM - 08:12 PM | निशीथ काल: 11:38 PM - 12:30 AM",
                            backgroundStory = "अयोध्या वासियों द्वारा घी के दीपक जलाकर प्रभु राम का स्वागत एवं माँ लक्ष्मी का आशीर्वाद।"
                        )
                    )
                    deityOverride = DeityInfo.LAKSHMI
                }
                if (isShukla && tithiNumber == 1) {
                    festivals.add(
                        FestivalItem(
                            id = "govardhan_puja",
                            nameHindi = "गोवर्धन पूजा (अन्नकूट)",
                            dateFormatted = "${date.dayOfMonth} कार्तिक",
                            associatedDeity = "भगवान श्री कृष्ण (गिरिराज जी)",
                            shortDesc = "इंद्र के मानमर्दन का पर्व, गिरिराज गोवर्धन पूजन, गायों की सेवा एवं 56 भोग अन्नकूट।",
                            significance = "प्रकृति और गौवंश के प्रति कृतज्ञता व्यक्त करने का पावन पर्व।",
                            pujaVidhi = "गोबर से गोवर्धन पर्वत बनाकर पूजन, परिक्रमा और छप्पन भोग का नैवेद्य।",
                            timings = "प्रातःकाल मुहूर्त: 06:20 से 08:45 तक",
                            backgroundStory = "भगवान श्री कृष्ण द्वारा कनिष्ठिका उंगली पर गोवर्धन पर्वत उठाकर ब्रजवासियों की रक्षा।"
                        )
                    )
                    deityOverride = DeityInfo.KRISHNA
                }
                if (isShukla && tithiNumber == 2) {
                    festivals.add(
                        FestivalItem(
                            id = "bhai_dooj",
                            nameHindi = "भाई दूज (यम द्वितीया)",
                            dateFormatted = "${date.dayOfMonth} कार्तिक",
                            associatedDeity = "यमराज एवं यमुना जी",
                            shortDesc = "भाई-बहन के पवित्र प्रेम और स्नेह का पर्व, बहन द्वारा भाई के दीर्घायु की मंगलकामना।",
                            significance = "यमराज अपनी बहन यमुना के घर भोजन करने आए थे और वरदान दिया था कि इस दिन जो भाई बहन के हाथ से तिलक लगवाएगा, उसे अकाल मृत्यु का भय नहीं होगा।",
                            pujaVidhi = "बहन द्वारा भाई का तिलक, आरती, मिष्ठान खिलाना और भाई द्वारा उपहार देना।",
                            timings = "शुभ तिलक मुहूर्त: दोपहर 01:10 से 03:25 तक",
                            backgroundStory = "यमराज और यमुना जी के दिव्य भ्रातृ-भगिनी स्नेह की अमर गाथा।"
                        )
                    )
                }
                if (isShukla && tithiNumber == 6) {
                    festivals.add(
                        FestivalItem(
                            id = "chhath_puja",
                            nameHindi = "महापर्व छठ पूजा (सूर्य षष्ठी संध्या अर्घ्य)",
                            dateFormatted = "${date.dayOfMonth} कार्तिक",
                            associatedDeity = "भगवान सूर्य देव एवं छठी मइया",
                            shortDesc = "प्रकृति, सूर्य और जल की उपासना का चार दिवसीय सबसे कठोर और पवित्र लोकपर्व।",
                            significance = "संतान की दीर्घायु, परिवार के आरोग्य और सुख-समृद्धि हेतु अस्ताचलगामी व उदीयमान सूर्य को अर्घ्य।",
                            pujaVidhi = "नदी/सरोवर के जल में खड़े होकर सूप में फल, ठेकुआ रखकर अस्ताचलगामी सूर्य को अर्घ्य देना।",
                            timings = "संध्या अर्घ्य: सायं 05:25 से सूर्यास्त तक",
                            backgroundStory = "माता सीता और द्रौपदी द्वारा सूर्योपासना से समस्त मनोरथ सिद्ध करने का प्रसंग।"
                        )
                    )
                    deityOverride = DeityInfo.SURYA
                }
                if (isShukla && tithiNumber == 11) {
                    festivals.add(
                        FestivalItem(
                            id = "dev_uthani_ekadashi",
                            nameHindi = "देवउठनी एकादशी (प्रबोधिनी एकादशी)",
                            dateFormatted = "${date.dayOfMonth} कार्तिक",
                            associatedDeity = "भगवान श्री हरि विष्णु",
                            shortDesc = "चार माह के चातुर्मास शयन पश्चात भगवान विष्णु का जागरण, मांगलिक कार्यों का शुभारंभ।",
                            significance = "इस दिन से विवाह, मुंडन, गृह प्रवेश आदि समस्त मांगलिक कार्य प्रारंभ हो जाते हैं।",
                            pujaVidhi = "ईख (गन्ना), सिंघाड़ा, बेर से भगवान विष्णु की पूजा, 'उत्तिष्ठ उत्तिष्ठ गोविंद' कहकर देव जागरण।",
                            timings = "पूजन समय: सायं 06:00 से रात्रि 09:30",
                            backgroundStory = "भगवान विष्णु क्षीरसागर में योगनिद्रा से जागकर पुनः सृष्टि का कार्यभार संभालते हैं।"
                        )
                    )
                    deityOverride = DeityInfo.VISHNU
                }
                if (isShukla && tithiNumber == 12) {
                    festivals.add(
                        FestivalItem(
                            id = "tulsi_vivah",
                            nameHindi = "तुलसी विवाह",
                            dateFormatted = "${date.dayOfMonth} कार्तिक",
                            associatedDeity = "माता तुलसी एवं भगवान शालिग्राम",
                            shortDesc = "माँ तुलसी और भगवान शालिग्राम जी का विवाह उत्सव, कन्यादान के समान पुण्य फल।",
                            significance = "तुलसी विवाह कराने से वैवाहिक जीवन सुखमय होता है और मोक्ष की प्राप्ति होती है।",
                            pujaVidhi = "तुलसी के पौधे को लाल चुनरी ओढ़ाकर शालिग्राम जी के साथ वैदिक रीति से गठबंधन व फेरे।",
                            timings = "गोधूलि मुहूर्त: सायं 05:35 से 07:15 तक",
                            backgroundStory = "वृंदा (तुलसी) और भगवान विष्णु के शालिग्राम स्वरूप के अलौकिक संबंध की कथा।"
                        )
                    )
                    deityOverride = DeityInfo.VISHNU
                }
                if (isShukla && tithiNumber == 15) {
                    festivals.add(
                        FestivalItem(
                            id = "dev_diwali",
                            nameHindi = "देव दीपावली (कार्तिक पूर्णिमा)",
                            dateFormatted = "${date.dayOfMonth} कार्तिक",
                            associatedDeity = "भगवान शिव एवं गंगा माता",
                            shortDesc = "काशी के घाटों पर देवताओं द्वारा दीपोत्सव, त्रिपुरारी शिव विजयोत्सव एवं महापुण्य स्नान।",
                            significance = "भगवान शिव ने त्रिपुरासुर का वध किया था, जिसकी खुशी में देवताओं ने स्वर्ग से उतरकर दीप जलाए थे।",
                            pujaVidhi = "पवित्र नदियों में स्नान, घाटों व देवालयों में दीपदान, भगवान शिव का अभिषेक।",
                            timings = "प्रदोष काल दीपदान: सायं 05:20 से 07:45 तक",
                            backgroundStory = "त्रिपुरासुर वध के उपरांत देवाधिदेव महादेव की विजय पर देवों का उत्सव।"
                        )
                    )
                    deityOverride = DeityInfo.SHIVA
                }
            }

            "मार्गशीर्ष" -> {
                if (isShukla && tithiNumber == 11) {
                    festivals.add(
                        FestivalItem(
                            id = "gita_jayanti",
                            nameHindi = "गीता जयंती (मोक्षदा एकादशी)",
                            dateFormatted = "${date.dayOfMonth} मार्गशीर्ष",
                            associatedDeity = "योगेश्वर श्री कृष्ण",
                            shortDesc = "कुरुक्षेत्र में भगवान श्री कृष्ण द्वारा अर्जुन को श्रीमद्भगवद्गीता के उपदेश का अवतरण दिवस।",
                            significance = "गीता ज्ञान का अमृत जीवन के समस्त संशयों और दुखों को हरने वाला सनातन प्रकाश है।",
                            pujaVidhi = "गीता पुस्तक का पूजन, 11वें अध्याय का पाठ और भगवान कृष्ण की आरती।",
                            timings = "प्रातः 07:30 से 11:00 बजे तक",
                            backgroundStory = "महाभारत युद्ध से पूर्व अर्जुन को कर्मयोग और आत्मज्ञान का दिव्य संदेश।"
                        )
                    )
                    deityOverride = DeityInfo.KRISHNA
                }
            }

            "माघ" -> {
                if (isShukla && tithiNumber == 5) {
                    festivals.add(
                        FestivalItem(
                            id = "vasant_panchami",
                            nameHindi = "बसंत पंचमी (सरस्वती पूजा)",
                            dateFormatted = "${date.dayOfMonth} माघ",
                            associatedDeity = "विद्या दायिनी माँ सरस्वती",
                            shortDesc = "ऋतुराज बसंत का आगमन, विद्या, बुद्धि, कला और वाणी की देवी माँ सरस्वती का प्राकट्य।",
                            significance = "ज्ञान और विद्या के आरंभ (अक्षरारंभ) हेतु अत्यंत अबूझ और शुभ मुहूर्त।",
                            pujaVidhi = "पीले वस्त्र धारण कर माँ सरस्वती को पीले पुष्प, पीली मिठाई, पुस्तक व वाद्य यंत्र अर्पित करें।",
                            timings = "पूजा मुहूर्त: प्रातः 07:15 से दोपहर 12:35 तक",
                            backgroundStory = "ब्रह्मा जी के मुख से ज्ञान की देवी वीणावादिनी माँ सरस्वती का प्राकट्य।"
                        )
                    )
                    deityOverride = DeityInfo.DURGA
                }
            }

            "फाल्गुन" -> {
                if (isKrishna && (tithiNumber == 28 || tithiNumber == 29)) {
                    festivals.add(
                        FestivalItem(
                            id = "maha_shivratri",
                            nameHindi = "महाशिवरात्रि महापर्व",
                            dateFormatted = "${date.dayOfMonth} फाल्गुन",
                            associatedDeity = "देवाधिदेव महादेव एवं माता पार्वती",
                            shortDesc = "शिव-शक्ति के मिलन की पावन महारात्रि, शिवलिंग प्राकट्य दिवस एवं महाव्रत।",
                            significance = "इस रात्रि जागरण और शिवलिंग पर जलाभिषेक से करोड़ों जन्मों के पाप नष्ट होते हैं।",
                            pujaVidhi = "चार प्रहर पूजा, जल, दूध, दही, शहद, बेलपत्र, भांग, धतूरा से अभिषेक और 'ॐ नमः शिवाय' जप।",
                            timings = "निशीथ काल: रात्रि 11:45 से 12:35 | चार प्रहर पूजन रात्रि भर",
                            backgroundStory = "भगवान शिव और माता पार्वती का पावन वैवाहिक मिलन एवं ज्योतिर्लिंग का प्राकट्य।"
                        )
                    )
                    deityOverride = DeityInfo.SHIVA
                }
                if (isShukla && tithiNumber == 15) {
                    festivals.add(
                        FestivalItem(
                            id = "holika_dahan",
                            nameHindi = "होलिका दहन (फाल्गुनी पूर्णिमा)",
                            dateFormatted = "${date.dayOfMonth} फाल्गुन",
                            associatedDeity = "भगवान नृसिंह एवं भक्त प्रह्लाद",
                            shortDesc = "बुराई पर अच्छाई और अहंकार पर भक्ति की विजय, होलिका दहन का पावन पर्व।",
                            significance = "अग्नि में समस्त नकारात्मकताओं की आहुति देकर नए उल्लास का स्वागत।",
                            pujaVidhi = "होलिका पूजन, अक्षत, रोली, पुष्प, गोबर के उपले, गेहूं की बालियां अर्पित कर परिक्रमा।",
                            timings = "होलिका दहन मुहूर्त: रात्रि 07:15 से 09:30 तक",
                            backgroundStory = "अग्नि में होलिका का भस्म होना और भगवान की कृपा से भक्त प्रह्लाद की रक्षा।"
                        )
                    )
                    deityOverride = DeityInfo.VISHNU
                }
            }

            "चैत्र" -> {
                if (isKrishna && tithiNumber == 16) {
                    festivals.add(
                        FestivalItem(
                            id = "dhulandi_holi",
                            nameHindi = "होली (रंगोत्सव / धुलेंडी)",
                            dateFormatted = "${date.dayOfMonth} चैत्र",
                            associatedDeity = "राधा-कृष्ण",
                            shortDesc = "रंगों, प्रेम और आनंद का महापर्व, आपसी बैर मिटाकर गले मिलने का दिन।",
                            significance = "वसंत ऋतु के उल्लास और सामाजिक समरसता का प्रतीक।",
                            pujaVidhi = "भगवान को अबीर-गुलाल अर्पित कर बड़ों का आशीर्वाद लेना।",
                            timings = "प्रातःकाल से दोपहर तक रंगोत्सव",
                            backgroundStory = "ब्रज की लठमार व फूलों की होली, राधा-कृष्ण की प्रेम लीला।"
                        )
                    )
                    deityOverride = DeityInfo.KRISHNA
                }
                if (isShukla && tithiNumber == 1) {
                    festivals.add(
                        FestivalItem(
                            id = "chaitra_navratri",
                            nameHindi = "हिन्दू नववर्ष (नव संवत्सरारंभ / चैत्र नवरात्रि)",
                            dateFormatted = "${date.dayOfMonth} चैत्र",
                            associatedDeity = "ब्रह्मा जी एवं माँ शैलपुत्री",
                            shortDesc = "भारतीय सनातन नवसंवत्सर का शुभारंभ एवं चैत्र वासंती नवरात्रि घटस्थापना।",
                            significance = "इसी दिन ब्रह्मा जी ने सृष्टि की रचना प्रारंभ की थी।",
                            pujaVidhi = "प्रातः सूर्य अर्घ्य, ध्वजारोहण, नीम की पत्ती और मिश्री का प्रसाद, घटस्थापना।",
                            timings = "घटस्थापना: प्रातः 06:12 से 10:20 तक",
                            backgroundStory = "सम्राट विक्रमादित्य द्वारा विक्रम संवत का प्रवर्तन एवं सृष्टि का आरंभ।"
                        )
                    )
                    deityOverride = DeityInfo.DURGA
                }
                if (isShukla && tithiNumber == 9) {
                    festivals.add(
                        FestivalItem(
                            id = "ram_navami",
                            nameHindi = "श्री राम नवमी",
                            dateFormatted = "${date.dayOfMonth} चैत्र",
                            associatedDeity = "मर्यादा पुरुषोत्तम प्रभु श्री राम",
                            shortDesc = "अयोध्या में मर्यादा पुरुषोत्तम भगवान श्री राम का पावन प्राकट्य जन्मोत्सव।",
                            significance = "कौशल्या नंदन प्रभु श्री राम के जन्मोत्सव से समस्त जगत में धर्म का प्रकाश फैला।",
                            pujaVidhi = "मध्याह्न 12 बजे 'भए प्रगट कृपाला' स्तुति, रामलला का पंचामृत अभिषेक, पंजीरी भोग और जन्मोत्सव।",
                            timings = "श्री राम जन्मोत्सव मुहूर्त: 11:05 AM से 01:35 PM",
                            backgroundStory = "त्रेतायुग में अधर्म के विनाश हेतु साक्षात नारायण का श्री राम रूप में अवतार।"
                        )
                    )
                    deityOverride = DeityInfo.RAM
                }
                if (isShukla && tithiNumber == 15) {
                    festivals.add(
                        FestivalItem(
                            id = "hanuman_jayanti",
                            nameHindi = "हनुमान जयंती (चैत्र पूर्णिमा)",
                            dateFormatted = "${date.dayOfMonth} चैत्र",
                            associatedDeity = "संकटमोचन श्री हनुमान",
                            shortDesc = "पवनपुत्र, अंजनी नंदन, भगवान शिव के रुद्रावतार श्री हनुमान जी का जन्मोत्सव।",
                            significance = "हनुमान चालीसा और सुंदरकांड के पाठ से समस्त भय, रोग और भूत-पिशाच बाधाएं दूर होती हैं।",
                            pujaVidhi = "सिंदूर और चमेली का तेल अर्पण, चोला चढ़ाना, बूंदी और लड्डू का भोग लगाना।",
                            timings = "प्रातः 06:10 से 11:30 तक | सायं सुंदरकांड पाठ",
                            backgroundStory = "माता अंजनी और केसरी के गृह में रुद्र के ग्यारहवें अवतार का प्राकट्य।"
                        )
                    )
                    deityOverride = DeityInfo.HANUMAN
                }
            }

            "वैशाख" -> {
                if (isShukla && tithiNumber == 3) {
                    festivals.add(
                        FestivalItem(
                            id = "akshaya_tritiya",
                            nameHindi = "अक्षय तृतीया (आखा तीज)",
                            dateFormatted = "${date.dayOfMonth} वैशाख",
                            associatedDeity = "माँ लक्ष्मी एवं भगवान परशुराम",
                            shortDesc = "अक्षय पुण्य का महापर्व, अबूझ सावा, स्वर्ण क्रय एवं भगवान परशुराम जयंती।",
                            significance = "इस दिन किया गया दान, जप, तप कभी क्षय नहीं होता।",
                            pujaVidhi = "भगवान विष्णु व लक्ष्मी जी की पूजा, कलश, जल, पंखा, छाता का दान और स्वर्ण क्रय।",
                            timings = "पूजन एवं खरीदारी मुहूर्त: प्रातः 05:45 से दोपहर 12:20 तक",
                            backgroundStory = "भगवान परशुराम जी का प्राकट्य एवं त्रेतायुग का आरंभ।"
                        )
                    )
                    deityOverride = DeityInfo.LAKSHMI
                }
            }

            "ज्येष्ठ" -> {
                if (isShukla && tithiNumber == 10) {
                    festivals.add(
                        FestivalItem(
                            id = "ganga_dussehra",
                            nameHindi = "गंगा दशहरा",
                            dateFormatted = "${date.dayOfMonth} ज्येष्ठ",
                            associatedDeity = "पतित पावनी माँ गंगा",
                            shortDesc = "माँ गंगा का स्वर्ग से पृथ्वी पर अवतरण दिवस, दस प्रकार के पापों का शमन।",
                            significance = "गंगा स्नान और 10 प्रकार की वस्तुओं के दान से समस्त कायिक, वाचिक व मानसिक पाप नष्ट होते हैं।",
                            pujaVidhi = "गंगा जल में स्नान या सामान्य जल में गंगाजल मिलाकर स्नान, 10 दीप दान, 10 फल दान।",
                            timings = "हस्त नक्षत्र स्नान मुहूर्त: प्रातः 05:25 से 09:30",
                            backgroundStory = "महाराज भगीरथ की कठोर तपस्या से माँ गंगा का धरा पर अवतरण।"
                        )
                    )
                }
            }

            "आषाढ़" -> {
                if (isShukla && tithiNumber == 11) {
                    festivals.add(
                        FestivalItem(
                            id = "devshayani_ekadashi",
                            nameHindi = "देवशयनी एकादशी (चातुर्मास प्रारंभ)",
                            dateFormatted = "${date.dayOfMonth} आषाढ़",
                            associatedDeity = "भगवान श्री हरि विष्णु",
                            shortDesc = "भगवान विष्णु का क्षीरसागर में चार माह का शयन, चातुर्मास व्रत-नियम प्रारंभ।",
                            significance = "चार महीने तक विवाह आदि मांगलिक कार्य विराम लेते हैं और जप-तप का काल प्रारंभ होता है।",
                            pujaVidhi = "भगवान विष्णु को शयन कराने का संकल्प, एकादशी व्रत और चातुर्मास नियम धारण।",
                            timings = "प्रातः 06:15 से 10:45",
                            backgroundStory = "भगवान विष्णु राजा बलि के पाताल लोक से लौटकर क्षीरसागर में शयन करते हैं।"
                        )
                    )
                    deityOverride = DeityInfo.VISHNU
                }
                if (isShukla && tithiNumber == 15) {
                    festivals.add(
                        FestivalItem(
                            id = "guru_purnima",
                            nameHindi = "गुरु पूर्णिमा (व्यास पूर्णिमा)",
                            dateFormatted = "${date.dayOfMonth} आषाढ़",
                            associatedDeity = "महर्षि वेदव्यास एवं सद्गुरु",
                            shortDesc = "आदिगुरु महर्षि वेदव्यास जयंती एवं सद्गुरु के प्रति श्रद्धा, समर्पण का महापर्व।",
                            significance = "गुरु साक्षात परब्रह्म हैं। गुरु पूजन से जीवन में अज्ञान का अंधकार मिटता है।",
                            pujaVidhi = "सद्गुरु के चरण वंदन, पाद्य अर्घ्य, गुरु गीता पाठ और गुरु दक्षिणा अर्पण।",
                            timings = "प्रातः 07:00 से दोपहर 12:00",
                            backgroundStory = "चारों वेदों और 18 पुराणों के रचयिता महर्षि कृष्ण द्वैपायन वेदव्यास का जन्मोत्सव।"
                        )
                    )
                }
            }

            "श्रावण" -> {
                if (isShukla && tithiNumber == 5) {
                    festivals.add(
                        FestivalItem(
                            id = "nag_panchami",
                            nameHindi = "नाग पंचमी",
                            dateFormatted = "${date.dayOfMonth} श्रावण",
                            associatedDeity = "नाग देवता एवं भगवान शिव",
                            shortDesc = "द्वादश नाग देवताओं का पूजन, कालसर्प दोष निवारण एवं गौ दुग्ध अर्पण।",
                            significance = "नाग पूजन से सर्प भय दूर होता है और परिवार की रक्षा होती है।",
                            pujaVidhi = "दीवार पर गेरू और कोयले से नाग चित्र बनाकर कच्चा दूध, दूर्वा, लावा अर्पित करें।",
                            timings = "पूजा मुहूर्त: प्रातः 06:05 से 08:40",
                            backgroundStory = "समुद्र मंथन में वासुकि नाग का मथानी बनना एवं तक्षक प्रसंग।"
                        )
                    )
                    deityOverride = DeityInfo.SHIVA
                }
                if (isShukla && tithiNumber == 15) {
                    festivals.add(
                        FestivalItem(
                            id = "raksha_bandhan",
                            nameHindi = "रक्षाबंधन (श्रावणी पूर्णिमा)",
                            dateFormatted = "${date.dayOfMonth} श्रावण",
                            associatedDeity = "भगवान श्री कृष्ण एवं द्रौपदी",
                            shortDesc = "भाई-बहन के अटूट रक्षा सूत्र का पावन पर्व एवं वैदिक उपाकर्म / रक्षा पोटली पूजन।",
                            significance = "रक्षा सूत्र जीवन के समस्त संकटों से रक्षा करने का दिव्य कवच है।",
                            pujaVidhi = "भद्रा रहित काल में भाई की कलाई पर राखी बांधना, आरती और मिष्ठान खिलाना।",
                            timings = "राखी बांधने का शुभ समय: दोपहर 01:30 से सायं 07:45",
                            backgroundStory = "द्रौपदी द्वारा भगवान श्री कृष्ण की उंगली पर चीर बांधना और भगवान द्वारा रक्षा का वचन।"
                        )
                    )
                }
            }

            "भाद्रपद" -> {
                if (isKrishna && tithiNumber == 23) { // 8th of Krishna
                    festivals.add(
                        FestivalItem(
                            id = "janmashtami",
                            nameHindi = "श्री कृष्ण जन्माष्टमी",
                            dateFormatted = "${date.dayOfMonth} भाद्रपद",
                            associatedDeity = "भगवान श्री कृष्ण",
                            shortDesc = "रोहिणी नक्षत्र में मथुरा के कारागार में पूर्णब्रह्म श्री कृष्ण का पावन अवतरण।",
                            significance = "समस्त पापों का हरण करने वाला सनातन धर्म का अत्यंत उल्लासमय जन्मोत्सव।",
                            pujaVidhi = "लड्डू गोपाल को खीरे से जन्म, पंचामृत अभिषेक, झूला झुलाना, माखन-मिश्री भोग।",
                            timings = "निशीथ काल जन्मोत्सव: रात्रि 11:58 से 12:45 तक",
                            backgroundStory = "भाद्रपद कृष्ण अष्टमी की आधी रात को कंस के कारागार में श्री कृष्ण का प्राकट्य।"
                        )
                    )
                    deityOverride = DeityInfo.KRISHNA
                }
                if (isShukla && tithiNumber == 4) {
                    festivals.add(
                        FestivalItem(
                            id = "ganesh_chaturthi",
                            nameHindi = "गणेश चतुर्थी (गणेशोत्सव प्रारंभ)",
                            dateFormatted = "${date.dayOfMonth} भाद्रपद",
                            associatedDeity = "भगवान श्री गणेश",
                            shortDesc = "विघ्नहर्ता गणपति बप्पा की घर-घर प्रतिष्ठा, दस दिवसीय गणेशोत्सव का शुभारंभ।",
                            significance = "भगवान गणेश की स्थापना से घर में सुख, शांति, बुद्धि और रिद्धि-सिद्धि का वास होता है।",
                            pujaVidhi = "मध्याह्न काल में गणेश जी की मृण्मयी प्रतिमा स्थापित कर दूर्वा, मोदक, लाल चंदन अर्पित करें।",
                            timings = "मध्याह्न गणेश स्थापना: 11:15 AM से 01:40 PM",
                            backgroundStory = "माता पार्वती द्वारा अपने उबटन से बालक गणेश का निर्माण और शिव द्वारा गजमुख प्रदान करना।"
                        )
                    )
                    deityOverride = DeityInfo.GANESHA
                }
                if (isShukla && tithiNumber == 14) {
                    festivals.add(
                        FestivalItem(
                            id = "anant_chaturdashi",
                            nameHindi = "अनंत चतुर्दशी (गणेश विसर्जन)",
                            dateFormatted = "${date.dayOfMonth} भाद्रपद",
                            associatedDeity = "भगवान अनंत (नारायण) एवं श्री गणेश",
                            shortDesc = "चौदह गांठों वाले अनंत सूत्र का धारण, श्री गणेश विसर्जन एवं अनंत फल प्राप्ति।",
                            significance = "अनंत भगवान का व्रत जीवन के समस्त बंधनों और कष्टों से मुक्ति दिलाता है।",
                            pujaVidhi = "अनंत सूत्र की पूजा कर पुरुष दाहिने हाथ और स्त्रियां बाएं हाथ में बांधें। गणपति विसर्जन।",
                            timings = "पूजन समय: प्रातः 06:12 से 11:45 तक",
                            backgroundStory = "महाभारत काल में भगवान श्री कृष्ण द्वारा पांडवों को अनंत व्रत का उपदेश।"
                        )
                    )
                    deityOverride = DeityInfo.VISHNU
                }
            }
        }

        // Solar calendar fixed festival: Makar Sankranti (Jan 14 / 15)
        if (date.monthValue == 1 && (date.dayOfMonth == 14 || date.dayOfMonth == 15)) {
            festivals.add(
                FestivalItem(
                    id = "makar_sankranti",
                    nameHindi = "मकर संक्रांति (पोंगल / उत्तरायण)",
                    dateFormatted = "${date.dayOfMonth} जनवरी",
                    associatedDeity = "भगवान सूर्य नारायण",
                    shortDesc = "सूर्य देव का धनु से मकर राशि में प्रवेश, उत्तरायण का शुभारंभ, तिल-गुड़ दान और पवित्र स्नान।",
                    significance = "सूर्य देव अपने पुत्र शनिदेव के घर जाते हैं। इस दिन गंगा स्नान और खिचड़ी दान का महापुण्य है।",
                    pujaVidhi = "प्रातः सूर्य को अर्घ्य, तिल, गुड़, कंबल और खिचड़ी का दान।",
                    timings = "पुण्यकाल मुहूर्त: प्रातः 07:15 से दोपहर 12:45 तक",
                    backgroundStory = "भीष्म पितामह द्वारा उत्तरायण सूर्य की प्रतीक्षा कर प्राण त्यागने का प्रसंग।"
                )
            )
            deityOverride = DeityInfo.SURYA
        }

        return EventResult(vrats, festivals, deityOverride)
    }

    private fun getEkadashiName(hinduMonth: String, isShukla: Boolean): String {
        return when (hinduMonth) {
            "चैत्र" -> if (isShukla) "कामदा" else "पापमोचिनी"
            "वैशाख" -> if (isShukla) "मोहिनी" else "वरुथिनी"
            "ज्येष्ठ" -> if (isShukla) "निर्जला" else "अपरा"
            "आषाढ़" -> if (isShukla) "देवशयनी" else "योगिनी"
            "श्रावण" -> if (isShukla) "श्रावण पुत्रदा" else "कामिका"
            "भाद्रपद" -> if (isShukla) "परिवर्तिनी" else "अजा"
            "आश्विन" -> if (isShukla) "पापांकुशा" else "इन्दिरा"
            "कार्तिक" -> if (isShukla) "देवउठनी (प्रबोधिनी)" else "रमा"
            "मार्गशीर्ष" -> if (isShukla) "मोक्षदा" else "उत्पन्ना"
            "पौष" -> if (isShukla) "पौष पुत्रदा" else "सफला"
            "माघ" -> if (isShukla) "जया" else "षटतिला"
            "फाल्गुन" -> if (isShukla) "आमलकी" else "विजया"
            else -> "एकादशी"
        }
    }
}
