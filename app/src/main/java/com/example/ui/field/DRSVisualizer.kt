package com.example.ui.field

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DRSState
import com.example.model.DismissalType
import com.example.model.Language
import com.example.ui.theme.*

@Composable
fun DRSVisualizer(
    drsState: DRSState,
    language: Language,
    onCompleteReview: (isOut: Boolean) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        // Automatically progress through DRS TV review sequence
        kotlinx.coroutines.delay(1200)
        currentStep = 1 // Pitching
        kotlinx.coroutines.delay(1200)
        currentStep = 2 // Impact
        kotlinx.coroutines.delay(1200)
        currentStep = 3 // Hitting Wickets
        kotlinx.coroutines.delay(1400)
        currentStep = 4 // Verdict
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xE607111E)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DRSElectricBlue))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == Language.URDU) "تھرڈ امپائر ڈی آر ایس ریویو" else "DECISION REVIEW SYSTEM (DRS)",
                        color = DRSElectricBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Surface(
                        color = DRSElectricBlue.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "BALL TRACKING 3D",
                            color = DRSElectricBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ball-Tracking & Snickometer 3D Display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFF030712), RoundedCornerShape(12.dp))
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Pitch perspective
                        val pitchPath = Path().apply {
                            moveTo(w * 0.15f, h * 0.85f)
                            lineTo(w * 0.85f, h * 0.85f)
                            lineTo(w * 0.65f, h * 0.25f)
                            lineTo(w * 0.35f, h * 0.25f)
                            close()
                        }
                        drawPath(pitchPath, Color(0xFF1E293B))

                        // 3 Stumps Target
                        val stumpH = 70f
                        val stumpW = 6f
                        val spacing = 12f
                        val stumpX = w * 0.5f
                        val stumpY = h * 0.25f

                        for (i in -1..1) {
                            drawRoundRect(
                                color = if (currentStep >= 3 && drsState.hittingWickets) WicketCrimson else Color(0xFFFFB300),
                                topLeft = Offset(stumpX + i * spacing - stumpW / 2, stumpY - stumpH),
                                size = Size(stumpW, stumpH)
                            )
                        }

                        // Ultra-Edge / Snicko waveform strip at bottom
                        val waveColor = if (drsState.snickoContactDetected) WicketCrimson else DRSElectricBlue
                        for (i in 0..60) {
                            val x = (w / 60f) * i
                            val spike = if (drsState.snickoContactDetected && i in 25..35) {
                                (30f - Math.abs(i - 30) * 5f)
                            } else {
                                (i % 3) * 3f
                            }
                            drawLine(
                                color = waveColor,
                                start = Offset(x, h * 0.94f - spike),
                                end = Offset(x, h * 0.94f + spike),
                                strokeWidth = 2f
                            )
                        }

                        // Projected Ball Trajectory
                        val trajectoryPath = Path().apply {
                            moveTo(w * 0.5f, h * 0.80f)
                            quadraticTo(
                                w * 0.5f + (if (drsState.pitchingInLine) 0f else 30f),
                                h * 0.55f,
                                stumpX + (if (drsState.hittingWickets) 0f else 50f),
                                stumpY - stumpH * 0.6f
                            )
                        }
                        drawPath(
                            trajectoryPath,
                            color = Color(0xFF38BDF8),
                            style = Stroke(width = 4f, cap = StrokeCap.Round)
                        )

                        // Impact Point circle
                        if (currentStep >= 2) {
                            drawCircle(
                                color = if (drsState.impactInLine) CricketGreen else Color(0xFFF59E0B),
                                radius = 10f,
                                center = Offset(w * 0.5f, h * 0.55f)
                            )
                        }
                    }

                    // Snicko label
                    Text(
                        text = if (drsState.snickoContactDetected) "ULTRA-EDGE: SPIKE DETECTED" else "ULTRA-EDGE: NO BAT CONTACT",
                        color = if (drsState.snickoContactDetected) WicketCrimson else DRSElectricBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // DRS Stage Verdicts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    VerdictPill(
                        label = if (language == Language.URDU) "پچنگ" else "PITCHING",
                        status = if (currentStep >= 1) if (drsState.pitchingInLine) "IN LINE" else "OUTSIDE" else "CHECKING...",
                        isPositive = drsState.pitchingInLine,
                        isEvaluated = currentStep >= 1
                    )
                    VerdictPill(
                        label = if (language == Language.URDU) "امپیکٹ" else "IMPACT",
                        status = if (currentStep >= 2) if (drsState.impactInLine) "IN LINE" else "OUTSIDE" else "PENDING",
                        isPositive = drsState.impactInLine,
                        isEvaluated = currentStep >= 2
                    )
                    VerdictPill(
                        label = if (language == Language.URDU) "وکٹس" else "WICKETS",
                        status = if (currentStep >= 3) if (drsState.hittingWickets) "HITTING" else "MISSING" else "PENDING",
                        isPositive = drsState.hittingWickets,
                        isEvaluated = currentStep >= 3
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Final Verdict announcement
                if (currentStep >= 4) {
                    val isOut = drsState.finalVerdict != DismissalType.NONE
                    Surface(
                        color = if (isOut) WicketCrimson else CricketGreen,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isOut) {
                                    if (language == Language.URDU) "فیصلہ برقرار: آؤٹ!" else "DECISION UPHELD: OUT!"
                                } else {
                                    if (language == Language.URDU) "فیصلہ تبدیل: ناٹ آؤٹ!" else "DECISION OVERTURNED: NOT OUT!"
                                },
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                            Text(
                                text = if (language == Language.URDU) "تھرڈ امپائر کا حتمی سگنل" else "Third Umpire communication complete",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { onCompleteReview(isOut) },
                        colors = ButtonDefaults.buttonColors(containerColor = DRSElectricBlue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (language == Language.URDU) "میچ میں واپس جائیں" else "RESUME LIVE MATCH",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VerdictPill(
    label: String,
    status: String,
    isPositive: Boolean,
    isEvaluated: Boolean
) {
    Surface(
        color = if (!isEvaluated) StadiumNavySurface else if (isPositive) CricketGreenDark.copy(alpha = 0.3f) else WicketCrimsonDark.copy(alpha = 0.3f),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (!isEvaluated) StadiumBorder else if (isPositive) CricketGreen else WicketCrimson
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, color = TextMutedSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(
                text = status,
                color = if (!isEvaluated) TextMutedSecondary else if (isPositive) CricketGreenLight else WicketCrimson,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
