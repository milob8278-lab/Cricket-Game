package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun PracticeScreen(
    language: Language,
    onBack: () -> Unit
) {
    var selectedDrill by remember { mutableIntStateOf(0) } // 0: Batting Nets, 1: Bowling Machine, 2: Fielding
    var bowlingMachineSpeed by remember { mutableIntStateOf(145) }
    var selectedPracticeShot by remember { mutableStateOf(ShotType.COVER_DRIVE) }
    var practiceFeedback by remember { mutableStateOf<String?>(null) }
    var ballsFacedCount by remember { mutableIntStateOf(0) }
    var middleShotCount by remember { mutableIntStateOf(0) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StadiumNavyDark)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
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
                    text = if (language == Language.URDU) "پریکٹس نیٹس اور ٹریننگ" else "PRACTICE & NETS TRAINING",
                    color = TextWhitePrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "Master your timing & shot library with zero pressure",
                    color = CricketGreenLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Drill Selection Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DrillTab(selected = selectedDrill == 0, title = "BATTING NETS") { selectedDrill = 0 }
            DrillTab(selected = selectedDrill == 1, title = "BOWLING DRILL") { selectedDrill = 1 }
            DrillTab(selected = selectedDrill == 2, title = "FIELDING DRILL") { selectedDrill = 2 }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Practice Stats Banner
        Surface(
            color = StadiumNavyCard,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("BALLS FACED", color = TextMutedSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("$ballsFacedCount", color = TextWhitePrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("MIDDLE SHOTS", color = TextMutedSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("$middleShotCount", color = CricketGreenLight, fontSize = 18.sp, fontWeight = FontWeight.Black)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("SWEET SPOT %", color = TextMutedSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    val pct = if (ballsFacedCount > 0) (middleShotCount * 100) / ballsFacedCount else 0
                    Text("$pct%", color = MilestoneGold, fontSize = 18.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bowling Machine Settings
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
            shape = RoundedCornerShape(14.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "BOWLING MACHINE SIMULATOR",
                    color = MilestoneGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Machine Speed", color = TextWhitePrimary, fontSize = 12.sp)
                    Text("$bowlingMachineSpeed km/h", color = MilestoneGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
                }
                Slider(
                    value = bowlingMachineSpeed.toFloat(),
                    onValueChange = { bowlingMachineSpeed = it.toInt() },
                    valueRange = 90f..160f,
                    colors = SliderDefaults.colors(thumbColor = MilestoneGold, activeTrackColor = MilestoneGold)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Practice Specific Shot:", color = TextMutedSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(ShotType.values()) { shot ->
                        val isSelected = shot == selectedPracticeShot
                        Surface(
                            modifier = Modifier.clickable { selectedPracticeShot = shot },
                            color = if (isSelected) CricketGreen else StadiumNavySurface,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = shot.shotNameEn,
                                color = if (isSelected) Color.White else TextMutedSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Trigger Practice Delivery
        Button(
            onClick = {
                ballsFacedCount++
                val roll = Random.nextFloat()
                val isMiddle = roll > 0.35f
                if (isMiddle) middleShotCount++

                practiceFeedback = if (isMiddle) {
                    "PERFECT TIMING! ${selectedPracticeShot.shotNameEn} cleanly dispatched to the boundary fence!"
                } else {
                    "Slightly early on the shot. Work on letting the ball arrive under your eyes."
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("FACE NEXT DELIVERY", fontWeight = FontWeight.Black, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Feedback Result
        practiceFeedback?.let { feedback ->
            Surface(
                color = if (feedback.contains("PERFECT")) CricketGreenDark else StadiumNavySurface,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.SportsScore, contentDescription = null, tint = MilestoneGold)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = feedback,
                        color = TextWhitePrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.DrillTab(selected: Boolean, title: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.weight(1f).height(36.dp),
        color = if (selected) CricketGreen else StadiumNavyCard,
        shape = RoundedCornerShape(8.dp),
        onClick = onClick
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = title,
                color = if (selected) Color.White else TextMutedSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
