package com.example.model

enum class PlayerRole(val labelEn: String, val labelUr: String) {
    TOP_ORDER_BAT("Top Order Batter", "اوپنر / ٹاپ آرڈر بلے باز"),
    MIDDLE_ORDER_BAT("Middle Order Batter", "مڈل آرڈر بلے باز"),
    WICKETKEEPER_BAT("Wicketkeeper-Batter", "وکٹ کیپر بلے باز"),
    BATTING_ALL_ROUNDER("Batting All-Rounder", "بیٹنگ آل راؤنڈر"),
    BOWLING_ALL_ROUNDER("Bowling All-Rounder", "بولنگ آل راؤنڈر"),
    FAST_BOWLER("Pace Bowler", "تیز گیند باز"),
    SPIN_BOWLER("Spin Wizard", "اسپن گیند باز")
}

enum class BattingHand(val labelEn: String, val labelUr: String) {
    RIGHT_HAND("Right-Handed", "دائیں ہاتھ کے بلے باز"),
    LEFT_HAND("Left-Handed", "بائیں ہاتھ کے بلے باز")
}

data class PlayerRating(
    val timing: Int = 80,         // 50-99
    val power: Int = 80,          // 50-99
    val bowlingPaceSpin: Int = 75,// 50-99
    val bowlingControl: Int = 75, // 50-99
    val fielding: Int = 80,       // 50-99
    val stamina: Int = 85         // 50-99
) {
    val overall: Int
        get() = (timing + power + bowlingPaceSpin + bowlingControl + fielding + stamina) / 6
}

data class PlayerMatchStats(
    var runsScored: Int = 0,
    var ballsFaced: Int = 0,
    var fours: Int = 0,
    var sixes: Int = 0,
    var isOut: Boolean = false,
    var dismissalDescription: String = "not out",
    var oversBowled: Double = 0.0,
    var maidens: Int = 0,
    var runsConceded: Int = 0,
    var wicketsTaken: Int = 0,
    var catches: Int = 0,
    var stumpings: Int = 0
) {
    val strikeRate: Double
        get() = if (ballsFaced > 0) (runsScored * 100.0) / ballsFaced else 0.0

    val economyRate: Double
        get() = if (oversBowled > 0.0) {
            val totalBalls = (oversBowled.toInt() * 6) + ((oversBowled * 10).toInt() % 10)
            if (totalBalls > 0) (runsConceded * 6.0) / totalBalls else 0.0
        } else 0.0
}

data class Player(
    val id: String,
    val name: String,
    val role: PlayerRole,
    val battingHand: BattingHand,
    val bowlingArchetype: BowlingArchetype,
    val ratings: PlayerRating,
    val jerseyNumber: Int,
    val facePresetIndex: Int = 0,
    val hairStyleIndex: Int = 0,
    val beardStyleIndex: Int = 0,
    val skinToneIndex: Int = 1,
    var currentMatchStats: PlayerMatchStats = PlayerMatchStats()
)
