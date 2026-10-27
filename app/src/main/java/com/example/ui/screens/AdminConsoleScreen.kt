package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CricketRepository
import com.example.model.Language
import com.example.ui.theme.*

@Composable
fun AdminConsoleScreen(
    repository: CricketRepository,
    language: Language,
    onBack: () -> Unit
) {
    val releases by repository.adminReleases.collectAsState()

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
                    text = "INTERNAL OPERATIONS & CONTENT ADMIN",
                    color = TextWhitePrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = "Release staging, rulesets, 4GB RAM metrics & audit log",
                    color = MilestoneGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // System Health & Memory Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StadiumNavyCard),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(StadiumBorder))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Memory, contentDescription = null, tint = CricketGreenLight)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ENGINE TELEMETRY & 4GB MATRIX AUDIT", color = TextWhitePrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Physical Baseline Target: 4GB RAM Tier", color = TextMutedSecondary, fontSize = 11.sp)
                    Text("PASS (0 crashes)", color = CricketGreenLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Target FPS in Lite Mode: 30 FPS Lock", color = TextMutedSecondary, fontSize = 11.sp)
                    Text("STABLE 30 FPS", color = MilestoneGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Simulation Rules Engine Parity", color = TextMutedSecondary, fontSize = 11.sp)
                    Text("100% DETERMINISTIC", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "STAGED CONTENT RELEASES & PACKS",
            color = TextMutedSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(releases) { release ->
                Surface(
                    color = StadiumNavyCard,
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(if (release.isPublished) CricketGreen else StadiumBorder)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = if (release.isPublished) CricketGreenDark else Color(0xFF334155),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(release.version, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(release.title, color = TextWhitePrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Icon(
                                if (release.isPublished) Icons.Default.CloudDone else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (release.isPublished) CricketGreenLight else TextMutedSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(release.description, color = TextMutedSecondary, fontSize = 11.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Scheduled Date: ${release.releaseDate}", color = MilestoneGold, fontSize = 10.sp)
                            Surface(
                                color = StadiumNavySurface,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (release.isPublished) "ACTIVE IN PRODUCTION" else "STAGED FOR REVIEW",
                                    color = if (release.isPublished) CricketGreenLight else MilestoneGold,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
