package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

enum class MultiplayerTab(val labelEn: String, val labelUr: String) {
    ONLINE_1V1("Online 1v1 Arena", "آن لائن 1v1 مقابلہ"),
    HOT_SEAT("Hot-Seat Pass & Play", "ہاٹ سیٹ (ایک موبائل)")
}

@Composable
fun MultiplayerScreen(
    teams: List<Team>,
    venues: List<StadiumVenue> = DefaultVenues.getVenues(),
    language: Language,
    onBack: () -> Unit,
    onStartMatch: (teamA: Team, teamB: Team, format: CricketFormat, overs: Int, venue: StadiumVenue, isOnline: Boolean, roomCode: String?) -> Unit
) {
    var selectedTab by remember { mutableStateOf(MultiplayerTab.ONLINE_1V1) }

    // User Profile
    val userProfile = remember {
        OnlineUserProfile(
            id = "usr_babar_10",
            displayName = "CaptainStrike",
            countryCode = "PK",
            ratingElo = 1380,
            tier = "Platinum League",
            matchesPlayed = 46,
            matchesWon = 34,
            matchesLost = 12,
            winRatePercent = 74
        )
    }

    // Online Lobby State
    var roomCodeInput by remember { mutableStateOf("") }
    var activeRoom by remember { mutableStateOf<OnlineRoom?>(null) }
    var isSearchingMatch by remember { mutableStateOf(false) }
    var selectedFormat by remember { mutableStateOf(CricketFormat.T20) }
    var selectedOvers by remember { mutableStateOf(5) }
    var selectedVenue by remember { mutableStateOf(venues[0]) }
    var hostTeam by remember { mutableStateOf(teams[0]) }
    var guestTeam by remember { mutableStateOf(teams[1]) }
    var isHostReady by remember { mutableStateOf(true) }
    var isGuestReady by remember { mutableStateOf(false) }

    // Hot-seat local state
    var localPlayer1Team by remember { mutableStateOf(teams[0]) }
    var localPlayer2Team by remember { mutableStateOf(teams[1]) }

    val scrollState = rememberScrollState()

    // Matchmaking pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "radar"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StadiumNavyDark)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhitePrimary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = if (language == Language.URDU) "ملٹی پلیئر اور آن لائن 1v1" else "MULTIPLAYER & ONLINE 1v1",
                        color = TextWhitePrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                    Text(
                        text = if (language == Language.URDU) "آن لائن پلیئرز یا ایک ہی ڈیوائس پر کھیلیں" else "Real-time 1v1 battles & pass-and-play matches",
                        color = MilestoneGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Network Status Pill
            Surface(
                color = CricketGreen.copy(alpha = 0.2f),
                shape = RoundedCornerShape(20.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(CricketGreen, Color(0xFF10B981))))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF22C55E))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ONLINE • 38ms",
                        color = Color(0xFF22C55E),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tab Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(StadiumNavyCard)
                .padding(4.dp)
        ) {
            MultiplayerTab.values().forEach { tab ->
                val isSelected = (selectedTab == tab)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) CricketGreen else Color.Transparent)
                        .clickable { selectedTab = tab }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (language == Language.URDU) tab.labelUr else tab.labelEn,
                        color = if (isSelected) Color.White else TextMutedSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            MultiplayerTab.ONLINE_1V1 -> {
                // User Profile Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF1E3A8A), Color(0xFF0F172A))))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0284C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.SportsCricket, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = userProfile.displayName,
                                        color = TextWhitePrimary,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(color = MilestoneGold, shape = RoundedCornerShape(4.dp)) {
                                        Text(
                                            text = userProfile.countryCode,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 9.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${userProfile.tier} • Elo Rating ${userProfile.ratingElo}",
                                    color = MilestoneGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${userProfile.matchesWon}W - ${userProfile.matchesLost}L",
                                color = TextWhitePrimary,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "${userProfile.winRatePercent}% Win Rate",
                                color = CricketGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // If not in a room: Show Matchmaking & Room Creation
                if (activeRoom == null && !isSearchingMatch) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Quick Match Button
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    isSearchingMatch = true
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F3E22)),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(CricketGreen, Color(0xFF10B981))))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = MilestoneGold, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (language == Language.URDU) "فوری آن لائن میچ" else "QUICK MATCH 1v1",
                                    color = TextWhitePrimary,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Auto matchmaking queue",
                                    color = TextMutedSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        // Create Private Room
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    val code = "RCP-${(100..999).random()}"
                                    activeRoom = OnlineRoom(
                                        roomCode = code,
                                        hostUser = userProfile,
                                        guestUser = null,
                                        format = selectedFormat,
                                        overs = selectedOvers,
                                        venue = selectedVenue,
                                        hostTeam = hostTeam,
                                        guestTeam = guestTeam,
                                        state = RoomState.LOBBY,
                                        isHostReady = true,
                                        isGuestReady = false
                                    )
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(Color(0xFF2563EB), Color(0xFF1D4ED8))))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.AddCircleOutline, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (language == Language.URDU) "پرائیویٹ روم بنائیں" else "CREATE ROOM",
                                    color = TextWhitePrimary,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Invite a friend with code",
                                    color = TextMutedSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Join Room with Code Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(StadiumBorder, Color(0xFF334155))))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (language == Language.URDU) "روم کوڈ کے ذریعے شامل ہوں" else "JOIN WITH ROOM CODE",
                                color = TextWhitePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = roomCodeInput,
                                    onValueChange = { if (it.length <= 7) roomCodeInput = it.uppercase() },
                                    placeholder = { Text("e.g. RCP-408", color = TextMutedSecondary, fontSize = 13.sp) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = TextWhitePrimary,
                                        unfocusedTextColor = TextWhitePrimary,
                                        focusedBorderColor = MilestoneGold,
                                        unfocusedBorderColor = StadiumBorder
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Button(
                                    onClick = {
                                        if (roomCodeInput.isNotBlank()) {
                                            val opponent = OnlineUserProfile(
                                                id = "challenger_77",
                                                displayName = "YorkerMaster",
                                                countryCode = "ENG",
                                                ratingElo = 1340,
                                                tier = "Platinum",
                                                matchesPlayed = 50,
                                                matchesWon = 36,
                                                matchesLost = 14,
                                                winRatePercent = 72
                                            )
                                            activeRoom = OnlineRoom(
                                                roomCode = roomCodeInput,
                                                hostUser = opponent,
                                                guestUser = userProfile,
                                                format = selectedFormat,
                                                overs = selectedOvers,
                                                venue = selectedVenue,
                                                hostTeam = teams[1],
                                                guestTeam = teams[0],
                                                state = RoomState.READY,
                                                isHostReady = true,
                                                isGuestReady = true
                                            )
                                        }
                                    },
                                    enabled = roomCodeInput.isNotBlank(),
                                    colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("JOIN", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                // Matchmaking Radar State
                if (isSearchingMatch) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.radialGradient(listOf(MilestoneGold, Color.Transparent)))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .background(CricketGreen.copy(alpha = pulseAlpha)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = MilestoneGold, modifier = Modifier.size(50.dp), strokeWidth = 3.dp)
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = if (language == Language.URDU) "مخالف کھلاڑی کی تلاش جاری ہے..." else "Searching for Online Opponent...",
                                color = TextWhitePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Matching by skill rating (${userProfile.ratingElo} Elo) • Ping < 45ms",
                                color = TextMutedSecondary,
                                fontSize = 11.sp
                            )

                            Spacer(modifier = Modifier.height(18.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedButton(
                                    onClick = {
                                        // Simulated quick match connection
                                        val opponent = OnlineUserProfile(
                                            id = "rival_babar",
                                            displayName = "BoomBoomAfridi",
                                            countryCode = "PK",
                                            ratingElo = 1395,
                                            tier = "Platinum",
                                            matchesPlayed = 62,
                                            matchesWon = 48,
                                            matchesLost = 14,
                                            winRatePercent = 77
                                        )
                                        activeRoom = OnlineRoom(
                                            roomCode = "RCP-${(100..999).random()}",
                                            hostUser = userProfile,
                                            guestUser = opponent,
                                            format = CricketFormat.T20,
                                            overs = 5,
                                            venue = selectedVenue,
                                            hostTeam = teams[0],
                                            guestTeam = teams[1],
                                            state = RoomState.READY,
                                            isHostReady = true,
                                            isGuestReady = true
                                        )
                                        isSearchingMatch = false
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MilestoneGold)
                                ) {
                                    Text("CONNECT NOW", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }

                                TextButton(onClick = { isSearchingMatch = false }) {
                                    Text("CANCEL", color = Color(0xFFEF4444), fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }

                // Active Online Room Lobby
                activeRoom?.let { room ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(MilestoneGold, Color(0xFF10B981))))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            // Room Code Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "ROOM CODE",
                                        color = TextMutedSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = room.roomCode,
                                        color = MilestoneGold,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 24.sp
                                    )
                                }

                                TextButton(
                                    onClick = { activeRoom = null },
                                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEF4444))
                                ) {
                                    Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("LEAVE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            HorizontalDivider(color = StadiumBorder, modifier = Modifier.padding(vertical = 12.dp))

                            // Players Versus
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Host Card
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Surface(color = CricketGreen, shape = RoundedCornerShape(6.dp)) {
                                        Text("HOST", color = Color.White, fontWeight = FontWeight.Black, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(room.hostUser.displayName, color = TextWhitePrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(if (language == Language.URDU) room.hostTeam.nameUr else room.hostTeam.nameEn, color = MilestoneGold, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Surface(
                                        color = Color(0xFF10B981).copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text("READY", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }

                                Text("VS", color = TextMutedSecondary, fontWeight = FontWeight.Black, fontSize = 16.sp)

                                // Guest Card
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Surface(color = Color(0xFF2563EB), shape = RoundedCornerShape(6.dp)) {
                                        Text("CHALLENGER", color = Color.White, fontWeight = FontWeight.Black, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(room.guestUser?.displayName ?: "Waiting...", color = if (room.guestUser != null) TextWhitePrimary else TextMutedSecondary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(if (language == Language.URDU) room.guestTeam.nameUr else room.guestTeam.nameEn, color = MilestoneGold, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    if (room.guestUser != null) {
                                        Surface(
                                            color = Color(0xFF10B981).copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text("READY", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    } else {
                                        Button(
                                            onClick = {
                                                val challenger = OnlineUserProfile(
                                                    id = "challenger_guest",
                                                    displayName = "FastBowler99",
                                                    countryCode = "AU",
                                                    ratingElo = 1360,
                                                    tier = "Gold",
                                                    matchesPlayed = 33,
                                                    matchesWon = 24,
                                                    matchesLost = 9,
                                                    winRatePercent = 72
                                                )
                                                room.guestUser = challenger
                                                room.isGuestReady = true
                                                activeRoom = room.copy(guestUser = challenger, isGuestReady = true)
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text("+ Add Bot/Guest", fontSize = 10.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Quick Emotes Bar
                            Text("QUICK IN-GAME EMOTES", color = TextMutedSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(listOf("🔥 What a ball!", "👏 Well played!", "🏏 Let's play!", "☝️ How's that!")) { emoteText ->
                                    Surface(
                                        color = Color(0xFF1E293B),
                                        shape = RoundedCornerShape(12.dp),
                                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(StadiumBorder, Color(0xFF475569)))),
                                        modifier = Modifier.clickable {
                                            val emote = QuickEmote("1", emoteText, emoteText, emoteText.take(2), userProfile.displayName)
                                            room.activeEmotes.add(0, emote)
                                        }
                                    ) {
                                        Text(emoteText, color = TextWhitePrimary, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Start Match Button
                            Button(
                                onClick = {
                                    val guest = room.guestUser ?: OnlineUserProfile(displayName = "Challenger")
                                    onStartMatch(
                                        room.hostTeam,
                                        room.guestTeam,
                                        room.format,
                                        room.overs,
                                        selectedVenue,
                                        true,
                                        room.roomCode
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (language == Language.URDU) "آن لائن 1v1 میچ شروع کریں" else "START ONLINE 1v1 MATCH",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.SportsCricket, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            MultiplayerTab.HOT_SEAT -> {
                // Hot Seat Local Setup
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(MilestoneGold, Color(0xFF0F766E))))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.People, contentDescription = null, tint = MilestoneGold, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == Language.URDU) "ہاٹ سیٹ: ایک ہی موبائل پر 2 کھلاڑی" else "HOT-SEAT PASS & PLAY RULES",
                                color = TextWhitePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (language == Language.URDU) "کھلاڑی 1 پہلے بیٹنگ کرے گا جبکہ کھلاڑی 2 بولنگ کرے گا۔ اننگز کے وقفے پر موبائل ایک دوسرے کو دیں اور ٹارگٹ کا دفاع کریں!" else "Player 1 bats first while Player 2 bowls. At innings break, pass the phone over to defend the target! 100% offline & seamless.",
                            color = TextMutedSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Teams selection for hot seat
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Player 1
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = StadiumNavyCard)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("PLAYER 1 TEAM", color = MilestoneGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            teams.take(4).forEach { t ->
                                val isSelected = (localPlayer1Team == t)
                                Surface(
                                    color = if (isSelected) CricketGreen else Color(0xFF1E293B),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .clickable { localPlayer1Team = t }
                                ) {
                                    Text(
                                        text = if (language == Language.URDU) t.nameUr else t.nameEn,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Player 2
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = StadiumNavyCard)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("PLAYER 2 TEAM", color = Color(0xFF60A5FA), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            teams.drop(2).take(4).forEach { t ->
                                val isSelected = (localPlayer2Team == t)
                                Surface(
                                    color = if (isSelected) Color(0xFF2563EB) else Color(0xFF1E293B),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .clickable { localPlayer2Team = t }
                                ) {
                                    Text(
                                        text = if (language == Language.URDU) t.nameUr else t.nameEn,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onStartMatch(
                            localPlayer1Team,
                            localPlayer2Team,
                            CricketFormat.T20,
                            5,
                            selectedVenue,
                            false,
                            null
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (language == Language.URDU) "ہاٹ سیٹ میچ شروع کریں" else "START HOT-SEAT MATCH",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
