package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun MatchSetupScreen(
    teams: List<Team>,
    venues: List<StadiumVenue>,
    language: Language,
    onBack: () -> Unit,
    onProceedToToss: (
        format: CricketFormat,
        overs: Int,
        difficulty: MatchDifficulty,
        venue: StadiumVenue,
        pitch: PitchType,
        weather: WeatherCondition,
        isDayNight: Boolean,
        teamA: Team,
        teamB: Team
    ) -> Unit
) {
    var selectedFormat by remember { mutableStateOf(CricketFormat.T20) }
    var customOvers by remember { mutableIntStateOf(5) }
    var selectedDifficulty by remember { mutableStateOf(MatchDifficulty.MEDIUM) }
    var selectedTeamA by remember { mutableStateOf(teams[0]) }
    var selectedTeamB by remember { mutableStateOf(teams[1]) }
    var selectedVenue by remember { mutableStateOf(venues[0]) }
    var selectedPitch by remember { mutableStateOf(PitchType.FLAT_BATTING) }
    var selectedWeather by remember { mutableStateOf(WeatherCondition.SUNNY) }
    var isDayNight by remember { mutableStateOf(true) }

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
            Text(
                text = if (language == Language.URDU) "میچ سیٹ اپ اور فارمیٹ" else "MATCH SETUP & CONDITIONS",
                color = TextWhitePrimary,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. FORMAT SELECTION
        SectionHeader(if (language == Language.URDU) "کرکٹ فارمیٹ منتخب کریں" else "SELECT CRICKET FORMAT")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(CricketFormat.values()) { format ->
                val isSelected = format == selectedFormat
                Surface(
                    modifier = Modifier.clickable { selectedFormat = format },
                    color = if (isSelected) CricketGreen else StadiumNavyCard,
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) CricketGreenLight else StadiumBorder))
                ) {
                    Text(
                        text = if (language == Language.URDU) format.displayNameUr else format.displayNameEn,
                        color = if (isSelected) Color.White else TextWhitePrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. TEAMS SELECTION (TEAM A vs TEAM B)
        SectionHeader(if (language == Language.URDU) "ٹیمیں منتخب کریں (آپ کی ٹیم بمقابلہ حریف)" else "SELECT TEAMS (YOUR TEAM vs OPPONENT)")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Team A
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == Language.URDU) "آپ کی ٹیم:" else "YOUR TEAM:",
                    color = MilestoneGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                teams.take(4).forEach { team ->
                    val isSelected = team.id == selectedTeamA.id
                    TeamSelectionPill(team, isSelected) { selectedTeamA = team }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // Team B
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == Language.URDU) "حریف ٹیم:" else "OPPONENT:",
                    color = Color(0xFF38BDF8),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                teams.drop(1).take(4).forEach { team ->
                    val isSelected = team.id == selectedTeamB.id
                    TeamSelectionPill(team, isSelected) { selectedTeamB = team }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. VENUE & PITCH SELECTION
        SectionHeader(if (language == Language.URDU) "اسٹیڈیم اور پچ کی قسم" else "STADIUM VENUE & PITCH TYPE")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(venues) { venue ->
                val isSelected = venue.id == selectedVenue.id
                Card(
                    modifier = Modifier
                        .width(170.dp)
                        .clickable {
                            selectedVenue = venue
                            selectedPitch = venue.defaultPitch
                        },
                    colors = CardDefaults.cardColors(containerColor = if (isSelected) StadiumNavySurface else StadiumNavyCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) MilestoneGold else StadiumBorder)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = if (language == Language.URDU) venue.nameUr else venue.nameEn,
                            color = TextWhitePrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = if (language == Language.URDU) venue.cityUr else venue.cityEn,
                            color = TextMutedSecondary,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Boundary: ${venue.boundaryDistanceMeters}m",
                            color = CricketGreenLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Pitch Type chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(PitchType.values()) { pitch ->
                val isSelected = pitch == selectedPitch
                Surface(
                    modifier = Modifier.clickable { selectedPitch = pitch },
                    color = if (isSelected) Color(0xFFC7B198) else StadiumNavyCard,
                    shape = RoundedCornerShape(8.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
                ) {
                    Text(
                        text = if (language == Language.URDU) pitch.displayNameUr else pitch.displayNameEn,
                        color = if (isSelected) Color.Black else TextWhitePrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. DIFFICULTY & WEATHER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SectionHeader(if (language == Language.URDU) "مشکل کی سطح" else "DIFFICULTY")
                MatchDifficulty.values().forEach { diff ->
                    val isSelected = diff == selectedDifficulty
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedDifficulty = diff }
                            .padding(vertical = 3.dp),
                        color = if (isSelected) CricketGreenDark else StadiumNavyCard,
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) CricketGreenLight else StadiumBorder))
                    ) {
                        Text(
                            text = if (language == Language.URDU) diff.displayNameUr else diff.displayNameEn,
                            color = TextWhitePrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                SectionHeader(if (language == Language.URDU) "موسم اور لائٹنگ" else "CONDITIONS")
                WeatherCondition.values().take(3).forEach { weather ->
                    val isSelected = weather == selectedWeather
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedWeather = weather }
                            .padding(vertical = 3.dp),
                        color = if (isSelected) Color(0xFF1E3A8A) else StadiumNavyCard,
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
                    ) {
                        Text(
                            text = if (language == Language.URDU) weather.displayNameUr else weather.displayNameEn,
                            color = TextWhitePrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isDayNight,
                        onCheckedChange = { isDayNight = it },
                        colors = CheckboxDefaults.colors(checkedColor = CricketGreen)
                    )
                    Text("Day / Night Lights", color = TextWhitePrimary, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // PROCEED BUTTON
        Button(
            onClick = {
                val overs = if (selectedFormat == CricketFormat.CUSTOM) customOvers else selectedFormat.defaultOvers
                onProceedToToss(
                    selectedFormat,
                    overs,
                    selectedDifficulty,
                    selectedVenue,
                    selectedPitch,
                    selectedWeather,
                    isDayNight,
                    selectedTeamA,
                    selectedTeamB
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = CricketGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = if (language == Language.URDU) "ٹاس کی طرف بڑھیں" else "PROCEED TO COIN TOSS",
                fontWeight = FontWeight.Black,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.PlayArrow, contentDescription = null)
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = TextMutedSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun TeamSelectionPill(team: Team, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = if (isSelected) team.primaryColor.copy(alpha = 0.35f) else StadiumNavyCard,
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) team.primaryColor else StadiumBorder)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(team.primaryColor, androidx.compose.foundation.shape.CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = team.nameEn,
                color = TextWhitePrimary,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
                maxLines = 1
            )
        }
    }
}
