package com.example.ui.hud

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BowlingArchetype
import com.example.model.DeliveryType
import com.example.model.Language
import com.example.model.Player
import com.example.ui.theme.*

@Composable
fun BowlingControls(
    modifier: Modifier = Modifier,
    bowler: Player,
    language: Language,
    selectedDelivery: DeliveryType,
    onDeliverySelected: (DeliveryType) -> Unit,
    targetLineOffset: Float, // -1.0 (leg) to 1.0 (off)
    targetLengthDistance: Float, // 0.0 (yorker) to 1.0 (bouncer)
    onTargetChanged: (line: Float, length: Float) -> Unit,
    speedKmh: Int,
    onSpeedChange: (Int) -> Unit,
    swingSpinAmount: Float, // -1.0 to 1.0
    onSwingSpinChange: (Float) -> Unit,
    onStartDeliveryClick: () -> Unit,
    onAppealClick: () -> Unit,
    isDelivering: Boolean
) {
    // Deliveries available for this bowler archetype
    val availableDeliveries = remember(bowler.bowlingArchetype) {
        when (bowler.bowlingArchetype) {
            BowlingArchetype.FAST, BowlingArchetype.LEFT_ARM_PACE -> listOf(
                DeliveryType.OUTSWINGER,
                DeliveryType.INSWINGER,
                DeliveryType.YORKER,
                DeliveryType.BOUNCER,
                DeliveryType.SLOWER_BALL,
                DeliveryType.CUTTER,
                DeliveryType.SEAM_UP
            )
            BowlingArchetype.MEDIUM -> listOf(
                DeliveryType.SEAM_UP,
                DeliveryType.INSWINGER,
                DeliveryType.OUTSWINGER,
                DeliveryType.SLOWER_BALL,
                DeliveryType.KNUCKLE_BALL
            )
            BowlingArchetype.OFF_SPIN -> listOf(
                DeliveryType.OFF_BREAK,
                DeliveryType.DOOSRA,
                DeliveryType.CARROM_BALL,
                DeliveryType.ARM_BALL,
                DeliveryType.TOP_SPINNER
            )
            BowlingArchetype.LEG_SPIN -> listOf(
                DeliveryType.LEG_BREAK,
                DeliveryType.GOOGLY,
                DeliveryType.FLIPPER,
                DeliveryType.TOP_SPINNER
            )
            BowlingArchetype.LEFT_ARM_SPIN -> listOf(
                DeliveryType.ARM_BALL,
                DeliveryType.OFF_BREAK,
                DeliveryType.TOP_SPINNER
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        // TOP: Delivery Type Selector Row
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(availableDeliveries) { delivery ->
                val isSelected = delivery == selectedDelivery
                Surface(
                    modifier = Modifier.clickable { onDeliverySelected(delivery) },
                    color = if (isSelected) CricketGreen else StadiumNavySurface,
                    shape = RoundedCornerShape(8.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) CricketGreenLight else StadiumBorder))
                ) {
                    Text(
                        text = if (language == Language.URDU) delivery.labelUr else delivery.labelEn,
                        color = if (isSelected) Color.White else TextWhitePrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // MAIN CONTROL ROW: Interactive Pitch Landing Target, Speed/Swing sliders, Start Delivery & Appeal
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Interactive Pitch Target Grid (Drag / Tap line and length)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (language == Language.URDU) "لینڈنگ مارکر" else "LANDING TARGET",
                    color = TextMutedSecondary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(width = 110.dp, height = 75.dp)
                        .background(Color(0xFFC7B198), RoundedCornerShape(8.dp)) // Pitch Clay
                        .border(1.5.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                val normX = ((offset.x / size.width) - 0.5f) * 2f // -1.0 to 1.0
                                val normY = (offset.y / size.height) // 0.0 to 1.0
                                onTargetChanged(normX.coerceIn(-1f, 1f), normY.coerceIn(0f, 1f))
                            }
                        }
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Crease markings
                        drawLine(Color.White, Offset(0f, size.height * 0.15f), Offset(size.width, size.height * 0.15f), strokeWidth = 2f)
                        drawLine(Color.White, Offset(0f, size.height * 0.85f), Offset(size.width, size.height * 0.85f), strokeWidth = 2f)

                        // 3 Stumps at batsman end
                        val cx = size.width * 0.5f
                        drawCircle(Color(0xFFFFB300), radius = 3f, center = Offset(cx - 8f, size.height * 0.88f))
                        drawCircle(Color(0xFFFFB300), radius = 3f, center = Offset(cx, size.height * 0.88f))
                        drawCircle(Color(0xFFFFB300), radius = 3f, center = Offset(cx + 8f, size.height * 0.88f))

                        // Selected Landing Marker Target Ring
                        val markerX = ((targetLineOffset / 2f) + 0.5f) * size.width
                        val markerY = targetLengthDistance * size.height
                        drawCircle(WicketCrimson, radius = 9f, center = Offset(markerX, markerY), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5f))
                        drawCircle(MilestoneGold, radius = 3.5f, center = Offset(markerX, markerY))
                    }
                }
            }

            // 2. Pace & Swing Sliders
            Column(
                modifier = Modifier
                    .width(160.dp)
                    .background(StadiumNavySurface.copy(alpha = 0.8f), RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                // Speed Meter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("SPEED", color = TextMutedSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("$speedKmh km/h", color = MilestoneGold, fontSize = 11.sp, fontWeight = FontWeight.Black)
                }
                Slider(
                    value = speedKmh.toFloat(),
                    onValueChange = { onSpeedChange(it.toInt()) },
                    valueRange = 85f..155f,
                    colors = SliderDefaults.colors(
                        thumbColor = MilestoneGold,
                        activeTrackColor = MilestoneGold
                    ),
                    modifier = Modifier.height(24.dp)
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Swing / Spin Curve
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (bowler.bowlingArchetype.name.contains("SPIN")) "SPIN TURN" else "SWING CURVE",
                        color = TextMutedSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (swingSpinAmount > 0.1f) "OUT (+)" else if (swingSpinAmount < -0.1f) "IN (-)" else "STRAIGHT",
                        color = CricketGreenLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = swingSpinAmount,
                    onValueChange = { onSwingSpinChange(it) },
                    valueRange = -1.0f..1.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = CricketGreenLight,
                        activeTrackColor = CricketGreenLight
                    ),
                    modifier = Modifier.height(24.dp)
                )
            }

            // 3. START DELIVERY BUTTON & APPEAL BUTTON
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // APPEAL BUTTON ("HOWZAT!")
                Button(
                    onClick = onAppealClick,
                    colors = ButtonDefaults.buttonColors(containerColor = WicketCrimson),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.height(52.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Campaign, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Text("HOWZAT!", fontWeight = FontWeight.Black, fontSize = 10.sp)
                    }
                }

                // START RUN-UP & BOWL
                Button(
                    onClick = onStartDeliveryClick,
                    enabled = !isDelivering,
                    colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(52.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Text(
                            text = if (isDelivering) "BOWLING..." else "BOWL!",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
