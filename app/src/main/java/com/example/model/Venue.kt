package com.example.model

data class StadiumVenue(
    val id: String,
    val nameEn: String,
    val nameUr: String,
    val cityEn: String,
    val cityUr: String,
    val boundaryDistanceMeters: Int,
    val capacity: Int,
    val defaultPitch: PitchType,
    val isFloodlit: Boolean,
    val descriptionEn: String,
    val descriptionUr: String
)

object DefaultVenues {
    fun getVenues(): List<StadiumVenue> {
        return listOf(
            StadiumVenue(
                id = "venue_lahore",
                nameEn = "Qaddafi Arena Lahore",
                nameUr = "قذافی ایرینا لاہور",
                cityEn = "Lahore, Pakistan",
                cityUr = "لاہور، پاکستان",
                boundaryDistanceMeters = 72,
                capacity = 35000,
                defaultPitch = PitchType.FLAT_BATTING,
                isFloodlit = true,
                descriptionEn = "Historic venue with electric festive atmosphere and batting-friendly track.",
                descriptionUr = "پرجوش شائقین اور بلے بازوں کی جنت، تاریخی میدان۔"
            ),
            StadiumVenue(
                id = "venue_melbourne",
                nameEn = "Melbourne Cricket Dome",
                nameUr = "میلبورن کرکٹ ڈوم",
                cityEn = "Melbourne, Australia",
                cityUr = "میلبورن، آسٹریلیا",
                boundaryDistanceMeters = 84,
                capacity = 100000,
                defaultPitch = PitchType.BOUNCY,
                isFloodlit = true,
                descriptionEn = "Colossal boundaries and true bounce. Fast bowlers relish the extra carry.",
                descriptionUr = "بڑی باؤنڈریز اور تیز اچھال والا دنیا کا سب سے بڑا کرکٹ کا میدان۔"
            ),
            StadiumVenue(
                id = "venue_london",
                nameEn = "Crown Ground London",
                nameUr = "کراؤن گراؤنڈ لندن",
                cityEn = "London, England",
                cityUr = "لندن، انگلینڈ",
                boundaryDistanceMeters = 68,
                capacity = 31000,
                defaultPitch = PitchType.GREEN_SEAM,
                isFloodlit = true,
                descriptionEn = "The traditional home of cricket. Significant swing and seam movement under cloudy skies.",
                descriptionUr = "کرکٹ کا روایتی گہوارہ۔ بادلوں میں تیز سونگ اور سیم۔"
            ),
            StadiumVenue(
                id = "venue_dubai",
                nameEn = "Dubai Desert International",
                nameUr = "دبئی ڈیزرٹ انٹرنیشنل",
                cityEn = "Dubai, UAE",
                cityUr = "دبئی، متحدہ عرب امارات",
                boundaryDistanceMeters = 74,
                capacity = 25000,
                defaultPitch = PitchType.DRY_DUSTY,
                isFloodlit = true,
                descriptionEn = "Ring of fire floodlights, abrasive pitch that aids spin, with dew later in night.",
                descriptionUr = "رنگ آف فائر لائٹس اور اسپنرز کے لیے کارآمد خشک پچ۔"
            ),
            StadiumVenue(
                id = "venue_kolkata",
                nameEn = "Eden Garden Park",
                nameUr = "ایڈن گارڈن پارک",
                cityEn = "Kolkata, India",
                cityUr = "کولکتہ، بھارت",
                boundaryDistanceMeters = 70,
                capacity = 68000,
                defaultPitch = PitchType.SLOW,
                isFloodlit = true,
                descriptionEn = "A cauldron of noise. Pitch starts true before slowing down for spinners.",
                descriptionUr = "پرشور تماشائی اور سست رفتار اسپننگ پچ۔"
            ),
            StadiumVenue(
                id = "venue_caribbean",
                nameEn = "Kensington Oval",
                nameUr = "کینزنگٹن اوول",
                cityEn = "Bridgetown, Barbados",
                cityUr = "بارباڈوس",
                boundaryDistanceMeters = 67,
                capacity = 28000,
                defaultPitch = PitchType.BOUNCY,
                isFloodlit = true,
                descriptionEn = "Caribbean breeze, hard surface that rewards pull and hook shots.",
                descriptionUr = "کیریبین ہوائیں اور سخت پچ جہاں پل اور ہک شاٹس کا مزہ آتا ہے۔"
            )
        )
    }
}
