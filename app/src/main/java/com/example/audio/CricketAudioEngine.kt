package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class CricketAudioEngine(private val context: Context) {

    private val audioScope = CoroutineScope(Dispatchers.Default)
    private val sampleRate = 22050

    var isAudioEnabled: Boolean = true
    var isHapticsEnabled: Boolean = true
    var masterVolume: Float = 1.0f

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    fun playBatCrack() {
        if (!isAudioEnabled) return
        triggerHaptic(40, 200)
        audioScope.launch {
            // Wood crack sound: fast decaying burst with sharp transient
            val durationMs = 120
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toFloat() / sampleRate
                val envelope = kotlin.math.exp(-35.0 * t).toFloat()
                val freq = 900.0 - (400.0 * (i.toDouble() / numSamples))
                val wave = sin(2.0 * Math.PI * freq * t) + 0.5 * sin(2.0 * Math.PI * (freq * 1.6) * t)
                buffer[i] = (wave * envelope * Short.MAX_VALUE * 0.85f * masterVolume).toInt().toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playStumpClatter() {
        if (!isAudioEnabled) return
        triggerHaptic(80, 255)
        audioScope.launch {
            val durationMs = 280
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toFloat() / sampleRate
                val envelope = kotlin.math.exp(-15.0 * t).toFloat()
                val wave1 = sin(2.0 * Math.PI * 520.0 * t)
                val wave2 = sin(2.0 * Math.PI * 880.0 * t)
                val bailsChime = sin(2.0 * Math.PI * 1850.0 * t) * kotlin.math.exp(-25.0 * t)
                val mixed = (wave1 * 0.5 + wave2 * 0.3 + bailsChime * 0.4) * envelope
                buffer[i] = (mixed * Short.MAX_VALUE * 0.9f * masterVolume).toInt().toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playBoundaryFanfare() {
        if (!isAudioEnabled) return
        triggerHaptic(60, 180)
        audioScope.launch {
            // Uplifting stadium chime C5 -> E5 -> G5
            val durationMs = 400
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)
            val notes = doubleArrayOf(523.25, 659.25, 783.99, 1046.50) // C5, E5, G5, C6
            val noteDuration = numSamples / notes.size

            for (i in 0 until numSamples) {
                val noteIdx = (i / noteDuration).coerceIn(0, notes.lastIndex)
                val freq = notes[noteIdx]
                val t = (i % noteDuration).toFloat() / sampleRate
                val env = kotlin.math.exp(-8.0 * t).toFloat()
                val wave = sin(2.0 * Math.PI * freq * t)
                buffer[i] = (wave * env * Short.MAX_VALUE * 0.7f * masterVolume).toInt().toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playSixRoar() {
        if (!isAudioEnabled) return
        triggerHaptic(120, 255)
        audioScope.launch {
            // Thunderous stadium explosion + rising cheer
            val durationMs = 600
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toFloat() / sampleRate
                val env = (1.0f - kotlin.math.exp(-10.0 * t).toFloat()) * kotlin.math.exp(-3.5 * t).toFloat()
                val bass = sin(2.0 * Math.PI * 140.0 * t)
                val horn = sin(2.0 * Math.PI * (440.0 + 80.0 * t) * t)
                val mixed = (bass * 0.6 + horn * 0.4) * env
                buffer[i] = (mixed * Short.MAX_VALUE * 0.95f * masterVolume).toInt().toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playAppeal() {
        if (!isAudioEnabled) return
        triggerHaptic(30, 150)
        audioScope.launch {
            val durationMs = 300
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toFloat() / sampleRate
                val freq = 300.0 + (350.0 * (i.toDouble() / numSamples))
                val env = sin(Math.PI * (i.toDouble() / numSamples)).toFloat()
                val wave = sin(2.0 * Math.PI * freq * t)
                buffer[i] = (wave * env * Short.MAX_VALUE * 0.7f * masterVolume).toInt().toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    private fun playPcmBuffer(buffer: ShortArray) {
        try {
            val minBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2.coerceAtLeast(minBufferSize))
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            audioScope.launch {
                kotlinx.coroutines.delay(1000)
                audioTrack.release()
            }
        } catch (e: Exception) {
            // AudioTrack failure handled safely
        }
    }

    private fun triggerHaptic(durationMs: Long, amplitude: Int = 180) {
        if (!isHapticsEnabled || vibrator == null) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, amplitude.coerceIn(1, 255)))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }
}
