package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.model.Team
import com.example.ui.theme.*

@Composable
fun MultiplayerScreen(
    teams: List<Team>,
    language: Language,
    onBack: () -> Unit,
    onStartHotSeatMatch: (player1Team: Team, player2Team: Team) -> Unit
) {
    var player1Team by remember { mutableStateOf(teams[0]) }
    var player2Team by remember { mutableStateOf(teams[1]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StadiumNavyDark)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        // App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhitePrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = if (language == Language.URDU) "دو کھلاڑیوں کا مقابلہ (ہاٹ سیٹ)" else "HOT-SEAT 2-PLAYER PASS & PLAY",
                    color = TextWhitePrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "Play with a friend on the same device — 100% offline",
                    color = MilestoneGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.People, contentDescription = null, tint = MilestoneGold, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "HOW HOT-SEAT WORKS",
                        color = TextWhitePrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Player 1 bats first while Player 2 bowls. At the innings break, pass the phone over to switch roles and defend the target! No internet or accounts needed.",
                    color = TextMutedSecondary,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Team selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Player 1 Team
            Column(modifier = Modifier.weight(1f)) {
                Text("PLAYER 1 TEAM", color = MilestoneGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                teams.take(4).forEach { team ->
                    val isSelected = team.id == player1Team.id
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { player1Team = team }
                            .padding(vertical = 3.dp),
                        color = if (isSelected) team.primaryColor.copy(alpha = 0.35f) else StadiumNavyCard,
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) team.primaryColor else StadiumBorder))
                    ) {
                        Text(team.nameEn, color = TextWhitePrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(10.dp))
                    }
                }
            }

            // Player 2 Team
            Column(modifier = Modifier.weight(1f)) {
                Text("PLAYER 2 TEAM", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                teams.drop(1).take(4).forEach { team ->
                    val isSelected = team.id == player2Team.id
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { player2Team = team }
                            .padding(vertical = 3.dp),
                        color = if (isSelected) team.primaryColor.copy(alpha = 0.35f) else StadiumNavyCard,
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) team.primaryColor else StadiumBorder))
                    ) {
                        Text(team.nameEn, color = TextWhitePrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(10.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onStartHotSeatMatch(player1Team, player2Team) },
            colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == Language.URDU) "ہاٹ سیٹ مقابلہ شروع کریں" else "START 2-PLAYER PASS & PLAY",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            )
        }
    }
}
