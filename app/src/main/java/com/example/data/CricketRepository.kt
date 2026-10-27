package com.example.data

import androidx.compose.ui.geometry.Offset
import com.example.data.supabase.*
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

    val supabaseClient = SupabaseClient()
    val supabaseHealth = MutableStateFlow<SupabaseHealthReport?>(null)
    val isSupabaseConnected = MutableStateFlow(false)
    val supabaseAuthSession = MutableStateFlow(SupabaseAuthSession())
    val isUsingRealDatabaseData = MutableStateFlow(false)

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

    // --- Online 1v1 Multiplayer Engine ---
    val userProfile = MutableStateFlow(OnlineUserProfile())
    val currentOnlineRoom = MutableStateFlow<OnlineRoom?>(null)
    val isSearchingMatch = MutableStateFlow(false)

    fun createOnlineRoom(
        format: CricketFormat = CricketFormat.T20,
        overs: Int = 5,
        hostTeam: Team,
        guestTeam: Team,
        venue: StadiumVenue = _venues.value[0]
    ): OnlineRoom {
        val randomNum = (100..999).random()
        val code = "RCP-$randomNum"
        val room = OnlineRoom(
            roomCode = code,
            hostUser = userProfile.value,
            guestUser = null,
            format = format,
            overs = overs,
            venue = venue,
            hostTeam = hostTeam,
            guestTeam = guestTeam,
            state = RoomState.LOBBY,
            isHostReady = true,
            isGuestReady = false
        )
        currentOnlineRoom.value = room
        return room
    }

    fun joinOnlineRoom(code: String): Boolean {
        val trimmed = code.trim().uppercase()
        val opponent = OnlineUserProfile(
            id = "challenger_99",
            displayName = "CricketWarrior",
            countryCode = "UK",
            ratingElo = 1320,
            tier = "Platinum",
            matchesPlayed = 42,
            matchesWon = 31,
            matchesLost = 11,
            winRatePercent = 74
        )
        val room = OnlineRoom(
            roomCode = trimmed,
            hostUser = opponent,
            guestUser = userProfile.value,
            format = CricketFormat.T20,
            overs = 5,
            venue = _venues.value[0],
            hostTeam = _teams.value[1],
            guestTeam = _teams.value[0],
            state = RoomState.READY,
            isHostReady = true,
            isGuestReady = true,
            pingMs = (28..45).random()
        )
        currentOnlineRoom.value = room
        return true
    }

    fun startQuickMatchmaking(onMatched: (OnlineRoom) -> Unit) {
        isSearchingMatch.value = true
        val randomNum = (100..999).random()
        val opponentNames = listOf("BabarStriker", "SpeedStar150", "KingSpin", "YorkerKing", "BoomBoomPower")
        val opponent = OnlineUserProfile(
            id = "rival_$randomNum",
            displayName = opponentNames.random(),
            countryCode = listOf("PK", "IN", "AU", "ENG", "SA").random(),
            ratingElo = userProfile.value.ratingElo + (-40..40).random(),
            tier = "Gold League",
            matchesPlayed = 30,
            matchesWon = 21,
            matchesLost = 9,
            winRatePercent = 70
        )
        val room = OnlineRoom(
            roomCode = "RCP-$randomNum",
            hostUser = opponent,
            guestUser = userProfile.value,
            format = CricketFormat.T20,
            overs = 5,
            venue = _venues.value.random(),
            hostTeam = _teams.value.random(),
            guestTeam = _teams.value.random(),
            state = RoomState.READY,
            isHostReady = true,
            isGuestReady = true,
            pingMs = (32..54).random()
        )
        currentOnlineRoom.value = room
        isSearchingMatch.value = false
        onMatched(room)
    }

    fun sendQuickEmote(emoji: String, textEn: String, textUr: String) {
        val room = currentOnlineRoom.value ?: return
        val emote = QuickEmote(
            id = System.currentTimeMillis().toString(),
            textEn = textEn,
            textUr = textUr,
            emoji = emoji,
            senderName = userProfile.value.displayName
        )
        room.activeEmotes.add(0, emote)
        if (room.activeEmotes.size > 5) {
            room.activeEmotes.removeAt(room.activeEmotes.size - 1)
        }
        currentOnlineRoom.value = room.copy()
    }

    fun leaveOnlineRoom() {
        currentOnlineRoom.value = null
        isSearchingMatch.value = false
    }

    // -------------------------------------------------------------
    // Supabase Backend Connectivity & Synchronization
    // -------------------------------------------------------------

    suspend fun runSupabaseHealthCheck(): SupabaseHealthReport {
        val report = supabaseClient.checkHealth()
        supabaseHealth.value = report
        isSupabaseConnected.value = (report.status == "HEALTHY" || report.status == "READY")
        if (isSupabaseConnected.value) {
            syncWithSupabaseDatabase()
        }
        return report
    }

    suspend fun syncWithSupabaseDatabase() {
        // Requirement 14: Do not use mock data when real database data is available
        val teamsRes = supabaseClient.fetchTeams()
        if (teamsRes.isSuccess && teamsRes.getOrNull()?.isNotEmpty() == true) {
            _teams.value = teamsRes.getOrNull()!!
            isUsingRealDatabaseData.value = true
        }

        val venuesRes = supabaseClient.fetchStadiums()
        if (venuesRes.isSuccess && venuesRes.getOrNull()?.isNotEmpty() == true) {
            _venues.value = venuesRes.getOrNull()!!
        }
    }

    suspend fun loginToSupabase(email: String, pass: String): Result<SupabaseAuthSession> {
        val result = supabaseClient.login(email, pass)
        if (result.isSuccess) {
            val session = result.getOrNull() ?: SupabaseAuthSession()
            supabaseAuthSession.value = session
            // Try loading user profile
            val profRes = supabaseClient.fetchUserProfile()
            if (profRes.isSuccess) {
                userProfile.value = profRes.getOrNull()!!
            }
        }
        return result
    }

    suspend fun signUpToSupabase(email: String, pass: String, displayName: String): Result<SupabaseAuthSession> {
        val result = supabaseClient.signUp(email, pass, displayName)
        if (result.isSuccess) {
            supabaseAuthSession.value = result.getOrNull() ?: SupabaseAuthSession()
        }
        return result
    }

    suspend fun logoutFromSupabase(): Result<Boolean> {
        val result = supabaseClient.logout()
        supabaseAuthSession.value = SupabaseAuthSession()
        return Result.success(result)
    }

    suspend fun recordCompletedMatchToSupabase(
        match: MatchState,
        isPlayerWon: Boolean,
        ratingChange: Int
    ): Result<String> {
        return supabaseClient.saveMatchHistory(match, isPlayerWon, ratingChange)
    }

    suspend fun saveCloudProgress(slotName: String, data: String): Result<Boolean> {
        return supabaseClient.saveCloudSave(slotName, data)
    }
}
