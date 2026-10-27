package com.example.model

data class CareerMilestone(
    val titleEn: String,
    val titleUr: String,
    val descriptionEn: String,
    val descriptionUr: String,
    val isAchieved: Boolean = false,
    val rewardXp: Int = 100
)

data class CareerProfile(
    var playerName: String = "Zayn Malik",
    var playerRole: PlayerRole = PlayerRole.TOP_ORDER_BAT,
    var battingHand: BattingHand = BattingHand.RIGHT_HAND,
    var bowlingArchetype: BowlingArchetype = BowlingArchetype.FAST,
    var level: Int = 1,
    var currentXp: Int = 150,
    var xpToNextLevel: Int = 500,
    var availableSkillPoints: Int = 5,
    var timingRating: Int = 75,
    var powerRating: Int = 78,
    var controlRating: Int = 72,
    var paceSpinRating: Int = 70,
    var staminaRating: Int = 80,
    var matchesPlayed: Int = 8,
    var totalRuns: Int = 342,
    var highestScore: Int = 89,
    var fifties: Int = 3,
    var centuries: Int = 0,
    var totalWickets: Int = 11,
    var bestBowling: String = "3/21",
    var clubTeamName: String = "Lahore Lions",
    var milestones: List<CareerMilestone> = listOf(
        CareerMilestone("Debut Match", "پہلا میچ", "Play your first professional match", "اپنا پہلا پروفیشنل میچ کھیلیں", true),
        CareerMilestone("First Boundary", "پہلا چوکا", "Smash your first four in a match", "میچ میں اپنا پہلا چوکا لگائیں", true),
        CareerMilestone("Half Century", "نصف سنچری", "Score 50 runs in an innings", "ایک اننگز میں 50 رنز بنائیں", true),
        CareerMilestone("Century Club", "سنچری کلب", "Reach 100 runs in a single innings", "ایک ہی اننگز میں 100 رنز مکمل کریں", false),
        CareerMilestone("Hat-Trick Hero", "ہیٹ ٹرک ہیرو", "Take 3 wickets in consecutive balls", "مسلسل 3 گیندوں پر 3 وکٹیں حاصل کریں", false),
        CareerMilestone("Player of the Match", "میچ کا بہترین کھلاڑی", "Win the MVP award in a match", "میچ میں مین آف دی میچ ایوارڈ جیتیں", true)
    )
)
