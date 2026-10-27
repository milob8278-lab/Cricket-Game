package com.example.data

import androidx.compose.ui.geometry.Offset
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LayoutButtonConfig(
    val id: String,
    val labelEn: String,
    val labelUr: String,
    var offsetFraction: Offset, // (x, y) relative to screen
    var scale: Float = 1.0f,
    var opacity: Float = 0.9f
)

data class AdminContentRelease(
    val version: String,
    val releaseDate: String,
    val title: String,
    val isPublished: Boolean,
    val description: String
)

class CricketRepository {

    private val _teams = MutableStateFlow<List<Team>>(DefaultTeams.getInitialTeams())
    val teams: StateFlow<List<Team>> = _teams.asStateFlow()

    private val _venues = MutableStateFlow<List<StadiumVenue>>(DefaultVenues.getVenues())
    val venues: StateFlow<List<StadiumVenue>> = _venues.asStateFlow()

    private val _currentMatch = MutableStateFlow<MatchState?>(null)
    val currentMatch: StateFlow<MatchState?> = _currentMatch.asStateFlow()

    private val _tournament = MutableStateFlow<TournamentState>(
        TournamentState(teams = DefaultTeams.getInitialTeams().take(6))
    )
    val tournament: StateFlow<TournamentState> = _tournament.asStateFlow()

    private val _career = MutableStateFlow<CareerProfile>(CareerProfile())
    val career: StateFlow<CareerProfile> = _career.asStateFlow()

    private val _challenges = MutableStateFlow<List<CricketChallenge>>(DefaultChallenges.getChallenges())
    val challenges: StateFlow<List<CricketChallenge>> = _challenges.asStateFlow()

    // Settings
    val currentLanguage = MutableStateFlow(Language.ENGLISH)
    val currentGraphics = MutableStateFlow(GraphicsProfile.HIGH_3D)
    val currentControlScheme = MutableStateFlow(ControlScheme.BUTTONS)
    val isLeftHandedLayout = MutableStateFlow(false)
    val isSoundEnabled = MutableStateFlow(true)
    val isHapticsEnabled = MutableStateFlow(true)
    val targetFps = MutableStateFlow(60)

    // Layout configuration for touch HUD
    val layoutConfigs = MutableStateFlow(
        listOf(
            LayoutButtonConfig("defend_btn", "Defend", "دفاع", Offset(0.72f, 0.85f), 1.0f, 0.9f),
            LayoutButtonConfig("normal_btn", "Normal", "معمول", Offset(0.85f, 0.85f), 1.0f, 0.9f),
            LayoutButtonConfig("loft_btn", "Loft", "ہوا میں", Offset(0.72f, 0.70f), 1.0f, 0.9f),
            LayoutButtonConfig("power_btn", "Power", "طاقتور", Offset(0.85f, 0.70f), 1.1f, 0.95f),
            LayoutButtonConfig("shot_map_btn", "Shot Map", "شاٹ میپ", Offset(0.92f, 0.52f), 1.0f, 0.9f),
            LayoutButtonConfig("footwork_btn", "Footwork", "پیروں کی حرکت", Offset(0.08f, 0.75f), 1.0f, 0.9f),
            LayoutButtonConfig("run_cluster", "Run Calls", "رن کالز", Offset(0.18f, 0.85f), 1.0f, 0.95f)
        )
    )

    // Admin Logs and Content Packs
    val adminReleases = MutableStateFlow(
        listOf(
            AdminContentRelease("v1.0-RC", "2026-10-26", "Global Championship Roster & 4GB Lite Optimization", true, "Official 8 fictional franchises, 6 venues, DRS, Full-Screen landscape HUD."),
            AdminContentRelease("v1.1-Beta", "2026-11-15", "The Hundred Ball Count & Caribbean League", false, "Staged tournament pack with 100-ball countdown logic and dynamic pitch wear.")
        )
    )

    fun createMatch(
        format: CricketFormat,
        overs: Int,
        difficulty: MatchDifficulty,
        venue: StadiumVenue,
        pitch: PitchType,
        weather: WeatherCondition,
        isDayNight: Boolean,
        teamA: Team,
        teamB: Team,
        tossWinner: Team,
        electedToBat: Boolean
    ): MatchState {
        val battingFirst = if (electedToBat) tossWinner else if (tossWinner.id == teamA.id) teamB else teamA
        val bowlingFirst = if (battingFirst.id == teamA.id) teamB else teamA

        val firstInnings = InningsState(
            inningsNumber = 1,
            battingTeam = battingFirst,
            bowlingTeam = bowlingFirst,
            maxOvers = overs
        )

        val match = MatchState(
            matchId = "match_${System.currentTimeMillis()}",
            format = format,
            maxOvers = overs,
            difficulty = difficulty,
            venue = venue,
            pitch = pitch,
            weather = weather,
            isDayNight = isDayNight,
            teamA = teamA,
            teamB = teamB,
            tossWinnerTeam = tossWinner,
            electedToBat = electedToBat,
            inningsList = mutableListOf(firstInnings),
            currentInningsIndex = 0
        )
        _currentMatch.value = match
        return match
    }

    fun startSecondInnings(target: Int) {
        val match = _currentMatch.value ?: return
        val current = match.currentInnings
        val newBattingTeam = current.bowlingTeam
        val newBowlingTeam = current.battingTeam

        val secondInnings = InningsState(
            inningsNumber = 2,
            battingTeam = newBattingTeam,
            bowlingTeam = newBowlingTeam,
            maxOvers = match.maxOvers,
            targetScore = target
        )
        match.inningsList.add(secondInnings)
        match.currentInningsIndex = 1
        _currentMatch.value = match
    }

    fun updateCurrentMatch(updated: MatchState) {
        _currentMatch.value = updated
    }

    fun updateCareer(updater: (CareerProfile) -> Unit) {
        val profile = _career.value
        updater(profile)
        _career.value = profile.copy()
    }

    fun completeChallenge(challengeId: String, stars: Int) {
        val list = _challenges.value.map {
            if (it.id == challengeId) it.copy(isCompleted = true, starsEarned = stars.coerceAtLeast(it.starsEarned)) else it
        }
        _challenges.value = list
    }

    fun resetLayoutToDefault() {
        layoutConfigs.value = listOf(
            LayoutButtonConfig("defend_btn", "Defend", "دفاع", Offset(0.72f, 0.85f), 1.0f, 0.9f),
            LayoutButtonConfig("normal_btn", "Normal", "معمول", Offset(0.85f, 0.85f), 1.0f, 0.9f),
            LayoutButtonConfig("loft_btn", "Loft", "ہوا میں", Offset(0.72f, 0.70f), 1.0f, 0.9f),
            LayoutButtonConfig("power_btn", "Power", "طاقتور", Offset(0.85f, 0.70f), 1.1f, 0.95f),
            LayoutButtonConfig("shot_map_btn", "Shot Map", "شاٹ میپ", Offset(0.92f, 0.52f), 1.0f, 0.9f),
            LayoutButtonConfig("footwork_btn", "Footwork", "پیروں کی حرکت", Offset(0.08f, 0.75f), 1.0f, 0.9f),
            LayoutButtonConfig("run_cluster", "Run Calls", "رن کالز", Offset(0.18f, 0.85f), 1.0f, 0.95f)
        )
    }
}
