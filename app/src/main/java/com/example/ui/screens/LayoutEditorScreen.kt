package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CricketRepository
import com.example.data.LayoutButtonConfig
import com.example.model.Language
import com.example.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun LayoutEditorScreen(
    repository: CricketRepository,
    language: Language,
    onBack: () -> Unit
) {
    val layoutConfigs by repository.layoutConfigs.collectAsState()
    val isLeftHanded by repository.isLeftHandedLayout.collectAsState()

    var selectedButtonId by remember { mutableStateOf<String?>(layoutConfigs.firstOrNull()?.id) }
    val currentSelected = layoutConfigs.find { it.id == selectedButtonId }

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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhitePrimary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = if (language == Language.URDU) "ٹچ کنٹرول لے آؤٹ ایڈیٹر" else "TOUCH HUD LAYOUT EDITOR",
                        color = TextWhitePrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "Drag buttons, adjust button scale and opacity",
                        color = MilestoneGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            IconButton(onClick = { repository.resetLayoutToDefault() }) {
                Icon(Icons.Default.RestartAlt, contentDescription = "Reset Layout", tint = MilestoneGold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Left Hand Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(StadiumNavyCard, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Left-Handed Batting & Fielder Layout", color = TextWhitePrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Switch(
                checked = isLeftHanded,
                onCheckedChange = { repository.isLeftHandedLayout.value = it }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Interactive Canvas preview with movable draggable control elements
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF030712), RoundedCornerShape(14.dp))
                .border(1.5.dp, StadiumBorder, RoundedCornerShape(14.dp))
        ) {
            val maxW = maxWidth
            val maxH = maxHeight

            Text(
                text = "DRAG BUTTONS TO CUSTOMIZE POSITIONS",
                color = Color(0x44FFFFFF),
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Center)
            )

            layoutConfigs.forEach { config ->
                val isSelected = config.id == selectedButtonId
                var dragOffset by remember { mutableStateOf(Offset.Zero) }

                Box(
                    modifier = Modifier
                        .offset {
                            val baseX = config.offsetFraction.x * maxW.toPx()
                            val baseY = config.offsetFraction.y * maxH.toPx()
                            IntOffset(
                                (baseX + dragOffset.x).roundToInt().coerceIn(0, (maxW.toPx() - 150).toInt()),
                                (baseY + dragOffset.y).roundToInt().coerceIn(0, (maxH.toPx() - 100).toInt())
                            )
                        }
                        .pointerInput(config.id) {
                            detectDragGestures(
                                onDragStart = { selectedButtonId = config.id },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    dragOffset += dragAmount
                                }
                            )
                        }
                        .background(
                            color = if (isSelected) CricketGreen.copy(alpha = config.opacity) else StadiumNavySurface.copy(alpha = config.opacity),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MilestoneGold else Color(0xFF64748B),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = (12 * config.scale).dp, vertical = (8 * config.scale).dp)
                ) {
                    Text(
                        text = config.labelEn,
                        color = TextWhitePrimary,
                        fontSize = (11 * config.scale).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Selected Button Controls: Scale and Opacity
        currentSelected?.let { sel ->
            Surface(
                color = StadiumNavyCard,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Selected: ${sel.labelEn}", color = MilestoneGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Scale", color = TextWhitePrimary, fontSize = 11.sp)
                        Text(String.format("%.1fx", sel.scale), color = CricketGreenLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = sel.scale,
                        onValueChange = { sel.scale = it },
                        valueRange = 0.8f..1.5f,
                        colors = SliderDefaults.colors(thumbColor = CricketGreenLight, activeTrackColor = CricketGreenLight)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Opacity", color = TextWhitePrimary, fontSize = 11.sp)
                        Text("${(sel.opacity * 100).toInt()}%", color = MilestoneGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = sel.opacity,
                        onValueChange = { sel.opacity = it },
                        valueRange = 0.4f..1.0f,
                        colors = SliderDefaults.colors(thumbColor = MilestoneGold, activeTrackColor = MilestoneGold)
                    )
                }
            }
        }
    }
}
