package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.model.Team
import com.example.ui.theme.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.random.Random

@Composable
fun TossScreen(
    teamA: Team,
    teamB: Team,
    language: Language,
    onTossComplete: (winner: Team, electedToBat: Boolean) -> Unit
) {
    var userCall by remember { mutableStateOf<String?>("HEADS") } // "HEADS" or "TAILS"
    var isFlipping by remember { mutableStateOf(false) }
    var coinResult by remember { mutableStateOf<String?>(null) }
    var tossWinner by remember { mutableStateOf<Team?>(null) }
    var electedToBat by remember { mutableStateOf<Boolean?>(null) }

    // Coin 3D rotation animation
    val coinRotation = remember { Animatable(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StadiumNavyDark)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                color = MilestoneGold.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (language == Language.URDU) "آفیشل ٹاس تقریب" else "OFFICIAL TOSS CEREMONY",
                    color = MilestoneGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${teamA.nameEn} vs ${teamB.nameEn}",
                color = TextWhitePrimary,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp
            )
        }

        // Center: 3D Animated Coin
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2
                val cy = size.height / 2
                val radius = size.width * 0.42f
                val rot = coinRotation.value
                val scaleX = abs(cos(Math.toRadians(rot.toDouble()))).toFloat().coerceAtLeast(0.08f)

                // 3D Coin Shadow
                drawOval(
                    color = Color(0x66000000),
                    topLeft = Offset(cx - radius * 0.9f, size.height * 0.92f),
                    size = androidx.compose.ui.geometry.Size(radius * 1.8f, 16f)
                )

                // Gold Coin Face
                drawOval(
                    color = Color(0xFFF59E0B),
                    topLeft = Offset(cx - radius * scaleX, cy - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2 * scaleX, radius * 2)
                )
                drawOval(
                    color = Color(0xFFFDE68A),
                    topLeft = Offset(cx - radius * 0.85f * scaleX, cy - radius * 0.85f),
                    size = androidx.compose.ui.geometry.Size(radius * 1.7f * scaleX, radius * 1.7f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
                )
            }

            // Coin Text indicator
            if (!isFlipping) {
                Text(
                    text = coinResult ?: (userCall ?: "HEADS"),
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            }
        }

        // Bottom Controls: Call selection, flip trigger or Bat/Bowl choice
        if (tossWinner == null) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (language == Language.URDU) "ٹاس کی کال منتخب کریں:" else "Captain's Call:",
                    color = TextMutedSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { userCall = "HEADS" },
                        colors = ButtonDefaults.buttonColors(containerColor = if (userCall == "HEADS") MilestoneGold else StadiumNavySurface),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("HEADS", color = if (userCall == "HEADS") Color.Black else TextWhitePrimary, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { userCall = "TAILS" },
                        colors = ButtonDefaults.buttonColors(containerColor = if (userCall == "TAILS") MilestoneGold else StadiumNavySurface),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("TAILS", color = if (userCall == "TAILS") Color.Black else TextWhitePrimary, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Flip Coin Button
                Button(
                    onClick = {
                        isFlipping = true
                        // Launch coroutine animation
                        val wonCall = Random.nextBoolean()
                        val finalSide = if (wonCall) userCall ?: "HEADS" else if (userCall == "HEADS") "TAILS" else "HEADS"
                        coinResult = finalSide

                        // Start coin rotation
                        kotlinx.coroutines.GlobalScope.apply {
                            // Local rotation handling inside compose is safer with launched effect
                        }
                        tossWinner = if (wonCall) teamA else teamB
                        isFlipping = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(50.dp)
                ) {
                    Text(
                        text = if (language == Language.URDU) "سکہ اچھالیں (FLIP COIN)" else "SPIN THE COIN",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            // Toss Decision Screen
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = CricketGreenDark,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (language == Language.URDU) "${tossWinner?.nameUr} نے ٹاس جیت لیا!" else "${tossWinner?.nameEn?.uppercase()} WON THE TOSS!",
                            color = MilestoneGold,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (tossWinner?.id == teamA.id) {
                                if (language == Language.URDU) "آپ کا کیا فیصلہ ہے؟" else "What is your decision, Captain?"
                            } else {
                                val aiElectedBat = Random.nextBoolean()
                                electedToBat = aiElectedBat
                                if (aiElectedBat) "Opponent elected to BAT first" else "Opponent elected to BOWL first"
                            },
                            color = TextWhitePrimary,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (tossWinner?.id == teamA.id) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                electedToBat = true
                                onTossComplete(tossWinner!!, true)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Text(
                                text = if (language == Language.URDU) "پہلے بیٹنگ کریں" else "BAT FIRST",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = {
                                electedToBat = false
                                onTossComplete(tossWinner!!, false)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Text(
                                text = if (language == Language.URDU) "پہلے بولنگ کریں" else "BOWL FIRST",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            onTossComplete(tossWinner!!, electedToBat ?: true)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = if (language == Language.URDU) "میچ میں داخل ہوں" else "ENTER MATCH",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                    }
                }
            }
        }
    }
}
