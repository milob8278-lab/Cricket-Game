package com.example.ui.hud

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InningsState
import com.example.model.Language
import com.example.model.MatchState
import com.example.ui.field.CameraAngle
import com.example.ui.theme.*

@Composable
fun MatchTopRibbon(
    match: MatchState,
    innings: InningsState,
    language: Language,
    currentCamera: CameraAngle,
    onCycleCamera: () -> Unit,
    onPauseClick: () -> Unit,
    onDrsClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        color = StadiumNavy.copy(alpha = 0.94f),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder)),
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team & Score Indicator
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(innings.battingTeam.primaryColor, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = innings.battingTeam.shortCode,
                        color = TextWhitePrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "${innings.totalRuns}/${innings.wicketsLost}",
                        color = MilestoneGold,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Surface(
                        color = StadiumNavySurface,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "OVERS ${innings.currentOversFormatted} / ${innings.maxOvers}",
                            color = TextWhitePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                // Rates & Target
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "CRR: ${String.format("%.2f", innings.currentRunRate)}",
                        color = TextMutedSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    innings.targetScore?.let { target ->
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TARGET: $target",
                            color = CricketGreenLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        innings.requiredRunRate?.let { rrr ->
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "RRR: ${String.format("%.2f", rrr)}",
                                color = WicketCrimson,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Controls & Camera
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // DRS Trigger button
                    Surface(
                        modifier = Modifier
                            .clickable { onDrsClick() }
                            .padding(end = 6.dp),
                        color = DRSElectricBlue.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(6.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DRSElectricBlue))
                    ) {
                        Text(
                            text = "DRS (2)",
                            color = DRSElectricBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Camera Switch
                    IconButton(
                        onClick = onCycleCamera,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Camera Angle",
                            tint = TextWhitePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Pause
                    IconButton(
                        onClick = onPauseClick,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pause,
                            contentDescription = "Pause Game",
                            tint = TextWhitePrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // Striker, Non-Striker & Bowler Live Info Strip
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Batsmen on pitch
                Row {
                    Text(
                        text = "★ ${innings.striker.name}: ${innings.striker.currentMatchStats.runsScored} (${innings.striker.currentMatchStats.ballsFaced})",
                        color = MilestoneGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "${innings.nonStriker.name}: ${innings.nonStriker.currentMatchStats.runsScored} (${innings.nonStriker.currentMatchStats.ballsFaced})",
                        color = TextMutedSecondary,
                        fontSize = 11.sp
                    )
                }

                // Current Bowler figures
                val bStats = innings.currentBowler.currentMatchStats
                Text(
                    text = "BOWLER: ${innings.currentBowler.name} ${bStats.wicketsTaken}/${bStats.runsConceded} (${bStats.oversBowled})",
                    color = CricketGreenLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
