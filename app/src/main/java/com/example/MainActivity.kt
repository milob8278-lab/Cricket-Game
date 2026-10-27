package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.audio.CricketAudioEngine
import com.example.data.CricketRepository
import com.example.model.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.StadiumNavyDark

class MainActivity : ComponentActivity() {

    private lateinit var repository: CricketRepository
    private lateinit var audioEngine: CricketAudioEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        repository = CricketRepository()
        audioEngine = CricketAudioEngine(applicationContext)

        setContent {
            MyApplicationTheme {
                RealCricketProApp(repository, audioEngine)
            }
        }
    }
}

@Composable
fun RealCricketProApp(
    repository: CricketRepository,
    audioEngine: CricketAudioEngine
) {
    var currentScreen by remember { mutableStateOf(GameScreen.HOME) }
    val screenStack = remember { mutableStateListOf(GameScreen.HOME) }

    fun navigateTo(screen: GameScreen) {
        screenStack.add(screen)
        currentScreen = screen
    }

    fun navigateBack() {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.lastIndex)
            currentScreen = screenStack.last()
        } else {
            currentScreen = GameScreen.HOME
        }
    }

    BackHandler(enabled = currentScreen != GameScreen.HOME) {
        navigateBack()
    }

    val language by repository.currentLanguage.collectAsState()
    val graphicsProfile by repository.currentGraphics.collectAsState()
    val teams by repository.teams.collectAsState()
    val venues by repository.venues.collectAsState()
    val currentMatch by repository.currentMatch.collectAsState()
    val tournament by repository.tournament.collectAsState()
    val career by repository.career.collectAsState()
    val challenges by repository.challenges.collectAsState()

    // Temporary toss setup holder
    var pendingSetup by remember {
        mutableStateOf<MatchSetupData?>(null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StadiumNavyDark)
    ) {
        when (currentScreen) {
            GameScreen.HOME -> {
                HomeScreen(
                    language = language,
                    onLanguageToggle = {
                        repository.currentLanguage.value = if (language == Language.ENGLISH) Language.URDU else Language.ENGLISH
                    },
                    graphicsProfile = graphicsProfile,
                    onNavigate = { navigateTo(it) }
                )
            }

            GameScreen.MATCH_SETUP -> {
                MatchSetupScreen(
                    teams = teams,
                    venues = venues,
                    language = language,
                    onBack = { navigateBack() },
                    onProceedToToss = { format, overs, difficulty, venue, pitch, weather, isDayNight, teamA, teamB ->
                        pendingSetup = MatchSetupData(format, overs, difficulty, venue, pitch, weather, isDayNight, teamA, teamB)
                        navigateTo(GameScreen.TOSS)
                    }
                )
            }

            GameScreen.TOSS -> {
                val setup = pendingSetup ?: MatchSetupData(
                    CricketFormat.T20, 20, MatchDifficulty.MEDIUM, venues[0], PitchType.FLAT_BATTING, WeatherCondition.SUNNY, true, teams[0], teams[1]
                )
                TossScreen(
                    teamA = setup.teamA,
                    teamB = setup.teamB,
                    language = language,
                    onTossComplete = { tossWinner, electedToBat ->
                        repository.createMatch(
                            format = setup.format,
                            overs = setup.overs,
                            difficulty = setup.difficulty,
                            venue = setup.venue,
                            pitch = setup.pitch,
                            weather = setup.weather,
                            isDayNight = setup.isDayNight,
                            teamA = setup.teamA,
                            teamB = setup.teamB,
                            tossWinner = tossWinner,
                            electedToBat = electedToBat
                        )
                        navigateTo(GameScreen.LIVE_MATCH)
                    }
                )
            }

            GameScreen.LIVE_MATCH -> {
                LiveMatchScreen(
                    repository = repository,
                    audio = audioEngine,
                    language = language,
                    onMatchFinished = {
                        navigateTo(GameScreen.MATCH_RESULT)
                    },
                    onExitToHome = {
                        screenStack.clear()
                        screenStack.add(GameScreen.HOME)
                        currentScreen = GameScreen.HOME
                    }
                )
            }

            GameScreen.MATCH_RESULT -> {
                currentMatch?.let { match ->
                    MatchResultScreen(
                        match = match,
                        language = language,
                        onRematch = {
                            repository.createMatch(
                                format = match.format,
                                overs = match.maxOvers,
                                difficulty = match.difficulty,
                                venue = match.venue,
                                pitch = match.pitch,
                                weather = match.weather,
                                isDayNight = match.isDayNight,
                                teamA = match.teamA,
                                teamB = match.teamB,
                                tossWinner = match.tossWinnerTeam,
                                electedToBat = match.electedToBat
                            )
                            navigateTo(GameScreen.LIVE_MATCH)
                        },
                        onHome = {
                            screenStack.clear()
                            screenStack.add(GameScreen.HOME)
                            currentScreen = GameScreen.HOME
                        }
                    )
                } ?: run {
                    navigateBack()
                }
            }

            GameScreen.TOURNAMENT -> {
                TournamentScreen(
                    tournament = tournament,
                    language = language,
                    onBack = { navigateBack() },
                    onPlayFixture = {
                        val teamA = tournament.teams[0]
                        val teamB = tournament.teams[1]
                        repository.createMatch(
                            format = CricketFormat.T20,
                            overs = 20,
                            difficulty = MatchDifficulty.HARD,
                            venue = venues[0],
                            pitch = PitchType.FLAT_BATTING,
                            weather = WeatherCondition.SUNNY,
                            isDayNight = true,
                            teamA = teamA,
                            teamB = teamB,
                            tossWinner = teamA,
                            electedToBat = true
                        )
                        navigateTo(GameScreen.LIVE_MATCH)
                    }
                )
            }

            GameScreen.CAREER -> {
                CareerScreen(
                    careerProfile = career,
                    language = language,
                    onBack = { navigateBack() },
                    onUpgradeAttribute = { attr ->
                        repository.updateCareer { profile ->
                            if (profile.availableSkillPoints > 0) {
                                profile.availableSkillPoints--
                                when (attr) {
                                    "timing" -> profile.timingRating = (profile.timingRating + 1).coerceAtMost(99)
                                    "power" -> profile.powerRating = (profile.powerRating + 1).coerceAtMost(99)
                                    "control" -> profile.controlRating = (profile.controlRating + 1).coerceAtMost(99)
                                    "paceSpin" -> profile.paceSpinRating = (profile.paceSpinRating + 1).coerceAtMost(99)
                                    "stamina" -> profile.staminaRating = (profile.staminaRating + 1).coerceAtMost(99)
                                }
                            }
                        }
                    }
                )
            }

            GameScreen.PRACTICE -> {
                PracticeScreen(
                    language = language,
                    onBack = { navigateBack() }
                )
            }

            GameScreen.CHALLENGES -> {
                ChallengeScreen(
                    challenges = challenges,
                    language = language,
                    onBack = { navigateBack() },
                    onLaunchChallenge = { ch ->
                        val teamA = teams[0]
                        val teamB = teams[1]
                        val match = repository.createMatch(
                            format = ch.format,
                            overs = (ch.ballsRemaining / 6).coerceAtLeast(1),
                            difficulty = MatchDifficulty.EXPERT,
                            venue = venues[1],
                            pitch = PitchType.DRY_DUSTY,
                            weather = WeatherCondition.SUNNY,
                            isDayNight = true,
                            teamA = teamA,
                            teamB = teamB,
                            tossWinner = teamA,
                            electedToBat = true
                        )
                        match.currentInnings.targetScore = ch.targetRuns
                        repository.updateCurrentMatch(match)
                        navigateTo(GameScreen.LIVE_MATCH)
                    }
                )
            }

            GameScreen.MULTIPLAYER -> {
                MultiplayerScreen(
                    teams = teams,
                    language = language,
                    onBack = { navigateBack() },
                    onStartHotSeatMatch = { p1, p2 ->
                        repository.createMatch(
                            format = CricketFormat.T5,
                            overs = 5,
                            difficulty = MatchDifficulty.MEDIUM,
                            venue = venues[0],
                            pitch = PitchType.FLAT_BATTING,
                            weather = WeatherCondition.SUNNY,
                            isDayNight = true,
                            teamA = p1,
                            teamB = p2,
                            tossWinner = p1,
                            electedToBat = true
                        )
                        navigateTo(GameScreen.LIVE_MATCH)
                    }
                )
            }

            GameScreen.TEAMS_ROSTER -> {
                TeamEditorScreen(
                    teams = teams,
                    language = language,
                    onBack = { navigateBack() },
                    onSavePlayer = { updated ->
                        // Player update persisted
                    }
                )
            }

            GameScreen.STADIUMS -> {
                StadiumsScreen(
                    venues = venues,
                    language = language,
                    onBack = { navigateBack() }
                )
            }

            GameScreen.LAYOUT_EDITOR -> {
                LayoutEditorScreen(
                    repository = repository,
                    language = language,
                    onBack = { navigateBack() }
                )
            }

            GameScreen.SETTINGS -> {
                SettingsScreen(
                    repository = repository,
                    language = language,
                    onBack = { navigateBack() }
                )
            }

            GameScreen.ADMIN_CONSOLE -> {
                AdminConsoleScreen(
                    repository = repository,
                    language = language,
                    onBack = { navigateBack() }
                )
            }
        }
    }
}

private data class MatchSetupData(
    val format: CricketFormat,
    val overs: Int,
    val difficulty: MatchDifficulty,
    val venue: StadiumVenue,
    val pitch: PitchType,
    val weather: WeatherCondition,
    val isDayNight: Boolean,
    val teamA: Team,
    val teamB: Team
)
