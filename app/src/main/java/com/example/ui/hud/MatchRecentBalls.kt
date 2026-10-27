package com.example.ui.hud

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BallDeliveryOutcome

@Composable
fun MatchRecentBalls(
    deliveries: List<BallDeliveryOutcome>,
    modifier: Modifier = Modifier
) {
    if (deliveries.isEmpty()) return

    Row(
        modifier = modifier
            .padding(horizontal = 14.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "THIS OVER:",
            color = Color(0xFF94A3B8),
            fontSize = 10.sp,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.width(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            items(deliveries) { outcome ->
                val (badgeColor, text) = when {
                    outcome.isWicket -> Pair(Color(0xFFDC2626), "W")
                    outcome.isBoundarySix -> Pair(Color(0xFFF59E0B), "6")
                    outcome.isBoundaryFour -> Pair(Color(0xFF16A34A), "4")
                    outcome.isExtra -> Pair(Color(0xFFEA580C), outcome.extraType ?: "EX")
                    outcome.runsScored > 0 -> Pair(Color(0xFF0284C7), outcome.runsScored.toString())
                    else -> Pair(Color(0xFF334155), "•")
                }

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(badgeColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}
