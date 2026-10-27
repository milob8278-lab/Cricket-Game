package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.CareerProfile
import com.example.model.Language
import com.example.ui.theme.*

@Composable
fun CareerScreen(
    careerProfile: CareerProfile,
    language: Language,
    onBack: () -> Unit,
    onUpgradeAttribute: (attributeName: String) -> Unit
) {
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
                    text = if (language == Language.URDU) "کیریئر مینیجر اور پروفائل" else "CRICKETER CAREER MODE",
                    color = TextWhitePrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "${careerProfile.clubTeamName} Franchise Star",
                    color = MilestoneGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Hero Cricketer Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MilestoneGold))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.player_3d_real),
                        contentDescription = "Real 3D Cricketer",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, MilestoneGold, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(careerProfile.playerName, color = TextWhitePrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(color = MilestoneGold, shape = RoundedCornerShape(4.dp)) {
                                Text("LVL ${careerProfile.level}", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                            }
                        }
                        Text("${careerProfile.playerRole.labelEn} • ${careerProfile.battingHand.labelEn}", color = TextMutedSecondary, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // XP Progress Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("XP PROGRESS", color = TextMutedSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("${careerProfile.currentXp} / ${careerProfile.xpToNextLevel} XP", color = MilestoneGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { (careerProfile.currentXp.toFloat() / careerProfile.xpToNextLevel).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = CricketGreenLight,
                    trackColor = StadiumNavySurface
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Career Statistics Strip
        Text(
            text = if (language == Language.URDU) "کیریئر ریکارڈز اور اعداد و شمار" else "CAREER STATS & RECORDS",
            color = TextMutedSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatBox("MATCHES", "${careerProfile.matchesPlayed}", Modifier.weight(1f))
            StatBox("RUNS", "${careerProfile.totalRuns}", Modifier.weight(1f))
            StatBox("HIGH SCORE", "${careerProfile.highestScore}", Modifier.weight(1f))
            StatBox("WICKETS", "${careerProfile.totalWickets}", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Attribute Upgrades Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == Language.URDU) "مہارتوں کی اپ گریڈیشن" else "SKILL ATTRIBUTES UPGRADE",
                color = TextMutedSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Surface(color = Color(0x33F59E0B), shape = RoundedCornerShape(6.dp)) {
                Text(
                    text = "${careerProfile.availableSkillPoints} PTS AVAILABLE",
                    color = MilestoneGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AttributeRow("Batting Timing", careerProfile.timingRating, careerProfile.availableSkillPoints > 0) {
                    onUpgradeAttribute("timing")
                }
                AttributeRow("Power Hitting", careerProfile.powerRating, careerProfile.availableSkillPoints > 0) {
                    onUpgradeAttribute("power")
                }
                AttributeRow("Bowling Control", careerProfile.controlRating, careerProfile.availableSkillPoints > 0) {
                    onUpgradeAttribute("control")
                }
                AttributeRow("Pace & Spin", careerProfile.paceSpinRating, careerProfile.availableSkillPoints > 0) {
                    onUpgradeAttribute("paceSpin")
                }
                AttributeRow("Stamina & Fitness", careerProfile.staminaRating, careerProfile.availableSkillPoints > 0) {
                    onUpgradeAttribute("stamina")
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Milestones
        Text(
            text = if (language == Language.URDU) "کیریئر سنگ میل اور کامیابیاں" else "CAREER MILESTONES",
            color = TextMutedSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            careerProfile.milestones.forEach { milestone ->
                Surface(
                    color = StadiumNavyCard,
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(if (milestone.isAchieved) CricketGreen else StadiumBorder)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (milestone.isAchieved) Icons.Default.CheckCircle else Icons.Default.MilitaryTech,
                            contentDescription = null,
                            tint = if (milestone.isAchieved) CricketGreenLight else TextMutedSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == Language.URDU) milestone.titleUr else milestone.titleEn,
                                color = TextWhitePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (language == Language.URDU) milestone.descriptionUr else milestone.descriptionEn,
                                color = TextMutedSecondary,
                                fontSize = 10.sp
                            )
                        }
                        Surface(color = StadiumNavySurface, shape = RoundedCornerShape(4.dp)) {
                            Text("+${milestone.rewardXp} XP", color = MilestoneGold, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(60.dp),
        color = StadiumNavyCard,
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, color = MilestoneGold, fontSize = 16.sp, fontWeight = FontWeight.Black)
            Text(label, color = TextMutedSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AttributeRow(name: String, rating: Int, canUpgrade: Boolean, onUpgrade: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(2f)) {
            Text(name, color = TextWhitePrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            LinearProgressIndicator(
                progress = { (rating / 100f).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth(0.9f).height(6.dp),
                color = if (rating >= 85) MilestoneGold else CricketGreenLight,
                trackColor = StadiumNavySurface
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("$rating/99", color = MilestoneGold, fontSize = 12.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onUpgrade,
                enabled = canUpgrade,
                modifier = Modifier.size(28.dp)
            ) {
                Surface(
                    color = if (canUpgrade) CricketGreen else Color(0xFF334155),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Add, contentDescription = "Upgrade", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
