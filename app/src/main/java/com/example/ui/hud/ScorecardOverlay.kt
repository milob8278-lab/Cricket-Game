package com.example.ui.hud

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InningsState
import com.example.model.Language
import com.example.model.WagonWheelShot
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ScorecardOverlay(
    innings: InningsState,
    language: Language,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Batting, 1: Bowling, 2: Charts (Wagon Wheel/Manhattan)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xE607111E)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f),
            colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${innings.battingTeam.nameEn} INNINGS SCORECARD",
                            color = TextWhitePrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "${innings.totalRuns}/${innings.wicketsLost} (${innings.currentOversFormatted} Ov) - CRR: ${String.format("%.2f", innings.currentRunRate)}",
                            color = MilestoneGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhitePrimary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Navigation Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TabButton(selected = selectedTab == 0, text = if (language == Language.URDU) "بلے بازی" else "BATTING") { selectedTab = 0 }
                    TabButton(selected = selectedTab == 1, text = if (language == Language.URDU) "گیند بازی" else "BOWLING") { selectedTab = 1 }
                    TabButton(selected = selectedTab == 2, text = if (language == Language.URDU) "ویگن وہیل / چارٹس" else "WAGON WHEEL") { selectedTab = 2 }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tab Content
                when (selectedTab) {
                    0 -> BattingScorecardTab(innings, language)
                    1 -> BowlingScorecardTab(innings, language)
                    2 -> WagonWheelChartTab(innings.wagonWheelShots, language)
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabButton(selected: Boolean, text: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .weight(1f)
            .height(36.dp),
        color = if (selected) CricketGreen else StadiumNavySurface,
        shape = RoundedCornerShape(8.dp),
        onClick = onClick
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (selected) Color.White else TextMutedSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun BattingScorecardTab(innings: InningsState, language: Language) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(StadiumNavySurface, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Text("BATSMAN", color = TextMutedSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2.5f))
            Text("R", color = TextWhitePrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f))
            Text("B", color = TextMutedSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f))
            Text("4s", color = TextMutedSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f))
            Text("6s", color = TextMutedSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f))
            Text("SR", color = MilestoneGold, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.0f))
        }

        Spacer(modifier = Modifier.height(4.dp))

        val xi = innings.battingTeam.getPlayingXI()
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(xi) { player ->
                val stats = player.currentMatchStats
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(2.5f)) {
                        Text(player.name, color = TextWhitePrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(stats.dismissalDescription, color = TextMutedSecondary, fontSize = 9.sp)
                    }
                    Text("${stats.runsScored}", color = MilestoneGold, fontSize = 12.sp, fontWeight = FontWeight.Black, modifier = Modifier.weight(0.7f))
                    Text("${stats.ballsFaced}", color = TextWhitePrimary, fontSize = 11.sp, modifier = Modifier.weight(0.7f))
                    Text("${stats.fours}", color = CricketGreenLight, fontSize = 11.sp, modifier = Modifier.weight(0.6f))
                    Text("${stats.sixes}", color = MilestoneGold, fontSize = 11.sp, modifier = Modifier.weight(0.6f))
                    Text(String.format("%.1f", stats.strikeRate), color = TextMutedSecondary, fontSize = 11.sp, modifier = Modifier.weight(1.0f))
                }
                Divider(color = StadiumBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
            }
        }

        // Extras Summary
        Surface(
            color = StadiumNavySurface,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "EXTRAS: ${innings.extrasWides + innings.extrasNoBalls + innings.extrasByes + innings.extrasLegByes} (wd ${innings.extrasWides}, nb ${innings.extrasNoBalls}, b ${innings.extrasByes}, lb ${innings.extrasLegByes})",
                    color = TextMutedSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = "TOTAL: ${innings.totalRuns}/${innings.wicketsLost}",
                    color = MilestoneGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun BowlingScorecardTab(innings: InningsState, language: Language) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(StadiumNavySurface, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Text("BOWLER", color = TextMutedSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2.5f))
            Text("O", color = TextWhitePrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f))
            Text("M", color = TextMutedSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f))
            Text("R", color = TextWhitePrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f))
            Text("W", color = WicketCrimson, fontSize = 10.sp, fontWeight = FontWeight.Black, modifier = Modifier.weight(0.7f))
            Text("ECON", color = MilestoneGold, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.0f))
        }

        Spacer(modifier = Modifier.height(4.dp))

        val bowlers = innings.bowlingTeam.getPlayingXI().filter { it.currentMatchStats.oversBowled > 0.0 || it.id == innings.currentBowler.id }
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(bowlers) { bowler ->
                val stats = bowler.currentMatchStats
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(bowler.name, color = TextWhitePrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2.5f))
                    Text("${stats.oversBowled}", color = TextWhitePrimary, fontSize = 11.sp, modifier = Modifier.weight(0.7f))
                    Text("${stats.maidens}", color = TextMutedSecondary, fontSize = 11.sp, modifier = Modifier.weight(0.6f))
                    Text("${stats.runsConceded}", color = TextWhitePrimary, fontSize = 11.sp, modifier = Modifier.weight(0.7f))
                    Text("${stats.wicketsTaken}", color = WicketCrimson, fontSize = 12.sp, fontWeight = FontWeight.Black, modifier = Modifier.weight(0.7f))
                    Text(String.format("%.2f", stats.economyRate), color = MilestoneGold, fontSize = 11.sp, modifier = Modifier.weight(1.0f))
                }
                Divider(color = StadiumBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
            }
        }
    }
}

@Composable
private fun WagonWheelChartTab(shots: List<WagonWheelShot>, language: Language) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (language == Language.URDU) "شاٹس ویگن وہیل (360° تجزیہ)" else "360° BATTING WAGON WHEEL",
            color = TextMutedSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .size(220.dp)
                .background(Color(0xFF0F172A), RoundedCornerShape(110.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2
                val cy = size.height / 2
                val boundaryR = size.width * 0.44f
                val innerR = size.width * 0.25f

                // Outfield Boundary Ring
                drawCircle(Color(0xFF1E6F38), radius = boundaryR, center = Offset(cx, cy))
                drawCircle(Color(0x44FFFFFF), radius = boundaryR, center = Offset(cx, cy), style = androidx.compose.ui.graphics.drawscope.Stroke(2f))
                drawCircle(Color(0x33FFFFFF), radius = innerR, center = Offset(cx, cy), style = androidx.compose.ui.graphics.drawscope.Stroke(1.5f))

                // Pitch strip
                drawRect(Color(0xFFC7B198), Offset(cx - 5f, cy - 18f), Size(10f, 36f))

                // Draw all shots from innings
                shots.forEach { shot ->
                    val rad = Math.toRadians(shot.angleDegrees.toDouble())
                    val shotDist = boundaryR * shot.distanceMultiplier
                    val targetX = cx + (shotDist * sin(rad)).toFloat()
                    val targetY = cy - (shotDist * cos(rad)).toFloat()

                    val shotColor = when {
                        shot.runs == 6 -> MilestoneGold
                        shot.runs == 4 -> CricketGreenLight
                        shot.runs > 0 -> Color(0xFF38BDF8)
                        else -> Color(0xFF64748B)
                    }

                    drawLine(
                        color = shotColor,
                        start = Offset(cx, cy),
                        end = Offset(targetX, targetY),
                        strokeWidth = if (shot.runs >= 4) 2.5f else 1.5f
                    )
                    drawCircle(shotColor, radius = 2.5f, center = Offset(targetX, targetY))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Legend
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            LegendItem(MilestoneGold, "6 Runs")
            LegendItem(CricketGreenLight, "4 Runs")
            LegendItem(Color(0xFF38BDF8), "1-3 Runs")
            LegendItem(Color(0xFF64748B), "Dots")
        }
    }
}

@Composable
private fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).background(color, androidx.compose.foundation.shape.CircleShape))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, color = TextWhitePrimary, fontSize = 10.sp)
    }
}
