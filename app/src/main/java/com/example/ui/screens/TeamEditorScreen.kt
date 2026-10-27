package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
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
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun TeamEditorScreen(
    teams: List<Team>,
    language: Language,
    onBack: () -> Unit,
    onSavePlayer: (updatedPlayer: Player) -> Unit
) {
    var selectedTeam by remember { mutableStateOf(teams[0]) }
    var editingPlayer by remember { mutableStateOf<Player?>(null) }

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhitePrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = if (language == Language.URDU) "ٹیم اور کھلاڑی ایڈیٹر" else "TEAM & SQUAD ROSTER EDITOR",
                    color = TextWhitePrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "Customize player names, ratings, faces and hair styles",
                    color = MilestoneGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Teams Selector Row
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(teams) { team ->
                val isSelected = team.id == selectedTeam.id
                Surface(
                    modifier = Modifier.clickable { selectedTeam = team },
                    color = if (isSelected) team.primaryColor else StadiumNavyCard,
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) Color.White else StadiumBorder))
                ) {
                    Text(
                        text = team.nameEn,
                        color = if (isSelected) Color.White else TextMutedSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Squad List (15 Players)
        Text(
            text = "${selectedTeam.nameEn.uppercase()} 15-PLAYER SQUAD",
            color = TextMutedSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(selectedTeam.squad) { player ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { editingPlayer = player },
                    color = StadiumNavyCard,
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(selectedTeam.primaryColor.copy(alpha = 0.25f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("#${player.jerseyNumber}", color = MilestoneGold, fontSize = 11.sp, fontWeight = FontWeight.Black)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(player.name, color = TextWhitePrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("${player.role.labelEn} • OVR: ${player.ratings.overall}", color = TextMutedSecondary, fontSize = 10.sp)
                            }
                        }

                        IconButton(onClick = { editingPlayer = player }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Player", tint = MilestoneGold, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // Player Edit BottomSheet / Dialog
        editingPlayer?.let { player ->
            var editedName by remember { mutableStateOf(player.name) }
            var editedTiming by remember { mutableIntStateOf(player.ratings.timing) }
            var editedPower by remember { mutableIntStateOf(player.ratings.power) }
            var editedPaceSpin by remember { mutableIntStateOf(player.ratings.bowlingPaceSpin) }
            var selectedSkin by remember { mutableIntStateOf(player.skinToneIndex) }
            var selectedHair by remember { mutableIntStateOf(player.hairStyleIndex) }

            AlertDialog(
                onDismissRequest = { editingPlayer = null },
                title = {
                    Text("EDIT CRICKETER PROFILE", color = TextWhitePrimary, fontWeight = FontWeight.Black, fontSize = 16.sp)
                },
                text = {
                    val editScroll = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 380.dp)
                            .verticalScroll(editScroll),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 3D Human Cricketer Model Preview
                        Image(
                            painter = painterResource(
                                id = if (player.role == PlayerRole.FAST_BOWLER || player.role == PlayerRole.SPIN_BOWLER) {
                                    R.drawable.bowler_3d_real
                                } else {
                                    R.drawable.player_3d_real
                                }
                            ),
                            contentDescription = "3D Human Cricketer",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(2.dp, MilestoneGold, RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = editedName,
                            onValueChange = { editedName = it },
                            label = { Text("Player Name") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhitePrimary,
                                unfocusedTextColor = TextWhitePrimary,
                                focusedBorderColor = CricketGreenLight,
                                unfocusedBorderColor = StadiumBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Appearance Customization:", color = MilestoneGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Skin Tone 1", "Skin Tone 2", "Skin Tone 3").forEachIndexed { idx, label ->
                                Surface(
                                    modifier = Modifier.clickable { selectedSkin = idx },
                                    color = if (selectedSkin == idx) CricketGreen else StadiumNavySurface,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(label, color = TextWhitePrimary, fontSize = 10.sp, modifier = Modifier.padding(6.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Attributes
                        Text("Batting Timing: $editedTiming", color = TextWhitePrimary, fontSize = 11.sp)
                        Slider(
                            value = editedTiming.toFloat(),
                            onValueChange = { editedTiming = it.toInt() },
                            valueRange = 50f..99f,
                            colors = SliderDefaults.colors(thumbColor = CricketGreenLight, activeTrackColor = CricketGreenLight)
                        )

                        Text("Power Hitting: $editedPower", color = TextWhitePrimary, fontSize = 11.sp)
                        Slider(
                            value = editedPower.toFloat(),
                            onValueChange = { editedPower = it.toInt() },
                            valueRange = 50f..99f,
                            colors = SliderDefaults.colors(thumbColor = MilestoneGold, activeTrackColor = MilestoneGold)
                        )

                        Text("Bowling Speed/Control: $editedPaceSpin", color = TextWhitePrimary, fontSize = 11.sp)
                        Slider(
                            value = editedPaceSpin.toFloat(),
                            onValueChange = { editedPaceSpin = it.toInt() },
                            valueRange = 50f..99f,
                            colors = SliderDefaults.colors(thumbColor = Color(0xFF38BDF8), activeTrackColor = Color(0xFF38BDF8))
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val updated = player.copy(
                                name = editedName,
                                skinToneIndex = selectedSkin,
                                hairStyleIndex = selectedHair,
                                ratings = player.ratings.copy(
                                    timing = editedTiming,
                                    power = editedPower,
                                    bowlingPaceSpin = editedPaceSpin
                                )
                            )
                            onSavePlayer(updated)
                            editingPlayer = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CricketGreen)
                    ) {
                        Text("SAVE CHANGES", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { editingPlayer = null }) {
                        Text("CANCEL", color = TextMutedSecondary)
                    }
                },
                containerColor = StadiumNavyCard,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}
