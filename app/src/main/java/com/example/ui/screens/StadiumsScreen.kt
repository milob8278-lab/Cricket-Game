package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.model.StadiumVenue
import com.example.ui.theme.*

@Composable
fun StadiumsScreen(
    venues: List<StadiumVenue>,
    language: Language,
    onBack: () -> Unit
) {
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
                    text = if (language == Language.URDU) "کرکٹ اسٹیڈیمز اور وینیوز" else "STADIUMS & VENUES CATALOG",
                    color = TextWhitePrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "Authentic regional stadiums with realistic pitch behavior",
                    color = MilestoneGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(venues) { venue ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (language == Language.URDU) venue.nameUr else venue.nameEn,
                                    color = TextWhitePrimary,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = if (language == Language.URDU) venue.cityUr else venue.cityEn,
                                    color = TextMutedSecondary,
                                    fontSize = 11.sp
                                )
                            }
                            Icon(Icons.Default.Stadium, contentDescription = null, tint = MilestoneGold, modifier = Modifier.size(28.dp))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (language == Language.URDU) venue.descriptionUr else venue.descriptionEn,
                            color = TextWhitePrimary.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(color = StadiumNavySurface, shape = RoundedCornerShape(6.dp)) {
                                Text("BOUNDARY: ${venue.boundaryDistanceMeters}m", color = CricketGreenLight, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                            }
                            Surface(color = StadiumNavySurface, shape = RoundedCornerShape(6.dp)) {
                                Text("PITCH: ${venue.defaultPitch.name.replace("_", " ")}", color = Color(0xFFC7B198), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                            }
                            Surface(color = StadiumNavySurface, shape = RoundedCornerShape(6.dp)) {
                                Text("CAPACITY: ${venue.capacity / 1000}K", color = MilestoneGold, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
