package com.example.simulation

import com.example.model.*
import kotlin.math.abs
import kotlin.random.Random

object CricketPhysicsEngine {

    data class DeliveryInput(
        val deliveryType: DeliveryType,
        val targetLineOffset: Float, // -1.0 (far leg) to 1.0 (far off)
        val targetLengthDistance: Float, // 0.0 (full/yorker) to 1.0 (short/bouncer)
        val speedKmh: Int,
        val swingAmount: Float, // -1.0 to 1.0
        val spinAmount: Float,  // -1.0 to 1.0
        val pitchType: PitchType,
        val weather: WeatherCondition,
        // Result of checking the bowler's front foot at release time.
        val isFrontFootNoBall: Boolean = false
    )

    data class BattingInput(
        val shotType: ShotType,
        val footwork: Footwork,
        val intent: BattingIntent,
        val timingDeltaMs: Long, // 0 = perfect timing, negative = early, positive = late
        val customAngleDegrees: Float? = null,
        val isLeftHander: Boolean = false
    )

    data class SimulationResult(
        val outcome: BallDeliveryOutcome,
        val commentaryEn: String,
        val commentaryUr: String,
        val ballTrajectoryPoints: List<Pair<Float, Float>>, // (x, y) relative to pitch coordinates
        val exitAngleDegrees: Float,
        val distanceMeters: Float,
        val isAppealWorthy: Boolean,
        val appealDismissal: DismissalType
    )

    fun simulateDelivery(
        overNumber: Int,
        ballInOver: Int,
        bowler: Player,
        striker: Player,
        deliveryInput: DeliveryInput,
        battingInput: BattingInput?,
        fieldPreset: FieldPreset,
        isFreeHitActive: Boolean
    ): SimulationResult {
        val pitch = deliveryInput.pitchType
        val weather = deliveryInput.weather

        // Evaluate delivery trajectory and deviations
        val effectiveSwing = deliveryInput.swingAmount * (if (weather == WeatherCondition.OVERCAST) 1.3f else 1.0f) * pitch.seam
        val effectiveSpin = deliveryInput.spinAmount * pitch.spin
        val effectiveBounce = pitch.bounce * (if (deliveryInput.targetLengthDistance > 0.7f) 1.25f else 0.95f)

        // Check for Bowling Extras: Wide or No-ball
        val isWide = abs(deliveryInput.targetLineOffset) > 0.82f
        val isNoBall = deliveryInput.isFrontFootNoBall

        if (isWide && !isNoBall) {
            val outcome = BallDeliveryOutcome(
                overNumber = overNumber,
                ballInOver = ballInOver,
                deliveryType = deliveryInput.deliveryType,
                speedKmh = deliveryInput.speedKmh,
                runsScored = 1,
                isExtra = true,
                extraType = "WD",
                isWicket = false,
                dismissalType = DismissalType.NONE,
                batsmanOnStrike = striker,
                bowler = bowler,
                shotPlayed = null,
                timingResult = TimingResult.MISSED,
                contactQuality = ContactQuality.CLEAN_MISS,
                shotAngleDegrees = 0f
            )
            val comEn = "Wide ball! Bowled too far outside the tramline. Umpire signals wide."
            val comUr = "وائیڈ بال! گیند کریز سے کافی باہر، امپائر کا وائیڈ کا اشارہ۔"
            return SimulationResult(
                outcome = outcome,
                commentaryEn = comEn,
                commentaryUr = comUr,
                ballTrajectoryPoints = generateTrajectory(deliveryInput, 0f, 0f),
                exitAngleDegrees = 0f,
                distanceMeters = 0f,
                isAppealWorthy = false,
                appealDismissal = DismissalType.NONE
            )
        }

        if (isNoBall) {
            val outcome = BallDeliveryOutcome(
                overNumber = overNumber,
                ballInOver = ballInOver,
                deliveryType = deliveryInput.deliveryType,
                speedKmh = deliveryInput.speedKmh,
                runsScored = 1,
                isExtra = true,
                extraType = "NB",
                isWicket = false,
                dismissalType = DismissalType.NONE,
                batsmanOnStrike = striker,
                bowler = bowler,
                shotPlayed = null,
                timingResult = TimingResult.MISSED,
                contactQuality = ContactQuality.CLEAN_MISS,
                shotAngleDegrees = 0f
            )
            val comEn = "NO BALL! Bowler oversteps the crease! FREE HIT coming up on next ball!"
            val comUr = "نو بال! بولر نے کریز پار کر دی! اگلی گیند فری ہٹ ہو گی!"
            return SimulationResult(
                outcome = outcome,
                commentaryEn = comEn,
                commentaryUr = comUr,
                ballTrajectoryPoints = generateTrajectory(deliveryInput, 0f, 0f),
                exitAngleDegrees = 0f,
                distanceMeters = 0f,
                isAppealWorthy = false,
                appealDismissal = DismissalType.NONE
            )
        }

        // Batting Calculation
        if (battingInput == null || battingInput.shotType == ShotType.LEAVE) {
            // Batsman leaves or did not offer a stroke
            val isBallHittingStumps = abs(deliveryInput.targetLineOffset) < 0.22f && deliveryInput.targetLengthDistance in 0.1f..0.6f
            val isBowled = isBallHittingStumps && !isFreeHitActive
            val isLbw = abs(deliveryInput.targetLineOffset) in 0.1f..0.35f && deliveryInput.targetLengthDistance in 0.2f..0.55f && !isFreeHitActive

            val dismissal = when {
                isBowled -> DismissalType.BOWLED
                isLbw && Random.nextFloat() < 0.65f -> DismissalType.LBW
                else -> DismissalType.NONE
            }

            val outcome = BallDeliveryOutcome(
                overNumber = overNumber,
                ballInOver = ballInOver,
                deliveryType = deliveryInput.deliveryType,
                speedKmh = deliveryInput.speedKmh,
                runsScored = 0,
                isExtra = false,
                isWicket = dismissal != DismissalType.NONE,
                dismissalType = dismissal,
                batsmanOnStrike = striker,
                bowler = bowler,
                shotPlayed = ShotType.LEAVE,
                timingResult = TimingResult.MISSED,
                contactQuality = ContactQuality.CLEAN_MISS,
                shotAngleDegrees = 0f
            )

            val comEn = when (dismissal) {
                DismissalType.BOWLED -> "BOWLED HIM! Left the ball and the off-stump is uprooted! What a sight!"
                DismissalType.LBW -> "LOUD APPEAL! Struck on the pads offering no stroke! Umpire raises the finger!"
                else -> "Good leave outside the off stump, safely through to the keeper."
            }
            val comUr = when (dismissal) {
                DismissalType.BOWLED -> "کلین بولڈ! بلے باز نے گیند چھوڑی اور وکٹ اکھڑ گئی! شاندار بولنگ!"
                DismissalType.LBW -> "پرشور اپیل! بلے باز پیڈ پر لگنے سے آؤٹ! امپائر کی انگلی فضا میں بلند!"
                else -> "خوبصورت انداز میں گیند کو وکٹ کیپر کے پاس جانے دیا۔"
            }

            return SimulationResult(
                outcome = outcome,
                commentaryEn = comEn,
                commentaryUr = comUr,
                ballTrajectoryPoints = generateTrajectory(deliveryInput, 0f, 0f),
                exitAngleDegrees = 0f,
                distanceMeters = 0f,
                isAppealWorthy = dismissal == DismissalType.LBW,
                appealDismissal = dismissal
            )
        }

        // Calculate Timing
        val absDelta = abs(battingInput.timingDeltaMs)
        val timingResult = when {
            absDelta < 40 -> TimingResult.PERFECT
            absDelta < 90 -> TimingResult.GOOD
            battingInput.timingDeltaMs < 0 -> TimingResult.EARLY
            battingInput.timingDeltaMs > 0 -> TimingResult.LATE
            else -> TimingResult.MISSED
        }

        // Contact Quality
        val contactQuality = when (timingResult) {
            TimingResult.PERFECT -> if (Random.nextFloat() < 0.85f) ContactQuality.SWEET_SPOT else ContactQuality.MIDDLE
            TimingResult.GOOD -> if (Random.nextFloat() < 0.6f) ContactQuality.MIDDLE else ContactQuality.INSIDE_EDGE
            TimingResult.EARLY -> if (Random.nextFloat() < 0.5f) ContactQuality.LEADING_EDGE else ContactQuality.TOE_END
            TimingResult.LATE -> if (Random.nextFloat() < 0.65f) ContactQuality.OUTSIDE_EDGE else ContactQuality.PAD_CONTACT
            TimingResult.MISSED -> ContactQuality.CLEAN_MISS
        }

        // Check Dismissal Opportunities
        var dismissal = DismissalType.NONE
        val isStumpsThreatened = abs(deliveryInput.targetLineOffset) < 0.25f && deliveryInput.targetLengthDistance < 0.65f

        if (!isFreeHitActive) {
            when (contactQuality) {
                ContactQuality.CLEAN_MISS -> {
                    if (isStumpsThreatened) dismissal = DismissalType.BOWLED
                    else if (abs(deliveryInput.targetLineOffset) < 0.38f && deliveryInput.targetLengthDistance < 0.5f) {
                        dismissal = DismissalType.LBW
                    }
                }
                ContactQuality.INSIDE_EDGE -> {
                    if (isStumpsThreatened && Random.nextFloat() < 0.45f) {
                        dismissal = DismissalType.BOWLED
                    }
                }
                ContactQuality.OUTSIDE_EDGE -> {
                    if (Random.nextFloat() < 0.70f) {
                        dismissal = DismissalType.CAUGHT_BEHIND
                    }
                }
                ContactQuality.LEADING_EDGE -> {
                    if (Random.nextFloat() < 0.55f) {
                        dismissal = DismissalType.CAUGHT
                    }
                }
                ContactQuality.TOE_END -> {
                    if (battingInput.intent == BattingIntent.LOFT || battingInput.intent == BattingIntent.POWER) {
                        if (Random.nextFloat() < 0.50f) dismissal = DismissalType.CAUGHT
                    }
                }
                ContactQuality.PAD_CONTACT -> {
                    if (isStumpsThreatened && Random.nextFloat() < 0.75f) dismissal = DismissalType.LBW
                }
                else -> Unit
            }
        }

        // Calculate Shot Direction and Distance
        val baseAngle = battingInput.customAngleDegrees ?: battingInput.shotType.defaultAngleDegrees
        val angleVariation = (Random.nextFloat() - 0.5f) * 20f
        val finalAngle = (baseAngle + angleVariation + 360f) % 360f

        val batterPower = striker.ratings.power / 100f
        val batterTimingRating = striker.ratings.timing / 100f
        val intentMultiplier = battingInput.intent.powerMultiplier

        var distanceMeters = when (contactQuality) {
            ContactQuality.SWEET_SPOT -> (65f + Random.nextFloat() * 35f) * batterPower * intentMultiplier
            ContactQuality.MIDDLE -> (45f + Random.nextFloat() * 30f) * batterTimingRating * intentMultiplier
            ContactQuality.INSIDE_EDGE, ContactQuality.OUTSIDE_EDGE -> (15f + Random.nextFloat() * 30f)
            ContactQuality.LEADING_EDGE, ContactQuality.TOE_END -> (20f + Random.nextFloat() * 25f)
            ContactQuality.PAD_CONTACT -> 5f + Random.nextFloat() * 10f
            ContactQuality.CLEAN_MISS -> 0f
        }

        // Scoring rules
        var runsScored = 0
        if (dismissal == DismissalType.NONE) {
            if (battingInput.intent == BattingIntent.DEFEND) {
                distanceMeters = distanceMeters.coerceAtMost(18f)
                runsScored = if (distanceMeters > 12f && Random.nextFloat() < 0.25f) 1 else 0
            } else if (battingInput.intent == BattingIntent.LOFT || battingInput.intent == BattingIntent.POWER) {
                if (distanceMeters >= 75f) {
                    runsScored = 6
                } else if (distanceMeters >= 60f) {
                    runsScored = 4
                } else if (distanceMeters >= 35f) {
                    runsScored = if (Random.nextFloat() < 0.6f) 2 else 1
                } else if (distanceMeters >= 15f) {
                    runsScored = 1
                }
            } else {
                // Normal shot
                if (distanceMeters >= 68f && contactQuality == ContactQuality.SWEET_SPOT) {
                    runsScored = 4
                } else if (distanceMeters >= 40f) {
                    runsScored = if (Random.nextFloat() < 0.4f) 2 else 1
                } else if (distanceMeters >= 12f) {
                    runsScored = 1
                }
            }
        }

        // Check Run-out chance on quick single
        if (runsScored == 1 && dismissal == DismissalType.NONE && Random.nextFloat() < 0.05f) {
            dismissal = DismissalType.RUN_OUT
            runsScored = 0
        }

        val outcome = BallDeliveryOutcome(
            overNumber = overNumber,
            ballInOver = ballInOver,
            deliveryType = deliveryInput.deliveryType,
            speedKmh = deliveryInput.speedKmh,
            runsScored = runsScored,
            isExtra = false,
            isWicket = dismissal != DismissalType.NONE,
            dismissalType = dismissal,
            batsmanOnStrike = striker,
            bowler = bowler,
            shotPlayed = battingInput.shotType,
            timingResult = timingResult,
            contactQuality = contactQuality,
            shotAngleDegrees = finalAngle,
            isBoundaryFour = (runsScored == 4),
            isBoundarySix = (runsScored == 6)
        )

        // Generate bilingual commentary
        val commentaryPair = generateCommentary(outcome, striker, bowler, battingInput.shotType)

        return SimulationResult(
            outcome = outcome,
            commentaryEn = commentaryPair.first,
            commentaryUr = commentaryPair.second,
            ballTrajectoryPoints = generateTrajectory(deliveryInput, finalAngle, distanceMeters),
            exitAngleDegrees = finalAngle,
            distanceMeters = distanceMeters,
            isAppealWorthy = dismissal == DismissalType.LBW || dismissal == DismissalType.CAUGHT_BEHIND,
            appealDismissal = dismissal
        )
    }

    private fun generateTrajectory(input: DeliveryInput, shotAngle: Float, distance: Float): List<Pair<Float, Float>> {
        val points = mutableListOf<Pair<Float, Float>>()
        points.add(Pair(0f, 0f))
        points.add(Pair(input.targetLineOffset * 20f, 50f))
        points.add(Pair(input.targetLineOffset * 25f + input.swingAmount * 15f, 100f))
        if (distance > 0f) {
            val radians = Math.toRadians(shotAngle.toDouble())
            val endX = (distance * kotlin.math.sin(radians)).toFloat()
            val endY = 100f + (distance * kotlin.math.cos(radians)).toFloat()
            points.add(Pair(endX, endY))
        }
        return points
    }

    private fun generateCommentary(
        outcome: BallDeliveryOutcome,
        striker: Player,
        bowler: Player,
        shot: ShotType
    ): Pair<String, String> {
        val sName = striker.name
        val bName = bowler.name

        return when {
            outcome.isWicket -> when (outcome.dismissalType) {
                DismissalType.BOWLED -> Pair(
                    "OUT! BOWLED HIM! Timber strikes! $bName crashes through $sName's defence!",
                    "آؤٹ! کلین بولڈ! $bName کی شاندار گیند نے $sName کی وکٹیں بکھیر دیں!"
                )
                DismissalType.CAUGHT -> Pair(
                    "WICKET! In the air and CAUGHT! $sName mistimes it straight to the fielder!",
                    "وکٹ گر گئی! اونچا شاٹ اور فیلڈر کا شاندار کیچ! $sName پولین واپس!"
                )
                DismissalType.CAUGHT_BEHIND -> Pair(
                    "EDGED AND TAKEN! Wicketkeeper takes a sharp catch behind the stumps!",
                    "ایج لگا اور کیپر نے شاندار کیچ تھام لیا! بلے باز آؤٹ!"
                )
                DismissalType.LBW -> Pair(
                    "LBW! Plumb in front of the stumps! Umpire raises the finger without hesitation!",
                    "ایل بی ڈبلیو! سیدھا پیڈ پر ٹکرائی اور امپائر کی انگلی فضا میں بلند!"
                )
                DismissalType.RUN_OUT -> Pair(
                    "RUN OUT! DIRECT HIT! Magnificent throw from the deep catches $sName short!",
                    "رن آؤٹ! ڈائریکٹ ہٹ! فیلڈر کا بجلی کی رفتار سے تھرو اور وکٹ اڑ گئی!"
                )
                DismissalType.STUMPED -> Pair(
                    "STUMPED! Lightning quick glovework from the keeper! Bails light up!",
                    "اسٹمپ آؤٹ! وکٹ کیپر کی بجلی جیسی چستی اور بیلز اکھڑ گئیں!"
                )
                else -> Pair(
                    "WICKET! Big breakthrough for $bName!",
                    "وکٹ! $bName کے لیے زبردست کامیابی!"
                )
            }
            outcome.isBoundarySix -> Pair(
                "SIX RUNS! THAT IS HUGE! ${shot.shotNameEn} dispatched deep into the second tier! What a colossal hit from $sName!",
                "فلک شگاف چھکا! ${shot.shotNameUr}! گیند اسٹیڈیم کی چھت پر جا گری! $sName کا لاجواب شاٹ!"
            )
            outcome.isBoundaryFour -> Pair(
                "FOUR! Exquisite timing on that ${shot.shotNameEn}! Pierces the infield and races away across the carpet!",
                "شاندار چوکا! ${shot.shotNameUr}! فیلڈرز بے بس، گیند بجلی کی رفتار سے باؤنڈری لائن پار کر گئی!"
            )
            outcome.runsScored == 2 -> Pair(
                "Pushed into the gap, excellent running between the wickets to turn one into two.",
                "گیپ میں شاٹ، دونوں بلے بازوں کی بہترین دوڑ، آسانی سے 2 رنز مکمل۔"
            )
            outcome.runsScored == 1 -> Pair(
                "Tucked away for a comfortable single, rotates the strike smartly.",
                "ہلکے ہاتھ سے کھیل کر سنگل لیا اور اسٹرائیک بدل لی۔"
            )
            else -> Pair(
                "Dot ball. Solid defence from $sName, bowler gives nothing away.",
                "کوئی رن نہیں۔ $sName کا مضبوط دفاع، بولر کا نپا تلا اوور۔"
            )
        }
    }
}
