package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Language
import com.example.model.MatchState
import com.example.ui.hud.ScorecardOverlay
import com.example.ui.theme.*

@Composable
fun MatchResultScreen(
    match: MatchState,
    language: Language,
    onRematch: () -> Unit,
    onHome: () -> Unit
) {
    var showFullScorecard by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val innings1 = match.inningsList.firstOrNull()
    val innings2 = match.inningsList.getOrNull(1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StadiumNavyDark)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Trophy & Celebration Header
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(Color(0x33F59E0B), RoundedCornerShape(36.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.EmojiEvents,
                contentDescription = "Trophy",
                tint = MilestoneGold,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = if (language == Language.URDU) "میچ کا حتمی نتیجہ" else "MATCH PRESENTATION",
            color = MilestoneGold,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (language == Language.URDU) match.matchResultSummaryUr else match.matchResultSummaryEn,
            color = TextWhitePrimary,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Innings 1 & Innings 2 Scorecards Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
            shape = RoundedCornerShape(14.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                innings1?.let { inn1 ->
                    InningsSummaryRow(
                        teamName = inn1.battingTeam.nameEn,
                        score = "${inn1.totalRuns}/${inn1.wicketsLost}",
                        overs = inn1.currentOversFormatted,
                        teamColor = inn1.battingTeam.primaryColor
                    )
                }

                if (innings2 != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = StadiumBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    InningsSummaryRow(
                        teamName = innings2.battingTeam.nameEn,
                        score = "${innings2.totalRuns}/${innings2.wicketsLost}",
                        overs = innings2.currentOversFormatted,
                        teamColor = innings2.battingTeam.primaryColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Player of the Match Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StadiumNavySurface),
            shape = RoundedCornerShape(14.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MilestoneGold))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.player_3d_real),
                    contentDescription = "Player of the Match",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .border(2.dp, MilestoneGold, CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = if (language == Language.URDU) "میچ کا بہترین کھلاڑی (PLAYER OF THE MATCH)" else "PLAYER OF THE MATCH",
                        color = MilestoneGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = innings1?.striker?.name ?: "Babar Zaman",
                        color = TextWhitePrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Match-winning performance with exceptional strike rate",
                        color = TextMutedSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // VIEW FULL DETAILED SCORECARD BUTTON
        OutlinedButton(
            onClick = { showFullScorecard = true },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (language == Language.URDU) "مکمل اسکورکارڈ اور ویگن وہیل دیکھیں" else "VIEW FULL SCORECARD & CHARTS",
                color = CricketGreenLight,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ACTIONS: REMATCH & HOME
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onRematch,
                colors = ButtonDefaults.buttonColors(containerColor = StadiumNavySurface),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Replay, contentDescription = null, tint = TextWhitePrimary)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (language == Language.URDU) "دوبارہ کھیلیں" else "REMATCH",
                    color = TextWhitePrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onHome,
                colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (language == Language.URDU) "ہوم پیج" else "MAIN MENU",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Full Scorecard Overlay
        if (showFullScorecard && innings1 != null) {
            ScorecardOverlay(
                innings = innings2 ?: innings1,
                language = language,
                onDismiss = { showFullScorecard = false }
            )
        }
    }
}

@Composable
private fun InningsSummaryRow(
    teamName: String,
    score: String,
    overs: String,
    teamColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(12.dp).background(teamColor, androidx.compose.foundation.shape.CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text(teamName, color = TextWhitePrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(score, color = MilestoneGold, fontWeight = FontWeight.Black, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text("($overs Ov)", color = TextMutedSecondary, fontSize = 12.sp)
        }
    }
}
