package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
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
import com.example.model.TournamentState
import com.example.ui.theme.*

@Composable
fun TournamentScreen(
    tournament: TournamentState,
    language: Language,
    onBack: () -> Unit,
    onPlayFixture: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Points Table, 1: Fixtures

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
                    text = if (language == Language.URDU) tournament.nameUr else tournament.nameEn,
                    color = TextWhitePrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "T20 Super League Championship",
                    color = MilestoneGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier.weight(1f).height(38.dp),
                color = if (selectedTab == 0) CricketGreen else StadiumNavyCard,
                shape = RoundedCornerShape(8.dp),
                onClick = { selectedTab = 0 }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (language == Language.URDU) "پوائنٹس ٹیبل" else "POINTS TABLE",
                        color = if (selectedTab == 0) Color.White else TextMutedSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Surface(
                modifier = Modifier.weight(1f).height(38.dp),
                color = if (selectedTab == 1) CricketGreen else StadiumNavyCard,
                shape = RoundedCornerShape(8.dp),
                onClick = { selectedTab = 1 }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (language == Language.URDU) "شیڈول اور فکسچرز" else "FIXTURES & SCHEDULE",
                        color = if (selectedTab == 1) Color.White else TextMutedSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == 0) {
            // Points Table
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(StadiumNavySurface, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text("TEAM", color = TextMutedSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2.5f))
                Text("P", color = TextWhitePrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f))
                Text("W", color = CricketGreenLight, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f))
                Text("L", color = WicketCrimson, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f))
                Text("PTS", color = MilestoneGold, fontSize = 10.sp, fontWeight = FontWeight.Black, modifier = Modifier.weight(0.8f))
                Text("NRR", color = TextMutedSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.0f))
            }

            Spacer(modifier = Modifier.height(6.dp))

            val teamsList = tournament.teams
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(teamsList.size) { idx ->
                    val team = teamsList[idx]
                    val pts = (5 - idx).coerceAtLeast(1) * 2
                    val nrr = 0.85 - (idx * 0.32)

                    Surface(
                        color = StadiumNavyCard,
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(modifier = Modifier.weight(2.5f), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(10.dp).background(team.primaryColor, androidx.compose.foundation.shape.CircleShape))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(team.nameEn, color = TextWhitePrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                            }
                            Text("5", color = TextWhitePrimary, fontSize = 11.sp, modifier = Modifier.weight(0.6f))
                            Text("${(5 - idx).coerceAtLeast(1)}", color = CricketGreenLight, fontSize = 11.sp, modifier = Modifier.weight(0.6f))
                            Text("${idx.coerceAtMost(4)}", color = WicketCrimson, fontSize = 11.sp, modifier = Modifier.weight(0.6f))
                            Text("$pts", color = MilestoneGold, fontSize = 12.sp, fontWeight = FontWeight.Black, modifier = Modifier.weight(0.8f))
                            Text(String.format("%+.2f", nrr), color = TextMutedSecondary, fontSize = 11.sp, modifier = Modifier.weight(1.0f))
                        }
                    }
                }
            }
        } else {
            // Fixtures Schedule
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tournament.teams.size / 2) { idx ->
                    val tA = tournament.teams[idx * 2]
                    val tB = tournament.teams[(idx * 2) + 1]

                    Card(
                        colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
                        shape = RoundedCornerShape(10.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Match ${idx + 1} - Super League", color = MilestoneGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("${tA.nameEn} vs ${tB.nameEn}", color = TextWhitePrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Qaddafi Arena Lahore", color = TextMutedSecondary, fontSize = 10.sp)
                            }

                            Button(
                                onClick = onPlayFixture,
                                colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("PLAY", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Play Current Fixture Action
        Button(
            onClick = onPlayFixture,
            colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == Language.URDU) "اگلا ٹورنامنٹ میچ کھیلیں" else "PLAY NEXT LEAGUE MATCH",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            )
        }
    }
}
