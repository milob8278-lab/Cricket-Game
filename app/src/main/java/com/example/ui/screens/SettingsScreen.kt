package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CricketRepository
import com.example.model.ControlScheme
import com.example.model.GraphicsProfile
import com.example.model.Language
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    repository: CricketRepository,
    language: Language,
    onBack: () -> Unit
) {
    val currentGraphics by repository.currentGraphics.collectAsState()
    val currentControlScheme by repository.currentControlScheme.collectAsState()
    val isSoundEnabled by repository.isSoundEnabled.collectAsState()
    val isHapticsEnabled by repository.isHapticsEnabled.collectAsState()
    val targetFps by repository.targetFps.collectAsState()

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
                    text = if (language == Language.URDU) "گیم کی ترتیبات اور سیٹنگز" else "GAME SETTINGS & ENGINE",
                    color = TextWhitePrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "Graphics profiles, audio, controls and bilingual localization",
                    color = MilestoneGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 1. GRAPHICS ENGINE PROFILE
        Text("GRAPHICS VISUAL ENGINE", color = MilestoneGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            GraphicsProfile.values().forEach { profile ->
                val isSelected = profile == currentGraphics
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { repository.currentGraphics.value = profile },
                    color = if (isSelected) CricketGreenDark else StadiumNavyCard,
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) CricketGreenLight else StadiumBorder)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { repository.currentGraphics.value = profile },
                            colors = RadioButtonDefaults.colors(selectedColor = CricketGreenLight)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(profile.displayNameEn, color = TextWhitePrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(
                                text = when (profile) {
                                    GraphicsProfile.HIGH_3D -> "Full dynamic lighting, animated crowd, 60 FPS on high-tier devices."
                                    GraphicsProfile.BALANCED_3D -> "Balanced textures and shaders, solid 30 FPS target."
                                    GraphicsProfile.LITE_3D -> "Optimized 4GB RAM baseline mode, low memory load, zero lag."
                                },
                                color = TextMutedSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 2. FRAME RATE TARGET
        Text("TARGET FRAME RATE", color = MilestoneGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf(30 to "30 FPS (Battery Friendly)", 60 to "60 FPS (Silky Smooth)").forEach { (fps, label) ->
                val isSelected = fps == targetFps
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { repository.targetFps.value = fps },
                    color = if (isSelected) CricketGreen else StadiumNavyCard,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color.White else TextMutedSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 3. LANGUAGE (URDU / ENGLISH)
        Text("LANGUAGE & LOCALIZATION", color = MilestoneGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Language.values().forEach { lang ->
                val isSelected = lang == repository.currentLanguage.collectAsState().value
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { repository.currentLanguage.value = lang },
                    color = if (isSelected) Color(0xFF1E40AF) else StadiumNavyCard,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "${lang.displayName} (${lang.code.uppercase()})",
                        color = if (isSelected) Color.White else TextMutedSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 4. CONTROL SCHEMES
        Text("TOUCH CONTROL SCHEME", color = MilestoneGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ControlScheme.values().forEach { scheme ->
                val isSelected = scheme == currentControlScheme
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { repository.currentControlScheme.value = scheme },
                    color = if (isSelected) StadiumNavySurface else StadiumNavyCard,
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) MilestoneGold else StadiumBorder)
                    )
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = isSelected, onClick = { repository.currentControlScheme.value = scheme })
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(scheme.displayNameEn, color = TextWhitePrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = when (scheme) {
                                    ControlScheme.BUTTONS -> "Defend, Normal, Loft and Power buttons on screen"
                                    ControlScheme.SWIPE -> "Swipe finger towards shot direction for intuitive gameplay"
                                    ControlScheme.JOYSTICK_BUTTONS -> "Virtual joystick for shot placement and action triggers"
                                },
                                color = TextMutedSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 5. AUDIO & HAPTICS
        Text("AUDIO & FEEDBACK", color = MilestoneGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = StadiumNavyCard,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sound Effects (Bat Crack, Crowd, Fanfare)", color = TextWhitePrimary, fontSize = 12.sp)
                    Switch(checked = isSoundEnabled, onCheckedChange = { repository.isSoundEnabled.value = it })
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = StadiumBorder)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Haptic Vibration (Impact feedback)", color = TextWhitePrimary, fontSize = 12.sp)
                    Switch(checked = isHapticsEnabled, onCheckedChange = { repository.isHapticsEnabled.value = it })
                }
            }
        }
    }
}
