package com.example.model

data class CommentaryItem(
    val ballSequence: String, // e.g. "4.2"
    val textEn: String,
    val textUr: String,
    val isWicket: Boolean = false,
    val isBoundary: Boolean = false,
    val isSix: Boolean = false
)

data class FallOfWicket(
    val score: Int,
    val wicketNumber: Int,
    val playerOutName: String,
    val overDecimal: Double
)

data class Partnership(
    val batsman1Name: String,
    var batsman1Runs: Int,
    val batsman2Name: String,
    var batsman2Runs: Int
) {
    val totalRuns: Int
        get() = batsman1Runs + batsman2Runs
}

data class WagonWheelShot(
    val angleDegrees: Float,
    val distanceMultiplier: Float, // 0.0 to 1.1 (boundary is 1.0)
    val runs: Int,
    val isAerial: Boolean
)

data class BallDeliveryOutcome(
    val overNumber: Int,
    val ballInOver: Int,
    val deliveryType: DeliveryType,
    val speedKmh: Int,
    val runsScored: Int,
    val isExtra: Boolean,
    val extraType: String? = null, // "WD", "NB", "B", "LB"
    val isWicket: Boolean,
    val dismissalType: DismissalType = DismissalType.NONE,
    val batsmanOnStrike: Player,
    val bowler: Player,
    val shotPlayed: ShotType?,
    val timingResult: TimingResult,
    val contactQuality: ContactQuality,
    val shotAngleDegrees: Float,
    val isBoundaryFour: Boolean = (runsScored == 4 && !isExtra),
    val isBoundarySix: Boolean = (runsScored == 6 && !isExtra)
)

data class DRSState(
    var isActive: Boolean = false,
    var reviewingTeam: Team? = null,
    var batsman: Player? = null,
    var bowler: Player? = null,
    var originalDecision: DismissalType = DismissalType.NONE,
    var snickoContactDetected: Boolean = false,
    var pitchingInLine: Boolean = true,
    var impactInLine: Boolean = true,
    var hittingWickets: Boolean = true,
    var finalVerdict: DismissalType = DismissalType.NONE,
    var isReviewSuccessful: Boolean = false,
    var stageStep: Int = 0 // 0: Snicko, 1: Pitching, 2: Impact, 3: Wicket Target, 4: Verdict
)

data class InningsState(
    val inningsNumber: Int,
    val battingTeam: Team,
    val bowlingTeam: Team,
    var totalRuns: Int = 0,
    var wicketsLost: Int = 0,
    var legalBallsBowled: Int = 0,
    var maxOvers: Int,
    var extrasWides: Int = 0,
    var extrasNoBalls: Int = 0,
    var extrasLegByes: Int = 0,
    var extrasByes: Int = 0,
    var strikerIndex: Int = 0,
    var nonStrikerIndex: Int = 1,
    var currentBowlerIndex: Int = 10,
    var currentOverDeliveries: MutableList<BallDeliveryOutcome> = mutableListOf(),
    var allDeliveries: MutableList<BallDeliveryOutcome> = mutableListOf(),
    var fallOfWickets: MutableList<FallOfWicket> = mutableListOf(),
    var wagonWheelShots: MutableList<WagonWheelShot> = mutableListOf(),
    var targetScore: Int? = null,
    var isDeclaredOrCompleted: Boolean = false
) {
    val currentOversFormatted: String
        get() {
            val overs = legalBallsBowled / 6
            val balls = legalBallsBowled % 6
            return "$overs.$balls"
        }

    val currentRunRate: Double
        get() = if (legalBallsBowled > 0) (totalRuns * 6.0) / legalBallsBowled else 0.0

    val requiredRunRate: Double?
        get() {
            val target = targetScore ?: return null
            val runsNeeded = target - totalRuns
            val ballsRemaining = (maxOvers * 6) - legalBallsBowled
            return if (ballsRemaining > 0) (runsNeeded * 6.0) / ballsRemaining else 0.0
        }

    val striker: Player
        get() = battingTeam.getPlayingXI()[strikerIndex.coerceIn(0, battingTeam.getPlayingXI().lastIndex)]

    val nonStriker: Player
        get() = battingTeam.getPlayingXI()[nonStrikerIndex.coerceIn(0, battingTeam.getPlayingXI().lastIndex)]

    val currentBowler: Player
        get() = bowlingTeam.getPlayingXI()[currentBowlerIndex.coerceIn(0, bowlingTeam.getPlayingXI().lastIndex)]
}

data class MatchState(
    val matchId: String,
    val format: CricketFormat,
    val maxOvers: Int,
    val difficulty: MatchDifficulty,
    val venue: StadiumVenue,
    val pitch: PitchType,
    val weather: WeatherCondition,
    val isDayNight: Boolean,
    val teamA: Team,
    val teamB: Team,
    var tossWinnerTeam: Team,
    var electedToBat: Boolean,
    var inningsList: MutableList<InningsState> = mutableListOf(),
    var currentInningsIndex: Int = 0,
    var isFreeHitActive: Boolean = false,
    var reviewsRemainingTeamA: Int = 2,
    var reviewsRemainingTeamB: Int = 2,
    var activeDRS: DRSState = DRSState(),
    var currentFieldPreset: FieldPreset = FieldPreset.ATTACKING,
    var isMatchFinished: Boolean = false,
    var matchResultSummaryEn: String = "",
    var matchResultSummaryUr: String = "",
    var manOfTheMatch: Player? = null,
    var commentary: MutableList<CommentaryItem> = mutableListOf()
) {
    val currentInnings: InningsState
        get() = inningsList[currentInningsIndex]

    val battingTeam: Team
        get() = currentInnings.battingTeam

    val bowlingTeam: Team
        get() = currentInnings.bowlingTeam
}
