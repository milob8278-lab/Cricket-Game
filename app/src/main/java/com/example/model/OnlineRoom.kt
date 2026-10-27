package com.example.model

enum class RoomState(val labelEn: String, val labelUr: String) {
    LOBBY("Waiting in Lobby", "لابی میں انتظار"),
    READY("Players Ready", "کھلاڑی تیار ہیں"),
    TOSS("Toss in Progress", "ٹاس جاری ہے"),
    IN_MATCH("Match in Progress", "میچ جاری ہے"),
    INNINGS_BREAK("Innings Break", "اننگز کا وقفہ"),
    FINISHED("Match Finished", "میچ ختم ہو گیا")
}

data class OnlineUserProfile(
    val id: String = "usr_player",
    val displayName: String = "ProCricketer",
    val countryCode: String = "PK",
    val ratingElo: Int = 1250,
    val tier: String = "Gold League",
    val matchesPlayed: Int = 24,
    val matchesWon: Int = 18,
    val matchesLost: Int = 6,
    val winRatePercent: Int = 75
)

data class OnlineDeliveryIntent(
    val deliveryType: DeliveryType = DeliveryType.SEAM_UP,
    val speedKmh: Int = 142,
    val targetLineOffset: Float = 0.05f, // -1.0 (far leg) to +1.0 (far off)
    val targetLengthDistance: Float = 0.45f, // 0.1 (yorker) to 0.85 (bouncer)
    val swingAmount: Float = 0.08f,
    val spinAmount: Float = 0.0f
)

data class OnlineBattingIntent(
    val shotType: ShotType = ShotType.COVER_DRIVE,
    val footwork: Footwork = Footwork.FRONT_FOOT,
    val intent: BattingIntent = BattingIntent.NORMAL,
    val timingDeltaMs: Long = 0L,
    val customAngleDegrees: Float? = null
)

data class QuickEmote(
    val id: String,
    val textEn: String,
    val textUr: String,
    val emoji: String,
    val senderName: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class OnlineRoom(
    val roomCode: String,
    val hostUser: OnlineUserProfile,
    var guestUser: OnlineUserProfile? = null,
    var format: CricketFormat = CricketFormat.T20,
    var overs: Int = 5,
    var venue: StadiumVenue,
    var hostTeam: Team,
    var guestTeam: Team,
    var state: RoomState = RoomState.LOBBY,
    var isHostReady: Boolean = false,
    var isGuestReady: Boolean = false,
    var tossWinnerId: String? = null,
    var tossDecision: String? = null, // "BAT" or "BOWL"
    var currentBowlerPlayerId: String? = null,
    var currentBatterPlayerId: String? = null,
    var pendingDelivery: OnlineDeliveryIntent? = null,
    var pendingBatting: OnlineBattingIntent? = null,
    var lastOutcomeText: String? = null,
    var pingMs: Int = 38,
    val activeEmotes: MutableList<QuickEmote> = mutableListOf()
)
