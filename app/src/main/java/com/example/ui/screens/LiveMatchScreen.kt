package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.CricketAudioEngine
import com.example.data.CricketRepository
import com.example.model.*
import com.example.simulation.CricketPhysicsEngine
import com.example.ui.field.CameraAngle
import com.example.ui.field.CricketField3D
import com.example.ui.field.DRSVisualizer
import com.example.ui.hud.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun LiveMatchScreen(
    repository: CricketRepository,
    audio: CricketAudioEngine,
    language: Language,
    onMatchFinished: () -> Unit,
    onExitToHome: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val matchState by repository.currentMatch.collectAsState()
    val graphicsProfile by repository.currentGraphics.collectAsState()

    val match = matchState ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val innings = match.currentInnings
    val isUserBatting = innings.battingTeam.id == match.teamA.id

    // Live Camera Angle
    var currentCamera by remember { mutableStateOf(CameraAngle.BROADCAST) }

    // Batting State
    var selectedFootwork by remember { mutableStateOf(Footwork.FRONT_FOOT) }
    var selectedIntent by remember { mutableStateOf(BattingIntent.NORMAL) }
    var selectedShot by remember { mutableStateOf(ShotType.COVER_DRIVE) }

    // Bowling State
    var selectedDelivery by remember { mutableStateOf(DeliveryType.OUTSWINGER) }
    var targetLineOffset by remember { mutableFloatStateOf(0.15f) }
    var targetLengthDistance by remember { mutableFloatStateOf(0.35f) }
    var speedKmh by remember { mutableIntStateOf(140) }
    var swingSpinAmount by remember { mutableFloatStateOf(0.4f) }

    // Delivery & Animation Progress
    var isDelivering by remember { mutableStateOf(false) }
    var deliveryProgress by remember { mutableFloatStateOf(0f) }
    var ballExitAngle by remember { mutableFloatStateOf(135f) }
    var ballExitDistance by remember { mutableFloatStateOf(0f) }
    var lastTimingResult by remember { mutableStateOf<TimingResult?>(null) }
    var latestCommentaryEn by remember { mutableStateOf("Match underway at ${match.venue.nameEn}!") }
    var latestCommentaryUr by remember { mutableStateOf("میچ کا شاندار آغاز ${match.venue.nameUr} میں!") }

    // Overlays
    var showScorecard by remember { mutableStateOf(false) }
    var showPauseMenu by remember { mutableStateOf(false) }
    var showDRS by remember { mutableStateOf(false) }
    var pendingDRS by remember { mutableStateOf<DRSState?>(null) }

    // Animation progress driver
    val animatableProgress = remember { Animatable(0f) }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. FULL-SCREEN 3D & LITE CRICKET FIELD
        CricketField3D(
            modifier = Modifier.fillMaxSize(),
            graphicsProfile = graphicsProfile,
            cameraAngle = currentCamera,
            isDelivering = isDelivering,
            deliveryProgress = animatableProgress.value,
            ballExitAngleDegrees = ballExitAngle,
            ballExitDistance = ballExitDistance,
            shotType = selectedShot,
            battingTeamColor = innings.battingTeam.primaryColor,
            bowlingTeamColor = innings.bowlingTeam.primaryColor,
            fieldPreset = match.currentFieldPreset,
            weather = match.weather
        )

        // 2. BROADCAST HUD OVERLAY
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP SECTION: Ribbon Scoreboard, Recent Balls, Live Ticker
            Column {
                MatchTopRibbon(
                    match = match,
                    innings = innings,
                    language = language,
                    currentCamera = currentCamera,
                    onCycleCamera = {
                        currentCamera = when (currentCamera) {
                            CameraAngle.BROADCAST -> CameraAngle.BEHIND_BATSMAN
                            CameraAngle.BEHIND_BATSMAN -> CameraAngle.STUMP_CAM
                            CameraAngle.STUMP_CAM -> CameraAngle.DRONE_AERIAL
                            CameraAngle.DRONE_AERIAL -> CameraAngle.BROADCAST
                        }
                    },
                    onPauseClick = { showPauseMenu = true },
                    onDrsClick = {
                        val activeDRS = DRSState(
                            isActive = true,
                            reviewingTeam = innings.battingTeam,
                            batsman = innings.striker,
                            bowler = innings.currentBowler,
                            originalDecision = DismissalType.LBW,
                            snickoContactDetected = false,
                            pitchingInLine = true,
                            impactInLine = true,
                            hittingWickets = true,
                            finalVerdict = DismissalType.LBW,
                            isReviewSuccessful = false
                        )
                        pendingDRS = activeDRS
                        showDRS = true
                    }
                )

                // Recent Balls Strip
                MatchRecentBalls(deliveries = innings.currentOverDeliveries)

                // Live Commentary Ticker Banner
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 2.dp)
                        .clickable { showScorecard = true },
                    color = StadiumNavyCard.copy(alpha = 0.88f),
                    shape = RoundedCornerShape(8.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = CricketGreenDark,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "COMM",
                                color = MilestoneGold,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.URDU) latestCommentaryUr else latestCommentaryEn,
                            color = TextWhitePrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == Language.URDU) "اسکورکارڈ" else "SCORECARD",
                            color = MilestoneGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Timing Feedback Banner
                lastTimingResult?.let { timing ->
                    Surface(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 4.dp),
                        color = when (timing) {
                            TimingResult.PERFECT -> CricketGreen
                            TimingResult.GOOD -> Color(0xFF0284C7)
                            TimingResult.EARLY, TimingResult.LATE -> MilestoneGold
                            TimingResult.MISSED -> WicketCrimson
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (language == Language.URDU) timing.feedbackUr else timing.feedbackEn,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // BOTTOM SECTION: Dynamic Interactive Touch Controls (Batting or Bowling)
            if (isUserBatting) {
                BattingControls(
                    language = language,
                    selectedFootwork = selectedFootwork,
                    onFootworkChange = { selectedFootwork = it },
                    selectedIntent = selectedIntent,
                    onIntentChange = { selectedIntent = it },
                    selectedShot = selectedShot,
                    onShotChange = { selectedShot = it },
                    onPlayShotClick = { intent, shot ->
                        if (!isDelivering) {
                            // User plays a shot on upcoming delivery
                            coroutineScope.launch {
                                executeDeliveryCycle(
                                    repository = repository,
                                    audio = audio,
                                    match = match,
                                    isUserBatting = true,
                                    userBattingInput = CricketPhysicsEngine.BattingInput(
                                        shotType = shot,
                                        footwork = selectedFootwork,
                                        intent = intent,
                                        timingDeltaMs = (Random.nextLong(120) - 40) // realistic user response window
                                    ),
                                    bowlingInput = CricketPhysicsEngine.DeliveryInput(
                                        deliveryType = selectedDelivery,
                                        targetLineOffset = (Random.nextFloat() * 0.8f) - 0.4f,
                                        targetLengthDistance = 0.2f + Random.nextFloat() * 0.5f,
                                        speedKmh = 135 + Random.nextInt(15),
                                        swingAmount = (Random.nextFloat() - 0.5f) * 0.8f,
                                        spinAmount = 0.2f,
                                        pitchType = match.pitch,
                                        weather = match.weather,
                                                                                                isFrontFootNoBall = Random.nextFloat() < 0.04f
                                    ),
                                    animatableProgress = animatableProgress,
                                    onUpdateProgress = { isDelivering = it },
                                    onUpdateExit = { angle, dist ->
                                        ballExitAngle = angle
                                        ballExitDistance = dist
                                    },
                                    onCommentary = { en, ur ->
                                        latestCommentaryEn = en
                                        latestCommentaryUr = ur
                                    },
                                    onTimingResult = { lastTimingResult = it },
                                    onTriggerDRS = { drs ->
                                        pendingDRS = drs
                                        showDRS = true
                                    },
                                    onMatchEnded = onMatchFinished
                                )
                            }
                        }
                    },
                    onRunCallClick = { call ->
                        // Quick run decision
                        audio.playBatCrack()
                    },
                    timingMeterProgress = animatableProgress.value,
                    isBallInFlight = isDelivering
                )
            } else {
                // USER IS BOWLING
                BowlingControls(
                    bowler = innings.currentBowler,
                    language = language,
                    selectedDelivery = selectedDelivery,
                    onDeliverySelected = { selectedDelivery = it },
                    targetLineOffset = targetLineOffset,
                    targetLengthDistance = targetLengthDistance,
                    onTargetChanged = { line, length ->
                        targetLineOffset = line
                        targetLengthDistance = length
                    },
                    speedKmh = speedKmh,
                    onSpeedChange = { speedKmh = it },
                    swingSpinAmount = swingSpinAmount,
                    onSwingSpinChange = { swingSpinAmount = it },
                    onStartDeliveryClick = {
                        if (!isDelivering) {
                            coroutineScope.launch {
                                executeDeliveryCycle(
                                    repository = repository,
                                    audio = audio,
                                    match = match,
                                    isUserBatting = false,
                                    userBattingInput = null, // AI will bat
                                    bowlingInput = CricketPhysicsEngine.DeliveryInput(
                                        deliveryType = selectedDelivery,
                                        targetLineOffset = targetLineOffset,
                                        targetLengthDistance = targetLengthDistance,
                                        speedKmh = speedKmh,
                                        swingAmount = swingSpinAmount,
                                        spinAmount = swingSpinAmount,
                                        pitchType = match.pitch,
                                        weather = match.weather,
                                                                                                isFrontFootNoBall = Random.nextFloat() < 0.04f
                                    ),
                                    animatableProgress = animatableProgress,
                                    onUpdateProgress = { isDelivering = it },
                                    onUpdateExit = { angle, dist ->
                                        ballExitAngle = angle
                                        ballExitDistance = dist
                                    },
                                    onCommentary = { en, ur ->
                                        latestCommentaryEn = en
                                        latestCommentaryUr = ur
                                    },
                                    onTimingResult = { lastTimingResult = it },
                                    onTriggerDRS = { drs ->
                                        pendingDRS = drs
                                        showDRS = true
                                    },
                                    onMatchEnded = onMatchFinished
                                )
                            }
                        }
                    },
                    onAppealClick = {
                        audio.playAppeal()
                    },
                    isDelivering = isDelivering
                )
            }
        }

        // 3. DIALOGS & POPUPS
        // Full Scorecard Overlay
        if (showScorecard) {
            ScorecardOverlay(
                innings = innings,
                language = language,
                onDismiss = { showScorecard = false }
            )
        }

        // DRS Visualizer Screen
        if (showDRS && pendingDRS != null) {
            DRSVisualizer(
                drsState = pendingDRS!!,
                language = language,
                onCompleteReview = { isOut ->
                    showDRS = false
                    pendingDRS = null
                }
            )
        }

        // Pause Menu Dialog
        if (showPauseMenu) {
            AlertDialog(
                onDismissRequest = { showPauseMenu = false },
                title = {
                    Text(
                        text = if (language == Language.URDU) "میچ عارضی طور پر روکا گیا" else "MATCH PAUSED",
                        color = TextWhitePrimary,
                        fontWeight = FontWeight.Black
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "${innings.battingTeam.nameEn} ${innings.totalRuns}/${innings.wicketsLost} (${innings.currentOversFormatted} Ov)",
                            color = MilestoneGold,
                            fontWeight = FontWeight.Bold
                        )
                        Divider(color = StadiumBorder)

                        // Graphics profile toggle
                        Text("Graphics Engine Profile:", color = TextMutedSecondary, fontSize = 12.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            GraphicsProfile.values().forEach { prof ->
                                val isSelected = prof == graphicsProfile
                                Surface(
                                    modifier = Modifier.clickable { repository.currentGraphics.value = prof },
                                    color = if (isSelected) CricketGreen else StadiumNavySurface,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (prof == GraphicsProfile.LITE_3D) "3D Lite" else if (prof == GraphicsProfile.BALANCED_3D) "Balanced" else "3D High",
                                        color = if (isSelected) Color.White else TextMutedSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        // Audio toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Sound Effects & Audio", color = TextWhitePrimary, fontSize = 13.sp)
                            Switch(
                                checked = audio.isAudioEnabled,
                                onCheckedChange = { audio.isAudioEnabled = it }
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showPauseMenu = false },
                        colors = ButtonDefaults.buttonColors(containerColor = CricketGreen)
                    ) {
                        Text("RESUME", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showPauseMenu = false
                            onExitToHome()
                        }
                    ) {
                        Text("EXIT TO MENU", color = WicketCrimson, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = StadiumNavyCard,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

private suspend fun executeDeliveryCycle(
    repository: CricketRepository,
    audio: CricketAudioEngine,
    match: MatchState,
    isUserBatting: Boolean,
    userBattingInput: CricketPhysicsEngine.BattingInput?,
    bowlingInput: CricketPhysicsEngine.DeliveryInput,
    animatableProgress: Animatable<Float, AnimationVector1D>,
    onUpdateProgress: (Boolean) -> Unit,
    onUpdateExit: (Float, Float) -> Unit,
    onCommentary: (String, String) -> Unit,
    onTimingResult: (TimingResult) -> Unit,
    onTriggerDRS: (DRSState) -> Unit,
    onMatchEnded: () -> Unit
) {
    onUpdateProgress(true)
    animatableProgress.snapTo(0f)

    // AI batting generation if user is bowling
    val actualBattingInput = userBattingInput ?: CricketPhysicsEngine.BattingInput(
        shotType = ShotType.values().random(),
        footwork = Footwork.FRONT_FOOT,
        intent = if (match.currentInnings.requiredRunRate ?: 0.0 > 10.0) BattingIntent.POWER else BattingIntent.NORMAL,
        timingDeltaMs = (Random.nextLong(100) - 40)
    )

    val currentInnings = match.currentInnings
    val overNo = currentInnings.legalBallsBowled / 6
    val ballNo = (currentInnings.legalBallsBowled % 6) + 1

    // Simulation calculation
    val result = CricketPhysicsEngine.simulateDelivery(
        overNumber = overNo,
        ballInOver = ballNo,
        bowler = currentInnings.currentBowler,
        striker = currentInnings.striker,
        deliveryInput = bowlingInput,
        battingInput = actualBattingInput,
        fieldPreset = match.currentFieldPreset,
        isFreeHitActive = match.isFreeHitActive
    )

    // Phase 1: Ball in flight from bowler to batsman (0.0 to 0.5)
    animatableProgress.animateTo(
        targetValue = 0.5f,
        animationSpec = tween(durationMillis = 650, easing = LinearEasing)
    )

    // Bat contact moment!
    val outcome = result.outcome
    onTimingResult(outcome.timingResult)
    onUpdateExit(result.exitAngleDegrees, result.distanceMeters)

    if (outcome.isWicket) {
        audio.playStumpClatter()
    } else if (outcome.isBoundarySix) {
        audio.playSixRoar()
    } else if (outcome.isBoundaryFour) {
        audio.playBoundaryFanfare()
    } else if (outcome.runsScored > 0) {
        audio.playBatCrack()
    }

    // Phase 2: Exit shot flight (0.5 to 1.0)
    animatableProgress.animateTo(
        targetValue = 1.0f,
        animationSpec = tween(durationMillis = 750, easing = FastOutSlowInEasing)
    )

    // Only wides and no-balls are illegal deliveries; byes/leg-byes still count as legal balls.
    val isIllegalDelivery = outcome.extraType == "WD" || outcome.extraType == "NB"
    if (!isIllegalDelivery) {
        currentInnings.legalBallsBowled++
        match.isFreeHitActive = false
    } else if (outcome.extraType == "NB") {
        match.isFreeHitActive = true
    }

    currentInnings.totalRuns += outcome.runsScored
    currentInnings.currentOverDeliveries.add(outcome)
    currentInnings.allDeliveries.add(outcome)

    // Keep extras in the innings extras columns. outcome.runsScored is total innings runs.
    when (outcome.extraType) {
        "WD" -> currentInnings.extrasWides += outcome.runsScored
        "NB" -> currentInnings.extrasNoBalls += 1
        "LB" -> currentInnings.extrasLegByes += outcome.runsScored
        "B" -> currentInnings.extrasByes += outcome.runsScored
    }

    // Extras are not batter runs. A wide is not a ball faced; a no-ball is.
    val strikerStats = currentInnings.striker.currentMatchStats
    val batterRuns = if (outcome.extraType == null) outcome.runsScored else 0
    strikerStats.runsScored += batterRuns
    if (outcome.extraType != "WD") strikerStats.ballsFaced++
    if (outcome.isBoundaryFour) strikerStats.fours++
    if (outcome.isBoundarySix) strikerStats.sixes++

    // Byes and leg-byes are not charged to the bowler.
    val bowlerStats = currentInnings.currentBowler.currentMatchStats
    val bowlerRunsConceded = if (outcome.extraType == "B" || outcome.extraType == "LB") 0 else outcome.runsScored
    bowlerStats.runsConceded += bowlerRunsConceded

    if (outcome.isWicket) {
        currentInnings.wicketsLost++
        strikerStats.isOut = true
        strikerStats.dismissalDescription = outcome.dismissalType.labelEn
        bowlerStats.wicketsTaken++

        currentInnings.fallOfWickets.add(
            FallOfWicket(
                score = currentInnings.totalRuns,
                wicketNumber = currentInnings.wicketsLost,
                playerOutName = currentInnings.striker.name,
                overDecimal = currentInnings.legalBallsBowled / 6.0
            )
        )

        // Next batsman comes in
        if (currentInnings.wicketsLost < 10) {
            currentInnings.strikerIndex = (currentInnings.wicketsLost + 1).coerceAtMost(10)
        }
    } else {
        // Rotate strike on odd runs
        if (outcome.runsScored % 2 != 0) {
            val temp = currentInnings.strikerIndex
            currentInnings.strikerIndex = currentInnings.nonStrikerIndex
            currentInnings.nonStrikerIndex = temp
        }
    }

    // Add wagon wheel shot
    if (outcome.runsScored > 0 || outcome.isBoundaryFour || outcome.isBoundarySix) {
        currentInnings.wagonWheelShots.add(
            WagonWheelShot(
                angleDegrees = result.exitAngleDegrees,
                distanceMultiplier = (result.distanceMeters / 75f).coerceIn(0.2f, 1.0f),
                runs = outcome.runsScored,
                isAerial = outcome.isBoundarySix || (actualBattingInput.intent == BattingIntent.LOFT)
            )
        )
    }

    // Update commentary
    onCommentary(result.commentaryEn, result.commentaryUr)

    // Check Over Completion (Rotate Strike & Pick next bowler)
    if (!isIllegalDelivery && currentInnings.legalBallsBowled > 0 && currentInnings.legalBallsBowled % 6 == 0) {
        bowlerStats.oversBowled += 1.0
        currentInnings.currentOverDeliveries.clear()
        // Rotate strike at end of over
        val temp = currentInnings.strikerIndex
        currentInnings.strikerIndex = currentInnings.nonStrikerIndex
        currentInnings.nonStrikerIndex = temp
        // Change Bowler
        currentInnings.currentBowlerIndex = (currentInnings.currentBowlerIndex + 1) % 11
    }

    // Check Innings or Match Completion
    val maxBalls = match.maxOvers * 6
    val isTargetReached = currentInnings.targetScore != null && currentInnings.totalRuns >= currentInnings.targetScore!!
    val isInningsOver = currentInnings.wicketsLost >= 10 || currentInnings.legalBallsBowled >= maxBalls || isTargetReached

    if (isInningsOver) {
        if (match.currentInningsIndex == 0) {
            // First Innings Complete -> Start 2nd Innings
            val target = currentInnings.totalRuns + 1
            repository.startSecondInnings(target)
            onCommentary("Innings Break! Target is $target runs.", "اننگز ختم! دوسری ٹیم کو جیت کے لیے $target رنز درکار ہیں۔")
        } else {
            // Match Finished!
            match.isMatchFinished = true
            val teamA = match.teamA
            val teamB = match.teamB
            val target = currentInnings.targetScore ?: 0
            val won = currentInnings.totalRuns >= target

            if (won) {
                match.matchResultSummaryEn = "${currentInnings.battingTeam.nameEn} WON BY ${10 - currentInnings.wicketsLost} WICKETS!"
                match.matchResultSummaryUr = "${currentInnings.battingTeam.nameUr} نے میچ جیت لیا!"
            } else {
                val margin = (target - 1) - currentInnings.totalRuns
                match.matchResultSummaryEn = "${currentInnings.bowlingTeam.nameEn} WON BY $margin RUNS!"
                match.matchResultSummaryUr = "${currentInnings.bowlingTeam.nameUr} نے $margin رنز سے کامیابی حاصل کی!"
            }
            repository.updateCurrentMatch(match)
            onMatchEnded()
        }
    }

    delay(300)
    onUpdateProgress(false)
}
