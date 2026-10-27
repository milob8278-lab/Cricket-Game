package com.example.ui.field

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.model.*
import kotlin.math.cos
import kotlin.math.sin

enum class CameraAngle(val labelEn: String, val labelUr: String) {
    BROADCAST("Broadcast TV", "براڈکاسٹ کیمرا"),
    BEHIND_BATSMAN("Behind Batter", "بلے باز کے پیچھے"),
    STUMP_CAM("Stump Cam", "اسٹمپ کیمرا"),
    DRONE_AERIAL("Drone / Aerial", "ڈرون نظارہ")
}

@Composable
fun CricketField3D(
    modifier: Modifier = Modifier,
    graphicsProfile: GraphicsProfile,
    cameraAngle: CameraAngle = CameraAngle.BROADCAST,
    isDelivering: Boolean = false,
    deliveryProgress: Float = 0f, // 0.0 (runup) to 0.5 (release) to 1.0 (bat contact / boundary)
    ballExitAngleDegrees: Float = 135f,
    ballExitDistance: Float = 0f,
    shotType: ShotType? = null,
    battingTeamColor: Color = Color(0xFF15803D),
    bowlingTeamColor: Color = Color(0xFF1E40AF),
    fieldPreset: FieldPreset = FieldPreset.ATTACKING,
    weather: WeatherCondition = WeatherCondition.SUNNY
) {
    // Ambient stadium animation for crowd & floodlights
    val infiniteTransition = rememberInfiniteTransition(label = "stadium")
    val crowdSway by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "crowd"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF07111E))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            when (cameraAngle) {
                CameraAngle.BROADCAST -> drawBroadcastView(
                    width, height, graphicsProfile, crowdSway,
                    isDelivering, deliveryProgress, ballExitAngleDegrees, ballExitDistance,
                    shotType, battingTeamColor, bowlingTeamColor, fieldPreset, weather
                )
                CameraAngle.BEHIND_BATSMAN -> drawBehindBatsmanView(
                    width, height, graphicsProfile,
                    isDelivering, deliveryProgress, ballExitAngleDegrees, ballExitDistance,
                    battingTeamColor, bowlingTeamColor
                )
                CameraAngle.STUMP_CAM -> drawStumpCamView(
                    width, height, graphicsProfile,
                    isDelivering, deliveryProgress, battingTeamColor, bowlingTeamColor
                )
                CameraAngle.DRONE_AERIAL -> drawDroneView(
                    width, height, graphicsProfile,
                    isDelivering, deliveryProgress, ballExitAngleDegrees, ballExitDistance,
                    battingTeamColor, bowlingTeamColor, fieldPreset
                )
            }
        }
    }
}

private fun DrawScope.drawBroadcastView(
    w: Float,
    h: Float,
    profile: GraphicsProfile,
    crowdSway: Float,
    isDelivering: Boolean,
    progress: Float,
    exitAngle: Float,
    exitDistance: Float,
    shotType: ShotType?,
    batColor: Color,
    bowlColor: Color,
    fieldPreset: FieldPreset,
    weather: WeatherCondition
) {
    val horizonY = h * 0.22f
    val centerX = w * 0.5f

    // 1. Sky & Floodlights
    val skyBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF040B14), Color(0xFF0A1B30), Color(0xFF112946)),
        startY = 0f,
        endY = horizonY
    )
    drawRect(brush = skyBrush, size = Size(w, horizonY))

    // Floodlight glow towers (High 3D mode)
    if (profile == GraphicsProfile.HIGH_3D) {
        drawCircle(Color(0x33FFEA00), radius = 45f, center = Offset(w * 0.15f, horizonY * 0.4f))
        drawCircle(Color(0x33FFEA00), radius = 45f, center = Offset(w * 0.85f, horizonY * 0.4f))
        drawLine(Color(0xFF64748B), Offset(w * 0.15f, horizonY * 0.4f), Offset(w * 0.15f, horizonY), strokeWidth = 3f)
        drawLine(Color(0xFF64748B), Offset(w * 0.85f, horizonY * 0.4f), Offset(w * 0.85f, horizonY), strokeWidth = 3f)
    }

    // 2. Crowd in stadium stands
    val crowdColor1 = Color(0xFF1E293B)
    val crowdColor2 = Color(0xFF334155)
    drawRect(crowdColor1, Offset(0f, horizonY * 0.65f), Size(w, horizonY * 0.35f))
    if (profile != GraphicsProfile.LITE_3D) {
        for (i in 0..30) {
            val cx = (w / 30f) * i + crowdSway * if (i % 2 == 0) 1f else -1f
            drawCircle(crowdColor2, radius = 5f, center = Offset(cx, horizonY * 0.8f))
        }
    }

    // 3. Outfield Oval
    val fieldPath = Path().apply {
        moveTo(0f, h)
        lineTo(0f, horizonY)
        cubicTo(w * 0.2f, horizonY - 20f, w * 0.8f, horizonY - 20f, w, horizonY)
        lineTo(w, h)
        close()
    }

    val grassBase = if (weather == WeatherCondition.RAIN_DRIZZLE) Color(0xFF165A2C) else Color(0xFF1E6F38)
    drawPath(fieldPath, grassBase)

    // Outfield mowed lawn stripes (Perspective rings)
    if (profile != GraphicsProfile.LITE_3D) {
        val lightGrass = Color(0x22FFFFFF)
        for (step in 1..4) {
            val ringY = horizonY + (h - horizonY) * (step / 5f)
            val ringRadiusX = w * (0.35f + step * 0.15f)
            val ringRadiusY = (h - horizonY) * (0.2f + step * 0.2f)
            drawOval(
                color = lightGrass,
                topLeft = Offset(centerX - ringRadiusX, ringY - ringRadiusY),
                size = Size(ringRadiusX * 2, ringRadiusY * 2),
                style = Stroke(width = 12f)
            )
        }
    }

    // 30-Yard Circle (Inner Ring)
    val innerCircleY = h * 0.62f
    drawOval(
        color = Color(0x66FFFFFF),
        topLeft = Offset(centerX - w * 0.38f, innerCircleY - h * 0.22f),
        size = Size(w * 0.76f, h * 0.44f),
        style = Stroke(width = 2.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f)))
    )

    // 4. Cricket Pitch Strip (Perspective Trapezoid)
    val pitchTopY = horizonY + 25f
    val pitchBottomY = h * 0.88f
    val pitchTopWidth = w * 0.08f
    val pitchBottomWidth = w * 0.22f

    val pitchPath = Path().apply {
        moveTo(centerX - pitchTopWidth / 2, pitchTopY)
        lineTo(centerX + pitchTopWidth / 2, pitchTopY)
        lineTo(centerX + pitchBottomWidth / 2, pitchBottomY)
        lineTo(centerX - pitchBottomWidth / 2, pitchBottomY)
        close()
    }
    drawPath(pitchPath, Color(0xFFC7B198)) // Clay Pitch

    // Crease Lines: Bowling crease and popping crease at striker & non-striker ends
    drawLine(Color.White, Offset(centerX - pitchTopWidth * 0.7f, pitchTopY + 15f), Offset(centerX + pitchTopWidth * 0.7f, pitchTopY + 15f), strokeWidth = 2.5f)
    drawLine(Color.White, Offset(centerX - pitchBottomWidth * 0.85f, pitchBottomY - 25f), Offset(centerX + pitchBottomWidth * 0.85f, pitchBottomY - 25f), strokeWidth = 3f)

    // Stumps: Non-striker end
    drawStumps(Offset(centerX, pitchTopY + 14f), scale = 0.5f)
    // Stumps: Striker end
    drawStumps(Offset(centerX, pitchBottomY - 26f), scale = 1.0f)

    // 5. 3D Active Players
    // Non-striker batsman
    drawPlayer3D(
        position = Offset(centerX - pitchTopWidth * 0.8f, pitchTopY + 18f),
        jerseyColor = batColor,
        isBatsman = true,
        scale = 0.55f
    )

    // Striker batsman (Right-handed batting stance / follow-through)
    val batSwingAngle = if (isDelivering && progress > 0.55f) {
        ((progress - 0.55f) / 0.45f) * 75f
    } else 0f
    drawPlayer3D(
        position = Offset(centerX - 18f, pitchBottomY - 25f),
        jerseyColor = batColor,
        isBatsman = true,
        scale = 1.15f,
        batSwingDeg = batSwingAngle
    )

    // Wicketkeeper (Crouched behind striker stumps)
    drawRealisticHumanKeeper3D(
        position = Offset(centerX, pitchBottomY + 22f),
        jerseyColor = bowlColor,
        scale = 1.1f
    )

    // Bowler (At top of pitch / delivering)
    val bowlerY = if (isDelivering && progress < 0.5f) {
        pitchTopY - 25f + (progress * 2f * 20f)
    } else pitchTopY - 20f

    drawRealisticHumanBowler3D(
        position = Offset(centerX + pitchTopWidth * 0.6f, bowlerY),
        jerseyColor = bowlColor,
        scale = 0.6f,
        armAngle = if (isDelivering && progress < 0.5f) progress * 360f else 45f
    )

    // 6. 3D Fielders (Positions dynamically based on FieldPreset)
    drawFieldersPreset(w, h, centerX, horizonY, bowlColor, fieldPreset, profile)

    // 7. 3D Cricket Ball Flight & Shadow
    if (isDelivering) {
        drawBall3D(
            progress = progress,
            centerX = centerX,
            pitchTopY = pitchTopY + 15f,
            pitchBottomY = pitchBottomY - 26f,
            exitAngle = exitAngle,
            exitDistance = exitDistance
        )
    }
}

private fun DrawScope.drawStumps(pos: Offset, scale: Float) {
    val stumpW = 4f * scale
    val stumpH = 34f * scale
    val spacing = 7f * scale

    // 3 wooden stumps
    for (i in -1..1) {
        drawRoundRect(
            color = Color(0xFFFFB300),
            topLeft = Offset(pos.x + i * spacing - stumpW / 2, pos.y - stumpH),
            size = Size(stumpW, stumpH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f, 2f)
        )
    }
    // Bails across top
    drawRoundRect(
        color = Color(0xFFFFE082),
        topLeft = Offset(pos.x - spacing * 1.4f, pos.y - stumpH - 3f * scale),
        size = Size(spacing * 2.8f, 3.5f * scale),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(1f, 1f)
    )
}

private fun DrawScope.drawPlayer3D(
    position: Offset,
    jerseyColor: Color,
    isBatsman: Boolean,
    scale: Float,
    batSwingDeg: Float = 0f
) {
    if (isBatsman) {
        drawRealisticHumanBatsman3D(position, jerseyColor, scale, batSwingDeg)
    } else {
        drawRealisticHumanFielder3D(position, jerseyColor, scale)
    }
}

private fun DrawScope.drawRealisticHumanBatsman3D(
    position: Offset,
    jerseyColor: Color,
    scale: Float,
    batSwingDeg: Float = 0f
) {
    // 1. Realistic Ambient Contact Shadow on turf
    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(Color(0x77000000), Color(0x33000000), Color.Transparent),
            center = Offset(position.x, position.y),
            radius = 24f * scale
        ),
        topLeft = Offset(position.x - 22f * scale, position.y - 7f * scale),
        size = Size(44f * scale, 14f * scale)
    )

    // 2. Athletic Spiked Cricket Shoes (Left & Right)
    val shoeW = 9f * scale
    val shoeH = 5f * scale
    // Back shoe (Left foot, grounded at crease)
    drawRoundRect(
        color = Color(0xFFF8FAFC),
        topLeft = Offset(position.x - 12f * scale, position.y - 4f * scale),
        size = Size(shoeW, shoeH),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f, 2f)
    )
    drawRect(Color(0xFF0F172A), Offset(position.x - 12f * scale, position.y - 1f * scale), Size(shoeW, 1.5f * scale)) // Spikes sole
    // Front shoe (Right foot, pointing towards cover/mid-off)
    drawRoundRect(
        color = Color(0xFFF8FAFC),
        topLeft = Offset(position.x + 3f * scale, position.y - 4f * scale),
        size = Size(shoeW, shoeH),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f, 2f)
    )
    drawRect(Color(0xFF0F172A), Offset(position.x + 3f * scale, position.y - 1f * scale), Size(shoeW, 1.5f * scale))

    // 3. Realistic 3D Batting Pads with Knee Rolls & Cane Rods
    val padW = 10f * scale
    val padH = 26f * scale
    val padTopY = position.y - 4f * scale - padH

    // Back leg pad
    drawRoundRect(
        color = Color(0xFFF1F5F9), // White leather pad
        topLeft = Offset(position.x - 13f * scale, padTopY),
        size = Size(padW, padH),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
    )
    // Front leg pad (More visible, facing bowler)
    drawRoundRect(
        color = Color(0xFFFFFFFF),
        topLeft = Offset(position.x + 2f * scale, padTopY),
        size = Size(padW, padH),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
    )
    // 3 Padded Horizontal Knee Rolls on front pad
    val kneeRollY = padTopY + padH * 0.35f
    for (i in 0..2) {
        drawLine(
            color = Color(0xFFCBD5E1),
            start = Offset(position.x + 2f * scale, kneeRollY + i * 3.5f * scale),
            end = Offset(position.x + 12f * scale, kneeRollY + i * 3.5f * scale),
            strokeWidth = 2f * scale
        )
    }
    // Vertical cane bars along front shin
    for (i in 1..3) {
        val caneX = position.x + (2f + i * 2.2f) * scale
        drawLine(
            color = Color(0xFFE2E8F0),
            start = Offset(caneX, kneeRollY + 10f * scale),
            end = Offset(caneX, padTopY + padH - 2f * scale),
            strokeWidth = 1.2f * scale
        )
    }
    // Pad buckle straps (Navy/Black velcro straps)
    drawLine(Color(0xFF334155), Offset(position.x + 1f * scale, padTopY + padH * 0.65f), Offset(position.x + 12f * scale, padTopY + padH * 0.65f), strokeWidth = 1.5f * scale)
    drawLine(Color(0xFF334155), Offset(position.x + 1f * scale, padTopY + padH * 0.85f), Offset(position.x + 12f * scale, padTopY + padH * 0.85f), strokeWidth = 1.5f * scale)

    // 4. Cricket Trousers / Hips
    val hipY = padTopY - 6f * scale
    drawRoundRect(
        color = Color(0xFFE2E8F0),
        topLeft = Offset(position.x - 10f * scale, hipY),
        size = Size(20f * scale, 10f * scale),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f)
    )

    // 5. Athletic Muscular Torso & Team Jersey
    val torsoW = 22f * scale
    val torsoH = 28f * scale
    val torsoTopY = hipY - torsoH

    // Jersey main body
    drawRoundRect(
        color = jerseyColor,
        topLeft = Offset(position.x - torsoW / 2, torsoTopY),
        size = Size(torsoW, torsoH),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
    )
    // Team Side Flank Ventilation Stripes (Contrast Gold/Cyan)
    drawRect(Color(0x33FFFFFF), Offset(position.x - torsoW / 2, torsoTopY), Size(3f * scale, torsoH))
    drawRect(Color(0x33FFFFFF), Offset(position.x + torsoW / 2 - 3f * scale, torsoTopY), Size(3f * scale, torsoH))
    // V-Neck / Collar Accent
    val collarPath = Path().apply {
        moveTo(position.x - 5f * scale, torsoTopY)
        lineTo(position.x, torsoTopY + 7f * scale)
        lineTo(position.x + 5f * scale, torsoTopY)
        close()
    }
    drawPath(collarPath, Color(0xFFF59E0B)) // Gold collar trim
    // Team Chest Logo Crest
    drawCircle(Color(0xFFFFD700), radius = 2.5f * scale, center = Offset(position.x - 5f * scale, torsoTopY + 10f * scale))

    // 6. Human Neck & Skin Shading
    val neckW = 7f * scale
    val neckH = 6f * scale
    val skinColor = Color(0xFFC68642) // Natural healthy athletic tan
    drawRoundRect(
        color = skinColor,
        topLeft = Offset(position.x - neckW / 2, torsoTopY - neckH + 2f * scale),
        size = Size(neckW, neckH),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f, 2f)
    )

    // 7. Human Face & 3D Cricket Protective Helmet
    val headCenter = Offset(position.x, torsoTopY - neckH - 6f * scale)
    val headRadius = 10f * scale

    // Human Chin & Facial Silhouette (Peeking through grill)
    drawCircle(color = skinColor, radius = headRadius * 0.85f, center = headCenter)
    // Eyes & Brow shadow
    drawLine(Color(0xFF3E2723), Offset(headCenter.x - 4f * scale, headCenter.y - 1f * scale), Offset(headCenter.x + 4f * scale, headCenter.y - 1f * scale), strokeWidth = 1.5f * scale)

    // 3D Helmet Shell (Curved composite dome)
    val helmetPath = Path().apply {
        moveTo(headCenter.x - headRadius, headCenter.y + 2f * scale)
        cubicTo(
            headCenter.x - headRadius, headCenter.y - headRadius * 1.3f,
            headCenter.x + headRadius, headCenter.y - headRadius * 1.3f,
            headCenter.x + headRadius, headCenter.y + 2f * scale
        )
        close()
    }
    drawPath(helmetPath, Color(0xFF0F172A)) // Navy helmet
    // Visor Peak
    drawLine(Color(0xFF1E293B), Offset(headCenter.x - headRadius * 0.9f, headCenter.y), Offset(headCenter.x + headRadius * 1.1f, headCenter.y + 1f * scale), strokeWidth = 3f * scale)

    // Steel Faceguard Grill (3 horizontal protective wires + ear struts)
    val grillColor = Color(0xFFE2E8F0)
    for (g in 1..3) {
        val yOffset = headCenter.y + (g * 2.4f) * scale
        drawLine(
            color = grillColor,
            start = Offset(headCenter.x - 7f * scale, yOffset),
            end = Offset(headCenter.x + 7f * scale, yOffset),
            strokeWidth = 1.3f * scale
        )
    }
    // Vertical side grill strut
    drawLine(grillColor, Offset(headCenter.x - 2f * scale, headCenter.y + 2f * scale), Offset(headCenter.x - 2f * scale, headCenter.y + 8f * scale), strokeWidth = 1.2f * scale)
    drawLine(grillColor, Offset(headCenter.x + 3f * scale, headCenter.y + 2f * scale), Offset(headCenter.x + 3f * scale, headCenter.y + 8f * scale), strokeWidth = 1.2f * scale)

    // 8. Articulated Muscular Arms & Batting Gloves
    // Right arm (Holding top of bat handle)
    drawRoundRect(
        color = skinColor,
        topLeft = Offset(position.x + 9f * scale, torsoTopY + 4f * scale),
        size = Size(6f * scale, 14f * scale),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f)
    )
    // Left arm (Bent forward across chest)
    drawRoundRect(
        color = skinColor,
        topLeft = Offset(position.x - 12f * scale, torsoTopY + 5f * scale),
        size = Size(14f * scale, 6f * scale),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f)
    )

    // Padded Batting Gloves (White leather with sausage/split finger rolls)
    val glovePos = Offset(position.x + 8f * scale, torsoTopY + 18f * scale)
    drawCircle(Color(0xFFFFFFFF), radius = 5f * scale, center = glovePos)
    drawCircle(Color(0xFFE2E8F0), radius = 4f * scale, center = Offset(glovePos.x - 3f * scale, glovePos.y + 4f * scale))
    // Glove wristband
    drawRoundRect(Color(0xFF1E40AF), Offset(glovePos.x - 4f * scale, glovePos.y - 6f * scale), Size(8f * scale, 3f * scale), androidx.compose.ui.geometry.CornerRadius(1f, 1f))

    // 9. Authentic English Willow Bat with Grip, Stickers, and Swing Animation
    val batAngleRad = Math.toRadians((18.0 - batSwingDeg))
    val batLen = 38f * scale
    val batStartX = glovePos.x
    val batStartY = glovePos.y
    val batEndX = batStartX + (batLen * sin(batAngleRad)).toFloat()
    val batEndY = batStartY - (batLen * cos(batAngleRad)).toFloat()

    // Handle with rubber spiral grip
    val handleLen = 12f * scale
    val handleEndX = batStartX + (handleLen * sin(batAngleRad)).toFloat()
    val handleEndY = batStartY - (handleLen * cos(batAngleRad)).toFloat()
    drawLine(
        color = Color(0xFF0F172A), // Black rubber grip
        start = Offset(batStartX, batStartY),
        end = Offset(handleEndX, handleEndY),
        strokeWidth = 3.5f * scale,
        cap = StrokeCap.Round
    )

    // Willow Blade (Honey Woodgrain gradient + thick spine)
    drawLine(
        color = Color(0xFFD4A373), // English willow
        start = Offset(handleEndX, handleEndY),
        end = Offset(batEndX, batEndY),
        strokeWidth = 7f * scale,
        cap = StrokeCap.Square
    )
    // Colored manufacturer bat sticker
    val stickerLen = 10f * scale
    val stickerStartX = handleEndX + (4f * scale * sin(batAngleRad)).toFloat()
    val stickerStartY = handleEndY - (4f * scale * cos(batAngleRad)).toFloat()
    val stickerEndX = stickerStartX + (stickerLen * sin(batAngleRad)).toFloat()
    val stickerEndY = stickerStartY - (stickerLen * cos(batAngleRad)).toFloat()
    drawLine(
        color = Color(0xFFEF4444), // Red/gold sticker
        start = Offset(stickerStartX, stickerStartY),
        end = Offset(stickerEndX, stickerEndY),
        strokeWidth = 6.2f * scale
    )
}

private fun DrawScope.drawRealisticHumanBowler3D(
    position: Offset,
    jerseyColor: Color,
    scale: Float,
    armAngle: Float
) {
    val skinColor = Color(0xFFC68642)

    // Ground shadow
    drawOval(
        brush = Brush.radialGradient(listOf(Color(0x66000000), Color.Transparent)),
        topLeft = Offset(position.x - 16f * scale, position.y - 4f * scale),
        size = Size(32f * scale, 10f * scale)
    )

    // Running legs in delivery stride:
    // Lead leg (Firmly planted, bent knee absorbing landing force)
    drawRoundRect(
        color = Color(0xFFF1F5F9), // White cricket trousers
        topLeft = Offset(position.x - 3f * scale, position.y - 20f * scale),
        size = Size(8f * scale, 20f * scale),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f)
    )
    drawRoundRect(Color(0xFFFFFFFF), Offset(position.x - 5f * scale, position.y - 4f * scale), Size(11f * scale, 5f * scale), androidx.compose.ui.geometry.CornerRadius(2f, 2f)) // Shoe

    // Trailing back leg (Kick back in run-up stride)
    drawRoundRect(
        color = Color(0xFFE2E8F0),
        topLeft = Offset(position.x + 8f * scale, position.y - 24f * scale),
        size = Size(7f * scale, 16f * scale),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f)
    )
    drawRoundRect(Color(0xFFFFFFFF), Offset(position.x + 12f * scale, position.y - 12f * scale), Size(8f * scale, 5f * scale), androidx.compose.ui.geometry.CornerRadius(2f, 2f))

    // Athletic Torso & Bowler Jersey
    val torsoW = 20f * scale
    val torsoH = 26f * scale
    val torsoTopY = position.y - 20f * scale - torsoH
    drawRoundRect(
        color = jerseyColor,
        topLeft = Offset(position.x - torsoW / 2, torsoTopY),
        size = Size(torsoW, torsoH),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(5f, 5f)
    )
    // Bowler jersey player number
    drawRect(Color(0x33FFFFFF), Offset(position.x - 3f * scale, torsoTopY + 6f * scale), Size(6f * scale, 10f * scale))

    // Human Head & Bowler Hair / Sweatband
    val headRadius = 8f * scale
    val headCenter = Offset(position.x, torsoTopY - headRadius)
    drawCircle(skinColor, radius = headRadius, center = headCenter)
    // Modern Athletic Hair Fade
    drawArc(
        color = Color(0xFF1E293B),
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(headCenter.x - headRadius, headCenter.y - headRadius),
        size = Size(headRadius * 2, headRadius * 2)
    )
    // Facial features (Eyes, jaw)
    drawLine(Color(0xFF3E2723), Offset(headCenter.x - 3f * scale, headCenter.y), Offset(headCenter.x + 3f * scale, headCenter.y), strokeWidth = 1.3f * scale)

    // Non-Bowling Arm (Tucked into chest for aerodynamic lever)
    drawRoundRect(skinColor, Offset(position.x - 10f * scale, torsoTopY + 8f * scale), Size(6f * scale, 12f * scale), androidx.compose.ui.geometry.CornerRadius(2f, 2f))

    // Full 360 Windmill Bowling Delivery Arm
    val armRad = Math.toRadians(armAngle.toDouble())
    val armLen = 28f * scale
    val armStartX = position.x + 7f * scale
    val armStartY = torsoTopY + 4f * scale
    val handX = armStartX + (armLen * sin(armRad)).toFloat()
    val handY = armStartY - (armLen * cos(armRad)).toFloat()

    // Muscular bowling arm
    drawLine(
        color = skinColor,
        start = Offset(armStartX, armStartY),
        end = Offset(handX, handY),
        strokeWidth = 5f * scale,
        cap = StrokeCap.Round
    )
    // Hand & Wrist
    drawCircle(skinColor, radius = 4f * scale, center = Offset(handX, handY))

    // Cricket Ball held in bowler's hand across the seam
    val ballRadius = 4f * scale
    drawCircle(Color(0xFFDC2626), radius = ballRadius, center = Offset(handX, handY))
    drawLine(Color.White, Offset(handX - 2f * scale, handY - 2f * scale), Offset(handX + 2f * scale, handY + 2f * scale), strokeWidth = 1f * scale)
}

private fun DrawScope.drawRealisticHumanKeeper3D(position: Offset, jerseyColor: Color, scale: Float) {
    val skinColor = Color(0xFFC68642)

    // Deep Crouch contact shadow
    drawOval(Color(0x66000000), Offset(position.x - 18f * scale, position.y - 3f * scale), Size(36f * scale, 10f * scale))

    // Crouched Legs with Wicketkeeping Pads (Flared knees)
    val padW = 10f * scale
    val padH = 18f * scale
    drawRoundRect(Color(0xFFFFFFFF), Offset(position.x - 15f * scale, position.y - 18f * scale), Size(padW, padH), androidx.compose.ui.geometry.CornerRadius(3f, 3f))
    drawRoundRect(Color(0xFFFFFFFF), Offset(position.x + 5f * scale, position.y - 18f * scale), Size(padW, padH), androidx.compose.ui.geometry.CornerRadius(3f, 3f))
    // Shoes
    drawRoundRect(Color(0xFFF8FAFC), Offset(position.x - 14f * scale, position.y - 4f * scale), Size(9f * scale, 4f * scale), androidx.compose.ui.geometry.CornerRadius(1f, 1f))
    drawRoundRect(Color(0xFFF8FAFC), Offset(position.x + 6f * scale, position.y - 4f * scale), Size(9f * scale, 4f * scale), androidx.compose.ui.geometry.CornerRadius(1f, 1f))

    // Torso (Low athletic crouch)
    val torsoTopY = position.y - 38f * scale
    drawRoundRect(jerseyColor, Offset(position.x - 11f * scale, torsoTopY), Size(22f * scale, 22f * scale), androidx.compose.ui.geometry.CornerRadius(5f, 5f))

    // Keeper Helmet with Full Steel Cage Faceguard
    val headCenter = Offset(position.x, torsoTopY - 8f * scale)
    drawCircle(Color(0xFF0F172A), radius = 9f * scale, center = headCenter)
    val grillColor = Color(0xFFCBD5E1)
    for (g in 1..3) {
        val gy = headCenter.y + g * 2.2f * scale
        drawLine(grillColor, Offset(headCenter.x - 6f * scale, gy), Offset(headCenter.x + 6f * scale, gy), strokeWidth = 1.3f * scale)
    }

    // Huge Webbed Wicketkeeping Gloves (Cupped open waiting for ball)
    val gloveR = 6.5f * scale
    val leftGlove = Offset(position.x - 14f * scale, position.y - 18f * scale)
    val rightGlove = Offset(position.x + 14f * scale, position.y - 18f * scale)

    drawCircle(Color(0xFFF59E0B), radius = gloveR, center = leftGlove) // Gold/Yellow leather
    drawCircle(Color(0xFF0F172A), radius = gloveR * 0.7f, center = leftGlove) // Inner web cup
    drawCircle(Color(0xFFF59E0B), radius = gloveR, center = rightGlove)
    drawCircle(Color(0xFF0F172A), radius = gloveR * 0.7f, center = rightGlove)
}

private fun DrawScope.drawRealisticHumanFielder3D(position: Offset, jerseyColor: Color, scale: Float) {
    val skinColor = Color(0xFFC68642)

    // Shadow
    drawOval(Color(0x44000000), Offset(position.x - 12f * scale, position.y - 2f * scale), Size(24f * scale, 7f * scale))

    // Trousers
    drawRoundRect(Color(0xFFF1F5F9), Offset(position.x - 7f * scale, position.y - 16f * scale), Size(6f * scale, 16f * scale), androidx.compose.ui.geometry.CornerRadius(2f, 2f))
    drawRoundRect(Color(0xFFF1F5F9), Offset(position.x + 1f * scale, position.y - 16f * scale), Size(6f * scale, 16f * scale), androidx.compose.ui.geometry.CornerRadius(2f, 2f))
    drawRoundRect(Color(0xFFFFFFFF), Offset(position.x - 8f * scale, position.y - 3f * scale), Size(8f * scale, 3.5f * scale), androidx.compose.ui.geometry.CornerRadius(1f, 1f))
    drawRoundRect(Color(0xFFFFFFFF), Offset(position.x + 1f * scale, position.y - 3f * scale), Size(8f * scale, 3.5f * scale), androidx.compose.ui.geometry.CornerRadius(1f, 1f))

    // Athletic Jersey
    val torsoTopY = position.y - 36f * scale
    drawRoundRect(jerseyColor, Offset(position.x - 9f * scale, torsoTopY), Size(18f * scale, 20f * scale), androidx.compose.ui.geometry.CornerRadius(4f, 4f))

    // Head with Team Cricket Cap & Peak Visor
    val headCenter = Offset(position.x, torsoTopY - 7f * scale)
    drawCircle(skinColor, radius = 7f * scale, center = headCenter)
    // Team cap
    drawArc(jerseyColor, 180f, 180f, true, Offset(headCenter.x - 7.5f * scale, headCenter.y - 7.5f * scale), Size(15f * scale, 15f * scale))
    // Cap visor shade
    drawLine(Color(0xFF0F172A), Offset(headCenter.x - 5f * scale, headCenter.y - 1f * scale), Offset(headCenter.x + 8f * scale, headCenter.y + 1f * scale), strokeWidth = 2.5f * scale)

    // Ready arms (Hands placed on knees / in athletic anticipation)
    drawRoundRect(skinColor, Offset(position.x - 11f * scale, torsoTopY + 6f * scale), Size(5f * scale, 12f * scale), androidx.compose.ui.geometry.CornerRadius(2f, 2f))
    drawRoundRect(skinColor, Offset(position.x + 6f * scale, torsoTopY + 6f * scale), Size(5f * scale, 12f * scale), androidx.compose.ui.geometry.CornerRadius(2f, 2f))
}

private fun DrawScope.drawFieldersPreset(
    w: Float,
    h: Float,
    cx: Float,
    horizonY: Float,
    teamColor: Color,
    preset: FieldPreset,
    profile: GraphicsProfile
) {
    // Fielder coordinates configured for cricket realism
    val fielders = when (preset) {
        FieldPreset.ATTACKING -> listOf(
            Offset(cx + w * 0.08f, h * 0.82f), // First Slip
            Offset(cx + w * 0.14f, h * 0.78f), // Second Slip
            Offset(cx + w * 0.22f, h * 0.68f), // Point
            Offset(cx + w * 0.18f, h * 0.52f), // Cover
            Offset(cx + w * 0.10f, h * 0.38f), // Mid-off
            Offset(cx - w * 0.10f, h * 0.38f), // Mid-on
            Offset(cx - w * 0.18f, h * 0.56f), // Midwicket
            Offset(cx - w * 0.22f, h * 0.72f), // Square Leg
            Offset(cx - w * 0.12f, h * 0.86f)  // Fine Leg
        )
        FieldPreset.DEFENSIVE -> listOf(
            Offset(cx + w * 0.35f, h * 0.58f), // Deep Point
            Offset(cx + w * 0.32f, h * 0.42f), // Deep Extra Cover
            Offset(cx + w * 0.18f, h * 0.28f), // Long-off
            Offset(cx - w * 0.18f, h * 0.28f), // Long-on
            Offset(cx - w * 0.34f, h * 0.44f), // Deep Midwicket
            Offset(cx - w * 0.36f, h * 0.62f), // Deep Square Leg
            Offset(cx - w * 0.20f, h * 0.78f), // Fine Leg
            Offset(cx + w * 0.12f, h * 0.65f), // Point
            Offset(cx + w * 0.16f, h * 0.82f)  // Third Man
        )
        else -> listOf(
            Offset(cx + w * 0.12f, h * 0.80f),
            Offset(cx + w * 0.20f, h * 0.65f),
            Offset(cx + w * 0.18f, h * 0.48f),
            Offset(cx + w * 0.10f, h * 0.36f),
            Offset(cx - w * 0.10f, h * 0.36f),
            Offset(cx - w * 0.20f, h * 0.52f),
            Offset(cx - w * 0.22f, h * 0.70f),
            Offset(cx - w * 0.12f, h * 0.84f),
            Offset(cx + w * 0.28f, h * 0.32f)
        )
    }

    fielders.forEach { pos ->
        val distRatio = (pos.y - horizonY) / (h - horizonY)
        val scale = 0.45f + distRatio * 0.45f
        drawPlayer3D(pos, teamColor, isBatsman = false, scale = scale)
    }
}

private fun DrawScope.drawBall3D(
    progress: Float,
    centerX: Float,
    pitchTopY: Float,
    pitchBottomY: Float,
    exitAngle: Float,
    exitDistance: Float
) {
    val ballRadius = 5.5f

    if (progress <= 0.5f) {
        // Delivery Phase: bowler to striker
        val t = progress / 0.5f
        val currentY = pitchTopY + (pitchBottomY - pitchTopY) * t
        val bounceApex = 40f * sin(Math.PI * t).toFloat()
        val currentX = centerX + (if (t < 0.6f) 4f * t else -3f * (t - 0.6f))

        // Ball shadow on pitch
        drawOval(Color(0x55000000), Offset(currentX - 5f, currentY - 2f), Size(10f, 4f))
        // Cricket ball (Red leather with seam)
        drawCircle(Color(0xFFDC2626), radius = ballRadius, center = Offset(currentX, currentY - bounceApex))
        drawLine(Color.White, Offset(currentX - 3f, currentY - bounceApex - 2f), Offset(currentX + 3f, currentY - bounceApex + 2f), strokeWidth = 1.2f)
    } else {
        // Exit Shot Phase: bat to outfield / boundary
        val t = (progress - 0.5f) / 0.5f
        val rad = Math.toRadians(exitAngle.toDouble())
        val dist = exitDistance.coerceAtLeast(30f) * t * 3.5f
        val currentX = (centerX - 18f) + (dist * sin(rad)).toFloat()
        val currentY = (pitchBottomY - 25f) - (dist * cos(rad)).toFloat()
        val aerialApex = 60f * sin(Math.PI * t).toFloat()

        // Ball shadow
        drawOval(Color(0x44000000), Offset(currentX - 6f, currentY - 2f), Size(12f, 5f))
        // High ball
        drawCircle(Color(0xFFDC2626), radius = ballRadius * (1.0f + 0.3f * (aerialApex / 60f)), center = Offset(currentX, currentY - aerialApex))
        // Glow trail
        drawCircle(Color(0x44F59E0B), radius = ballRadius * 2f, center = Offset(currentX, currentY - aerialApex))
    }
}

private fun DrawScope.drawBehindBatsmanView(
    w: Float, h: Float, profile: GraphicsProfile,
    isDelivering: Boolean, progress: Float, exitAngle: Float, exitDistance: Float,
    batColor: Color, bowlColor: Color
) {
    drawRect(Color(0xFF166534)) // Wide Green Field
    val cx = w * 0.5f
    // Pitch from bottom (batsman end) to top (bowler end)
    val pitchPath = Path().apply {
        moveTo(cx - w * 0.18f, h)
        lineTo(cx + w * 0.18f, h)
        lineTo(cx + w * 0.06f, h * 0.28f)
        lineTo(cx - w * 0.06f, h * 0.28f)
        close()
    }
    drawPath(pitchPath, Color(0xFFC7B198))
    drawStumps(Offset(cx, h * 0.28f + 10f), 0.55f)
    drawStumps(Offset(cx, h * 0.88f), 1.2f)
    drawPlayer3D(Offset(cx, h * 0.94f), batColor, isBatsman = true, scale = 1.4f)
    drawRealisticHumanBowler3D(Offset(cx, h * 0.26f), bowlColor, scale = 0.6f, armAngle = 90f)
}

private fun DrawScope.drawStumpCamView(
    w: Float, h: Float, profile: GraphicsProfile,
    isDelivering: Boolean, progress: Float, batColor: Color, bowlColor: Color
) {
    drawRect(Color(0xFF0F172A))
    // Fisheye wide angle directly looking down the pitch towards the approaching bowler
    drawRect(Color(0xFFC7B198), Offset(w * 0.25f, h * 0.35f), Size(w * 0.5f, h * 0.65f))
    drawPlayer3D(Offset(w * 0.35f, h * 0.58f), batColor, isBatsman = true, scale = 2.0f)
    drawRealisticHumanBowler3D(Offset(w * 0.5f, h * 0.38f), bowlColor, scale = 0.8f, armAngle = 180f)
}

private fun DrawScope.drawDroneView(
    w: Float, h: Float, profile: GraphicsProfile,
    isDelivering: Boolean, progress: Float, exitAngle: Float, exitDistance: Float,
    batColor: Color, bowlColor: Color, preset: FieldPreset
) {
    drawRect(Color(0xFF0A192F))
    // Top-down tactical oval view
    drawOval(Color(0xFF1E6F38), Offset(w * 0.15f, h * 0.08f), Size(w * 0.7f, h * 0.84f))
    drawOval(Color(0x55FFFFFF), Offset(w * 0.28f, h * 0.24f), Size(w * 0.44f, h * 0.52f), style = Stroke(2f))
    drawRect(Color(0xFFC7B198), Offset(w * 0.47f, h * 0.35f), Size(w * 0.06f, h * 0.30f))
    drawCircle(batColor, radius = 8f, center = Offset(w * 0.5f, h * 0.64f))
    drawCircle(bowlColor, radius = 7f, center = Offset(w * 0.5f, h * 0.36f))
}
