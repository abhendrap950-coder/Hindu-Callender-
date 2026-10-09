package com.example.panchang.model

data class CityLocation(
    val id: String,
    val nameHindi: String,
    val nameEnglish: String,
    val stateHindi: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneOffsetHours: Double = 5.5
) {
    val displayLabel: String
        get() = "$nameHindi, $stateHindi"

    companion object {
        val DEFAULT = CityLocation(
            id = "prayagraj",
            nameHindi = "प्रयागराज (इलाहाबाद)",
            nameEnglish = "Prayagraj",
            stateHindi = "उत्तर प्रदेश",
            latitude = 25.4358,
            longitude = 81.8463,
            timezoneOffsetHours = 5.5
        )

        val MAJOR_CITIES = listOf(
            DEFAULT,
            CityLocation("varanasi", "वाराणसी (काशी)", "Varanasi", "उत्तर प्रदेश", 25.3176, 82.9739),
            CityLocation("ayodhya", "अयोध्या धाम", "Ayodhya", "उत्तर प्रदेश", 26.7922, 82.1998),
            CityLocation("ujjain", "उज्जैन (महाकाल नगरी)", "Ujjain", "मध्य प्रदेश", 23.1765, 75.7885),
            CityLocation("mathura", "मथुरा (कृष्ण जन्मभूमि)", "Mathura", "उत्तर प्रदेश", 27.4924, 77.6737),
            CityLocation("haridwar", "हरिद्वार", "Haridwar", "उत्तराखंड", 29.9457, 78.1642),
            CityLocation("delhi", "नई दिल्ली", "New Delhi", "दिल्ली", 28.6139, 77.2090),
            CityLocation("mumbai", "मुंबई", "Mumbai", "महाराष्ट्र", 19.0760, 72.8777),
            CityLocation("jaipur", "जयपुर", "Jaipur", "राजस्थान", 26.9124, 75.7873),
            CityLocation("kolkata", "कोलकाता", "Kolkata", "पश्चिम बंगाल", 22.5726, 88.3639),
            CityLocation("lucknow", "लखनऊ", "Lucknow", "उत्तर प्रदेश", 26.8467, 80.9462),
            CityLocation("patna", "पटना", "Patna", "बिहार", 25.5941, 85.1376),
            CityLocation("bengaluru", "बेंगलुरु", "Bengaluru", "कर्नाटक", 12.9716, 77.5946),
            CityLocation("hyderabad", "हैदराबाद", "Hyderabad", "तेलंगाना", 17.3850, 78.4867),
            CityLocation("chennai", "चेन्नई", "Chennai", "तमिलनाडु", 13.0827, 80.2707),
            CityLocation("pune", "पुणे", "Pune", "महाराष्ट्र", 18.5204, 73.8567),
            CityLocation("ahmedabad", "अहमदाबाद", "Ahmedabad", "गुजरात", 23.0225, 72.5714),
            CityLocation("puri", "पुरी (जगन्नाथ धाम)", "Puri", "ओडिशा", 19.8135, 85.8312),
            CityLocation("dwarka", "द्वारका", "Dwarka", "गुजरात", 22.2442, 68.9685),
            CityLocation("tirupati", "तिरुपति", "Tirupati", "आंध्र प्रदेश", 13.6288, 79.4192),
            CityLocation("rishikesh", "ऋषिकेश", "Rishikesh", "उत्तराखंड", 30.0869, 78.2676),
            CityLocation("nashik", "नासिक (त्र्यंबकेश्वर)", "Nashik", "महाराष्ट्र", 19.9975, 73.7898),
            CityLocation("indore", "इंदौर", "Indore", "मध्य प्रदेश", 22.7196, 75.8577),
            CityLocation("bhopal", "भोपाल", "Bhopal", "मध्य प्रदेश", 23.2599, 77.4126),
            CityLocation("chandigarh", "चंडीगढ़", "Chandigarh", "पंजाब/हरियाणा", 30.7333, 76.7794),
            CityLocation("guwahati", "गुवाहाटी (कामाख्या)", "Guwahati", "असम", 26.1445, 91.7362),
            CityLocation("dehradun", "देहरादून", "Dehradun", "उत्तराखंड", 30.3165, 78.0322),
            CityLocation("gorakhpur", "गोरखपुर", "Gorakhpur", "उत्तर प्रदेश", 26.7606, 83.3732)
        )
    }
}
