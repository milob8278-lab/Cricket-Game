package com.example

import com.example.model.*
import com.example.simulation.CricketPhysicsEngine
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun defaultTeams_areLoadedWithSquads() {
        val teams = DefaultTeams.getInitialTeams()
        assertTrue("At least 6 teams should be present", teams.size >= 6)

        val lahore = teams.first { it.id == "team_lahore" }
        assertEquals(15, lahore.squad.size)
        assertEquals(11, lahore.getPlayingXI().size)
        assertNotNull(lahore.getCaptain())
        assertNotNull(lahore.getWicketkeeper())
    }

    @Test
    fun defaultVenues_haveValidPitchesAndDimensions() {
        val venues = DefaultVenues.getVenues()
        assertTrue(venues.isNotEmpty())
        venues.forEach { venue ->
            assertTrue(venue.boundaryDistanceMeters in 60..95)
            assertTrue(venue.capacity > 10000)
            assertNotNull(venue.defaultPitch)
        }
    }

    @Test
    fun physicsEngine_detectsWideBall() {
        val teams = DefaultTeams.getInitialTeams()
        val bowler = teams[0].getPlayingXI()[10]
        val striker = teams[1].getPlayingXI()[0]

        val deliveryInput = CricketPhysicsEngine.DeliveryInput(
            deliveryType = DeliveryType.OUTSWINGER,
            targetLineOffset = 0.95f, // Far outside wide guideline
            targetLengthDistance = 0.4f,
            speedKmh = 142,
            swingAmount = 0.2f,
            spinAmount = 0.0f,
            pitchType = PitchType.FLAT_BATTING,
            weather = WeatherCondition.SUNNY
        )

        val result = CricketPhysicsEngine.simulateDelivery(
            overNumber = 1,
            ballInOver = 1,
            bowler = bowler,
            striker = striker,
            deliveryInput = deliveryInput,
            battingInput = null,
            fieldPreset = FieldPreset.ATTACKING,
            isFreeHitActive = false
        )

        assertTrue(result.outcome.isExtra)
        assertEquals("WD", result.outcome.extraType)
        assertEquals(1, result.outcome.runsScored)
    }

    @Test
    fun physicsEngine_cleanBowledWhenLeavingStumpsDelivery() {
        val teams = DefaultTeams.getInitialTeams()
        val bowler = teams[0].getPlayingXI()[7]
        val striker = teams[1].getPlayingXI()[0]

        val deliveryInput = CricketPhysicsEngine.DeliveryInput(
            deliveryType = DeliveryType.YORKER,
            targetLineOffset = 0.05f, // Directly hitting stumps
            targetLengthDistance = 0.25f,
            speedKmh = 145,
            swingAmount = 0.0f,
            spinAmount = 0.0f,
            pitchType = PitchType.FLAT_BATTING,
            weather = WeatherCondition.SUNNY
        )

        val result = CricketPhysicsEngine.simulateDelivery(
            overNumber = 2,
            ballInOver = 3,
            bowler = bowler,
            striker = striker,
            deliveryInput = deliveryInput,
            battingInput = CricketPhysicsEngine.BattingInput(
                shotType = ShotType.LEAVE,
                footwork = Footwork.FRONT_FOOT,
                intent = BattingIntent.DEFEND,
                timingDeltaMs = 0
            ),
            fieldPreset = FieldPreset.ATTACKING,
            isFreeHitActive = false
        )

        assertTrue(result.outcome.isWicket)
        assertEquals(DismissalType.BOWLED, result.outcome.dismissalType)
    }

    @Test
    fun drsBallTracking_generatesValidTrajectoryPoints() {
        val teams = DefaultTeams.getInitialTeams()
        val bowler = teams[0].getPlayingXI()[8]
        val striker = teams[1].getPlayingXI()[3]

        val deliveryInput = CricketPhysicsEngine.DeliveryInput(
            deliveryType = DeliveryType.SEAM_UP,
            targetLineOffset = 0.02f,
            targetLengthDistance = 0.45f,
            speedKmh = 138,
            swingAmount = 0.05f,
            spinAmount = 0.0f,
            pitchType = PitchType.GREEN_SEAM,
            weather = WeatherCondition.OVERCAST
        )

        val result = CricketPhysicsEngine.simulateDelivery(
            overNumber = 3,
            ballInOver = 1,
            bowler = bowler,
            striker = striker,
            deliveryInput = deliveryInput,
            battingInput = null,
            fieldPreset = FieldPreset.DEFENSIVE,
            isFreeHitActive = false
        )

        assertTrue(result.ballTrajectoryPoints.isNotEmpty())
        val drs = DRSState(
            isActive = true,
            pitchingInLine = true,
            impactInLine = true,
            hittingWickets = true,
            finalVerdict = DismissalType.LBW
        )
        assertTrue(drs.pitchingInLine)
        assertTrue(drs.hittingWickets)
    }

    @Test
    fun wagonWheel_recordsShotAngles() {
        val shot = WagonWheelShot(
            angleDegrees = 135f,
            distanceMultiplier = 0.85f,
            runs = 4,
            isAerial = false
        )
        assertEquals(135f, shot.angleDegrees, 0.01f)
        assertEquals(4, shot.runs)
        assertFalse(shot.isAerial)
    }

    @Test
    fun physicsEngine_explicitFrontFootNoBallIsDeterministic() {
        val teams = DefaultTeams.getInitialTeams()
        val bowler = teams[0].getPlayingXI()[10]
        val striker = teams[1].getPlayingXI()[0]

        val result = CricketPhysicsEngine.simulateDelivery(
            overNumber = 1,
            ballInOver = 1,
            bowler = bowler,
            striker = striker,
            deliveryInput = CricketPhysicsEngine.DeliveryInput(
                deliveryType = DeliveryType.OUTSWINGER,
                targetLineOffset = 0.1f,
                targetLengthDistance = 0.4f,
                speedKmh = 140,
                swingAmount = 0f,
                spinAmount = 0f,
                pitchType = PitchType.FLAT_BATTING,
                weather = WeatherCondition.SUNNY,
                isFrontFootNoBall = true
            ),
            battingInput = null,
            fieldPreset = FieldPreset.ATTACKING,
            isFreeHitActive = false
        )

        assertTrue(result.outcome.isExtra)
        assertEquals("NB", result.outcome.extraType)
        assertEquals(1, result.outcome.runsScored)
        assertFalse(result.outcome.isWicket)
    }

}
