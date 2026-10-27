package com.example.ui.hud

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsCricket
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

@Composable
fun BattingControls(
    modifier: Modifier = Modifier,
    language: Language,
    selectedFootwork: Footwork,
    onFootworkChange: (Footwork) -> Unit,
    selectedIntent: BattingIntent,
    onIntentChange: (BattingIntent) -> Unit,
    selectedShot: ShotType,
    onShotChange: (ShotType) -> Unit,
    onPlayShotClick: (intent: BattingIntent, shot: ShotType) -> Unit,
    onRunCallClick: (callType: String) -> Unit,
    timingMeterProgress: Float, // 0.0 to 1.0 (0.5 is sweet spot)
    isBallInFlight: Boolean
) {
    var showShotMapDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        // Timing Window Indicator (Appears when ball is in flight from bowler)
        if (isBallInFlight) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .align(Alignment.CenterHorizontally)
                    .height(18.dp)
                    .background(Color(0xFF0F172A), RoundedCornerShape(9.dp))
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(9.dp))
            ) {
                // Sweet spot zone (0.45 to 0.55)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.18f)
                        .align(Alignment.Center)
                        .background(CricketGreen.copy(alpha = 0.6f))
                )
                // Traveling ball timing marker
                val markerX = (timingMeterProgress.coerceIn(0f, 1f) * 100).toInt()
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .offset(x = (markerX * 2.5f).dp)
                        .background(MilestoneGold, CircleShape)
                        .border(1.5.dp, Color.White, CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Lower Control HUD
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // LEFT ZONE: Footwork Selector & Running Calls
            Column(modifier = Modifier.width(170.dp)) {
                // Footwork Chips
                Surface(
                    color = StadiumNavySurface.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {
                        Text(
                            text = if (language == Language.URDU) "پیروں کی پوزیشن" else "FOOTWORK",
                            color = TextMutedSecondary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            FootworkPill(Footwork.FRONT_FOOT, selectedFootwork == Footwork.FRONT_FOOT, "FRONT", "آگے") { onFootworkChange(Footwork.FRONT_FOOT) }
                            FootworkPill(Footwork.BACK_FOOT, selectedFootwork == Footwork.BACK_FOOT, "BACK", "پیچھے") { onFootworkChange(Footwork.BACK_FOOT) }
                            FootworkPill(Footwork.ADVANCE, selectedFootwork == Footwork.ADVANCE, "CHARGE", "حملہ") { onFootworkChange(Footwork.ADVANCE) }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Running Cluster: YES, NO, WAIT
                Surface(
                    color = StadiumNavySurface.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { onRunCallClick("YES") },
                            colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("RUN!", fontWeight = FontWeight.Black, fontSize = 11.sp)
                        }
                        Button(
                            onClick = { onRunCallClick("NO") },
                            colors = ButtonDefaults.buttonColors(containerColor = WicketCrimson),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("NO!", fontWeight = FontWeight.Black, fontSize = 11.sp)
                        }
                        Button(
                            onClick = { onRunCallClick("WAIT") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64748B)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("WAIT", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        }
                    }
                }
            }

            // CENTER: Selected Shot & 360 Shot Map Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.clickable { showShotMapDialog = true },
                    color = StadiumNavySurface.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MilestoneGold))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.SportsCricket, contentDescription = null, tint = MilestoneGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = if (language == Language.URDU) selectedShot.shotNameUr else selectedShot.shotNameEn,
                                color = TextWhitePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = if (language == Language.URDU) "شاٹ تبدیل کریں 360°" else "TAP FOR 360° SHOT MAP",
                                color = MilestoneGold,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            // RIGHT ZONE: Batting Action Triggers (DEFEND, NORMAL, LOFT, POWER)
            Column(horizontalAlignment = Alignment.End) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // LOFT
                    ActionTriggerButton(
                        labelEn = "LOFT",
                        labelUr = "ہوا میں",
                        color = Color(0xFF38BDF8),
                        onClick = {
                            onIntentChange(BattingIntent.LOFT)
                            onPlayShotClick(BattingIntent.LOFT, selectedShot)
                        }
                    )
                    // POWER HIT
                    ActionTriggerButton(
                        labelEn = "POWER",
                        labelUr = "طاقتور",
                        color = MilestoneGold,
                        isHighEnergy = true,
                        onClick = {
                            onIntentChange(BattingIntent.POWER)
                            onPlayShotClick(BattingIntent.POWER, selectedShot)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // DEFEND
                    ActionTriggerButton(
                        labelEn = "DEFEND",
                        labelUr = "دفاع",
                        color = Color(0xFF64748B),
                        onClick = {
                            onIntentChange(BattingIntent.DEFEND)
                            onPlayShotClick(BattingIntent.DEFEND, selectedShot)
                        }
                    )
                    // NORMAL DRIVE
                    ActionTriggerButton(
                        labelEn = "NORMAL",
                        labelUr = "معمول",
                        color = CricketGreenLight,
                        onClick = {
                            onIntentChange(BattingIntent.NORMAL)
                            onPlayShotClick(BattingIntent.NORMAL, selectedShot)
                        }
                    )
                }
            }
        }
    }

    // 360-degree Interactive Shot Map Dialog
    if (showShotMapDialog) {
        AlertDialog(
            onDismissRequest = { showShotMapDialog = false },
            confirmButton = {
                TextButton(onClick = { showShotMapDialog = false }) {
                    Text("DONE", color = MilestoneGold, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text(
                    text = if (language == Language.URDU) "360° ڈائریکشنل شاٹ سلیکٹر" else "360° SHOT SELECTION MAP",
                    color = TextWhitePrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                ) {
                    Text(
                        text = if (language == Language.URDU) "اپنا پسندیدہ کرکٹ شاٹ منتخب کریں:" else "Select desired stroke around the field:",
                        color = TextMutedSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val allShots = ShotType.values()
                    androidx.compose.foundation.lazy.LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(allShots.size) { idx ->
                            val shot = allShots[idx]
                            val isSelected = shot == selectedShot
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onShotChange(shot)
                                        showShotMapDialog = false
                                    },
                                color = if (isSelected) CricketGreenDark else StadiumNavySurface,
                                shape = RoundedCornerShape(8.dp),
                                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) CricketGreenLight else StadiumBorder))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = if (language == Language.URDU) shot.shotNameUr else shot.shotNameEn,
                                            color = TextWhitePrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "${shot.defaultAngleDegrees.toInt()}° Direction",
                                            color = TextMutedSecondary,
                                            fontSize = 10.sp
                                        )
                                    }
                                    if (shot.isAerialDefault) {
                                        Surface(color = Color(0x33F59E0B), shape = RoundedCornerShape(4.dp)) {
                                            Text("AERIAL", color = MilestoneGold, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            containerColor = StadiumNavyCard,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun FootworkPill(
    footwork: Footwork,
    isSelected: Boolean,
    labelEn: String,
    labelUr: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        color = if (isSelected) CricketGreen else Color.Transparent,
        shape = RoundedCornerShape(6.dp),
        border = if (!isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF475569))) else null
    ) {
        Text(
            text = labelEn,
            color = if (isSelected) Color.White else TextMutedSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun ActionTriggerButton(
    labelEn: String,
    labelUr: String,
    color: Color,
    isHighEnergy: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(width = 68.dp, height = 44.dp)
            .clickable { onClick() },
        color = color,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = if (isHighEnergy) 6.dp else 2.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = labelEn,
                color = Color.Black,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp
            )
        }
    }
}
