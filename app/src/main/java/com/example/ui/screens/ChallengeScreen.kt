package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CricketChallenge
import com.example.model.Language
import com.example.ui.theme.*

@Composable
fun ChallengeScreen(
    challenges: List<CricketChallenge>,
    language: Language,
    onBack: () -> Unit,
    onLaunchChallenge: (CricketChallenge) -> Unit
) {
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
                    text = if (language == Language.URDU) "سنسنی خیز کرکٹ چیلنجز" else "SITUATIONAL CRICKET CHALLENGES",
                    color = TextWhitePrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "High-pressure death overs, miracle chases and spin traps",
                    color = MilestoneGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(challenges) { challenge ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(if (challenge.isCompleted) CricketGreen else StadiumBorder)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == Language.URDU) challenge.titleUr else challenge.titleEn,
                                color = TextWhitePrimary,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp
                            )
                            // Star rating
                            Row {
                                repeat(challenge.starDifficulty) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = MilestoneGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (language == Language.URDU) challenge.scenarioUr else challenge.scenarioEn,
                            color = TextMutedSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Match situation pills
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(color = StadiumNavySurface, shape = RoundedCornerShape(6.dp)) {
                                Text("NEED: ${challenge.targetRuns} RUNS", color = CricketGreenLight, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                            }
                            Surface(color = StadiumNavySurface, shape = RoundedCornerShape(6.dp)) {
                                Text("BALLS: ${challenge.ballsRemaining}", color = TextWhitePrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                            }
                            Surface(color = StadiumNavySurface, shape = RoundedCornerShape(6.dp)) {
                                Text("WKTS: ${challenge.wicketsInHand}", color = WicketCrimson, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { onLaunchChallenge(challenge) },
                            colors = ButtonDefaults.buttonColors(containerColor = if (challenge.isCompleted) StadiumNavySurface else CricketGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.FlashOn, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (challenge.isCompleted) "REPLAY SCENARIO" else "PLAY CHALLENGE",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
