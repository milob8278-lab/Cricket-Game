package com.example.model

enum class CricketFormat(val displayNameEn: String, val displayNameUr: String, val defaultOvers: Int) {
    T5("T5 (5 Overs)", "ٹی 5 (5 اوورز)", 5),
    T10("T10 (10 Overs)", "ٹی 10 (10 اوورز)", 10),
    T20("T20 (20 Overs)", "ٹی 20 (20 اوورز)", 20),
    ODI("ODI (50 Overs)", "ایک روزہ (50 اوورز)", 50),
    TEST("Test Match (2 Innings)", "ٹیسٹ میچ (2 اننگز)", 15),
    THE_HUNDRED("The Hundred (100 Balls)", "دی ہنڈرڈ (100 گیندیں)", 20),
    CUSTOM("Custom Overs", "کسٹم اوورز", 5)
}

enum class MatchDifficulty(val displayNameEn: String, val displayNameUr: String) {
    EASY("Easy / Rookie", "آسان / ابتدائی"),
    MEDIUM("Medium / Club", "درمیانہ / کلب"),
    HARD("Hard / Pro", "مشکل / پیشہ ور"),
    EXPERT("Expert / Legend", "ماہر / لیجنڈ")
}

enum class PitchType(
    val displayNameEn: String,
    val displayNameUr: String,
    val bounce: Float,
    val pace: Float,
    val seam: Float,
    val spin: Float
) {
    GREEN_SEAM("Green / Seaming", "سبز / سیمنگ پچ", 1.15f, 1.2f, 1.35f, 0.7f),
    DRY_DUSTY("Dry / Dusty Spin", "خشک / اسپن پچ", 0.9f, 0.85f, 0.8f, 1.45f),
    FLAT_BATTING("Flat / Road", "فلیٹ / بیٹنگ پیراڈائز", 1.0f, 1.0f, 0.75f, 0.8f),
    SLOW("Slow & Low", "سست اور نیچی", 0.75f, 0.8f, 0.85f, 1.2f),
    BOUNCY("Hard & Bouncy", "سخت اور اچھال والی", 1.35f, 1.3f, 1.1f, 0.9f),
    CRACKED("Cracked / Day 5", "دراڑ دار / پانچواں دن", 1.1f, 0.95f, 1.25f, 1.4f),
    WET("Wet / Post Rain", "گیلی / بارش کے بعد", 0.8f, 0.75f, 1.15f, 1.0f)
}

enum class WeatherCondition(val displayNameEn: String, val displayNameUr: String) {
    SUNNY("Sunny & Clear", "روشن و صاف"),
    OVERCAST("Overcast / Cloud Cover", "ابر آلود"),
    RAIN_DRIZZLE("Rain / Light Drizzle", "ہلکی بارش"),
    DEW_NIGHT("Evening Dew", "شام کی شبنم"),
    WINDY("Windy Breezes", "تیز ہوائیں")
}

enum class GraphicsProfile(val displayNameEn: String, val displayNameUr: String, val targetFps: Int) {
    HIGH_3D("High 3D (Full Visuals)", "ہائی تھری ڈی (مکمل گرافکس)", 60),
    BALANCED_3D("Balanced 3D (Standard)", "متوازن تھری ڈی (معیاری)", 30),
    LITE_3D("3D Lite / 2.5D (4GB RAM)", "تھری ڈی لائٹ (کم ریم کے لیے)", 30)
}

enum class ControlScheme(val displayNameEn: String, val displayNameUr: String) {
    BUTTONS("Classic Buttons", "کلاسک بٹنز"),
    SWIPE("Gesture Swipe", "سوائپ اشارے"),
    JOYSTICK_BUTTONS("Joystick + Buttons", "جوائے اسٹک اور بٹن")
}

enum class BattingIntent(val labelEn: String, val labelUr: String, val powerMultiplier: Float, val riskMultiplier: Float) {
    DEFEND("Defend", "دفاع", 0.2f, 0.15f),
    NORMAL("Normal", "معمول", 1.0f, 0.45f),
    LOFT("Loft", "ہوا میں", 1.6f, 0.8f),
    POWER("Power Hit", "طاقتور شاٹ", 2.1f, 1.1f)
}

enum class Footwork(val labelEn: String, val labelUr: String) {
    FRONT_FOOT("Front Foot", "سامنے کا پیر"),
    BACK_FOOT("Back Foot", "پیچھے کا پیر"),
    ADVANCE("Advance Down", "آگے بڑھ کر"),
    STAY_DEEP("Deep in Crease", "کریز کی گہرائی")
}

enum class ShotType(
    val shotNameEn: String,
    val shotNameUr: String,
    val defaultAngleDegrees: Float,
    val isAerialDefault: Boolean
) {
    COVER_DRIVE("Cover Drive", "کور ڈرائیو", 135f, false),
    OFF_DRIVE("Off Drive", "آف ڈرائیو", 160f, false),
    STRAIGHT_DRIVE("Straight Drive", "اسٹریٹ ڈرائیو", 180f, false),
    ON_DRIVE("On Drive", "آن ڈرائیو", 200f, false),
    SQUARE_CUT("Square Cut", "اسکوائر کٹ", 90f, false),
    LATE_CUT("Late Cut", "لیٹ کٹ", 60f, false),
    BACK_FOOT_PUNCH("Back Foot Punch", "بیک فٹ پنچ", 120f, false),
    FORWARD_DEFENCE("Forward Block", "فارورڈ بلاک", 175f, false),
    BACK_FOOT_DEFENCE("Back Foot Block", "بیک فٹ بلاک", 180f, false),
    LEAVE("Leave Delivery", "گیند چھوڑ دی", 0f, false),
    FLICK("Flick to Midwicket", "فلک شاٹ", 235f, false),
    LEG_GLANCE("Leg Glance", "لیگ گلانس", 290f, false),
    PULL_SHOT("Pull Shot", "پل شاٹ", 250f, true),
    HOOK_SHOT("Hook Shot", "ہک شاٹ", 270f, true),
    SWEEP("Paddle Sweep", "سویپ شاٹ", 280f, false),
    SLOG_SWEEP("Slog Sweep", "سلاگ سویپ", 240f, true),
    REVERSE_SWEEP("Reverse Sweep", "ریورس سویپ", 75f, true),
    SCOOP("Dilscoop", "اسکوپ شاٹ", 345f, true),
    RAMP_SHOT("Ramp Shot", "ریمپ شاٹ", 355f, true),
    UPPER_CUT("Upper Cut", "اپر کٹ", 80f, true),
    SWITCH_HIT("Switch Hit", "سوئچ ہٹ", 110f, true),
    HELICOPTER_SHOT("Helicopter Shot", "ہیلی کاپٹر شاٹ", 215f, true)
}

enum class BowlingArchetype(val displayNameEn: String, val displayNameUr: String) {
    FAST("Fast Pace (140-155 km/h)", "تیز رفتار (140-155 کلومیٹر)"),
    MEDIUM("Medium Pace (120-135 km/h)", "درمیانی رفتار (120-135 کلومیٹر)"),
    OFF_SPIN("Off Spin (Right-Arm)", "آف اسپن (دائیں ہاتھ)"),
    LEG_SPIN("Leg Spin (Wrist Spin)", "لیگ اسپن (ریسٹ اسپن)"),
    LEFT_ARM_SPIN("Left-Arm Orthodox", "بائیں ہاتھ کی اسپن"),
    LEFT_ARM_PACE("Left-Arm Fast Pace", "بائیں ہاتھ کی تیز رفتار")
}

enum class DeliveryType(val labelEn: String, val labelUr: String, val defaultSpeedKmh: Int) {
    OUTSWINGER("Outswinger", "آؤٹ سونگ", 142),
    INSWINGER("Inswinger", "ان سونگ", 143),
    YORKER("Toe Crusher Yorker", "یارکر", 146),
    BOUNCER("Fast Bouncer", "باؤنسر", 144),
    SLOWER_BALL("Slower Deception", "سست گیند", 118),
    CUTTER("Off/Leg Cutter", "کٹر گیند", 128),
    SEAM_UP("Seam-Up Delivery", "سیم اپ", 138),
    KNUCKLE_BALL("Knuckle Ball", "نکل بال", 120),
    OFF_BREAK("Classic Off Break", "آف بریک", 92),
    DOOSRA("The Doosra", "دوسرا", 90),
    CARROM_BALL("Carrom Ball", "کیرم بال", 94),
    ARM_BALL("Arm Ball (Straighter)", "آرم بال", 98),
    LEG_BREAK("Sharp Leg Break", "لیگ بریک", 88),
    GOOGLY("Googly (Wrong'un)", "گوگلی", 86),
    FLIPPER("Skiddy Flipper", "فلپر", 96),
    TOP_SPINNER("Top Spinner", "ٹاپ اسپنر", 92)
}

enum class DismissalType(val labelEn: String, val labelUr: String) {
    BOWLED("Bowled Clean!", "بولڈ آؤٹ!"),
    CAUGHT("Caught Out!", "کیچ آؤٹ!"),
    CAUGHT_BEHIND("Caught Behind!", "کیپر نے کیچ پکڑا!"),
    LBW("LBW (Leg Before)", "ایل بی ڈبلیو!"),
    RUN_OUT("Run Out!", "رن آؤٹ!"),
    STUMPED("Stumped Out!", "اسٹمپ آؤٹ!"),
    HIT_WICKET("Hit Wicket!", "ہٹ وکٹ!"),
    OBSTRUCTING_FIELD("Obstructing Field", "مداخلت پر آؤٹ"),
    NONE("Not Out", "ناٹ آؤٹ")
}

enum class TimingResult(val feedbackEn: String, val feedbackUr: String) {
    PERFECT("PERFECT TIMING!", "شاندار ٹائمنگ!"),
    GOOD("GOOD TIMING", "اچھی ٹائمنگ"),
    EARLY("EARLY SHOT", "جلد شاٹ کھیلا"),
    LATE("LATE ON THE SHOT", "دیر سے شاٹ کھیلا"),
    MISSED("BEATEN OUTSIDE", "گیند بیٹ ہو گئی")
}

enum class ContactQuality {
    SWEET_SPOT,
    MIDDLE,
    INSIDE_EDGE,
    OUTSIDE_EDGE,
    LEADING_EDGE,
    TOE_END,
    PAD_CONTACT,
    CLEAN_MISS
}

enum class Language(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    URDU("ur", "اردو")
}

enum class GameScreen {
    HOME,
    MATCH_SETUP,
    TOSS,
    LIVE_MATCH,
    MATCH_RESULT,
    TOURNAMENT,
    CAREER,
    PRACTICE,
    CHALLENGES,
    MULTIPLAYER,
    TEAMS_ROSTER,
    STADIUMS,
    LAYOUT_EDITOR,
    SETTINGS,
    ADMIN_CONSOLE
}

enum class FieldPreset(val labelEn: String, val labelUr: String) {
    ATTACKING("Attacking (Slips & Infield)", "جارحانہ فیلڈنگ"),
    DEFENSIVE("Defensive (Boundary Ropes)", "دفاعی فیلڈنگ"),
    POWERPLAY("Powerplay (2 Men Outside)", "پاور پلے فیلڈ"),
    PACE_ATTACK("Pace Attack (Catching)", "تیز گیند بازوں کے لیے"),
    SPIN_ATTACK("Spin Trap (Close Catchers)", "اسپن جال فیلڈ"),
    DEATH_OVERS("Death Overs (Yorker Field)", "آخری اوورز کا تحفظ")
}
