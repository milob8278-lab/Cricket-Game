package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.GameScreen
import com.example.model.GraphicsProfile
import com.example.model.Language
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    language: Language,
    onLanguageToggle: () -> Unit,
    graphicsProfile: GraphicsProfile,
    onNavigate: (GameScreen) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(StadiumNavyDark, StadiumNavy, Color(0xFF040A12))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = CricketGreenDark,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsCricket,
                        contentDescription = "Real Cricket Pro",
                        tint = MilestoneGold,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(6.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "REAL CRICKET PRO",
                        color = TextWhitePrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = if (language == Language.URDU) "مستند تھری ڈی کرکٹ سمولیشن" else "Authentic 3D Cricket Simulation",
                        color = MilestoneGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Language & Settings Action Icons
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.clickable { onLanguageToggle() },
                    color = StadiumNavySurface,
                    shape = RoundedCornerShape(8.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
                ) {
                    Text(
                        text = if (language == Language.ENGLISH) "اردو" else "ENGLISH",
                        color = CricketGreenLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = { onNavigate(GameScreen.SETTINGS) }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextWhitePrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Stadium & Match Quick Launch Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigate(GameScreen.MATCH_SETUP) },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CricketGreen))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF0F3E22), StadiumNavyCard, Color(0xFF1B2E4B))
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            color = MilestoneGold,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (language == Language.URDU) "فوری میچ کھیلیں" else "PLAY NOW",
                                color = Color.Black,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (language == Language.URDU) "کوئیک میچ (ٹی 20 / او ڈی آئی)" else "QUICK MATCH T20 / ODI",
                            color = TextWhitePrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (language == Language.URDU) "حقیقی 3D کھلاڑیوں اور جدید فزکس کے ساتھ" else "Photorealistic 3D Human Cricketers, 360° batting & DRS",
                            color = TextMutedSecondary,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { onNavigate(GameScreen.MATCH_SETUP) },
                            colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (language == Language.URDU) "میچ کھیلیں" else "START MATCH",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }

                    // Real 3D Human Player Graphic
                    Image(
                        painter = painterResource(id = R.drawable.player_3d_real),
                        contentDescription = "Real 3D Cricketer",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.5.dp, MilestoneGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Main Modes Grid
        Text(
            text = if (language == Language.URDU) "گیم موڈز اور ٹورنامنٹس" else "GAME MODES & COMPETITIONS",
            color = TextMutedSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ModeTile(
                title = if (language == Language.URDU) "سپر ٹورنامنٹ" else "Tournament",
                subtitle = if (language == Language.URDU) "لیگ میچز اور فائنل" else "League & Finals",
                icon = Icons.Default.EmojiEvents,
                iconColor = MilestoneGold,
                modifier = Modifier.weight(1f)
            ) { onNavigate(GameScreen.TOURNAMENT) }

            ModeTile(
                title = if (language == Language.URDU) "کیریئر موڈ" else "Career Mode",
                subtitle = if (language == Language.URDU) "اپنا کھلاڑی بنائیں" else "Build Your Legend",
                icon = Icons.Default.Person,
                iconColor = Color(0xFF38BDF8),
                modifier = Modifier.weight(1f)
            ) { onNavigate(GameScreen.CAREER) }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ModeTile(
                title = if (language == Language.URDU) "پریکٹس نیٹس" else "Practice / Nets",
                subtitle = if (language == Language.URDU) "بیٹنگ اور بولنگ مشق" else "Master All 24 Shots",
                icon = Icons.Default.Sports,
                iconColor = CricketGreenLight,
                modifier = Modifier.weight(1f)
            ) { onNavigate(GameScreen.PRACTICE) }

            ModeTile(
                title = if (language == Language.URDU) "سنسنی خیز چیلنجز" else "Challenges",
                subtitle = if (language == Language.URDU) "آخری اوور کا معرکہ" else "Pressure Scenarios",
                icon = Icons.Default.FlashOn,
                iconColor = Color(0xFFF97316),
                modifier = Modifier.weight(1f)
            ) { onNavigate(GameScreen.CHALLENGES) }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ModeTile(
                title = if (language == Language.URDU) "ملٹی پلیئر (دو کھلاڑی)" else "Hot-Seat Pass & Play",
                subtitle = if (language == Language.URDU) "ایک ہی فون پر مقابلہ" else "2 Players Local",
                icon = Icons.Default.People,
                iconColor = Color(0xFFC084FC),
                modifier = Modifier.weight(1f)
            ) { onNavigate(GameScreen.MULTIPLAYER) }

            ModeTile(
                title = if (language == Language.URDU) "ٹیم اور کھلاڑی ایڈیٹر" else "Teams & Players",
                subtitle = if (language == Language.URDU) "نام اور کٹس ایڈٹ کریں" else "Roster Customizer",
                icon = Icons.Default.Edit,
                iconColor = Color(0xFF2DD4BF),
                modifier = Modifier.weight(1f)
            ) { onNavigate(GameScreen.TEAMS_ROSTER) }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Features & Customization Strip
        Text(
            text = if (language == Language.URDU) "اسٹیڈیمز اور سیٹنگز" else "STADIUMS & CUSTOMIZATION",
            color = TextMutedSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SubFeatureCard(
                title = if (language == Language.URDU) "اسٹیڈیم گیلری" else "Venues & Pitches",
                icon = Icons.Default.Stadium,
                modifier = Modifier.weight(1f)
            ) { onNavigate(GameScreen.STADIUMS) }

            SubFeatureCard(
                title = if (language == Language.URDU) "کنٹرول لے آؤٹ ایڈیٹر" else "HUD Layout Editor",
                icon = Icons.Default.Tune,
                modifier = Modifier.weight(1f)
            ) { onNavigate(GameScreen.LAYOUT_EDITOR) }

            SubFeatureCard(
                title = if (language == Language.URDU) "ایڈمن کنسول" else "Admin Console",
                icon = Icons.Default.AdminPanelSettings,
                modifier = Modifier.weight(1f)
            ) { onNavigate(GameScreen.ADMIN_CONSOLE) }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Graphics Profile & Engine Specs Banner
        Surface(
            color = StadiumNavySurface,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Speed, contentDescription = null, tint = CricketGreenLight)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "ENGINE PROFILE: ${graphicsProfile.displayNameEn}",
                        color = TextWhitePrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Unified Physics & Rules Parity across 3D High, Balanced & 4GB Lite",
                        color = TextMutedSecondary,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ModeTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(title, color = TextWhitePrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(subtitle, color = TextMutedSecondary, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun SubFeatureCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(72.dp)
            .clickable { onClick() },
        color = StadiumNavyCard,
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = MilestoneGold, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, color = TextWhitePrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}
