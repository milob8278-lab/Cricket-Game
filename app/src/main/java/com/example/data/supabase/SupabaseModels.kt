package com.example.data.supabase

data class SupabaseHealthReport(
    val status: String, // CONNECTED, DEGRADED, CONFIGURATION_ERROR, OFFLINE
    val url: String,
    val maskedKey: String,
    val latencyMs: Long,
    val authStatus: String,
    val databaseStatus: String,
    val tables: Map<String, String> = emptyMap(),
    val errorMessage: String? = null
)

data class SupabaseAuthSession(
    val isAuthenticated: Boolean = false,
    val accessToken: String? = null,
    val userId: String? = null,
    val email: String? = null,
    val displayName: String? = null,
    val error: String? = null
)

data class SupabaseMatchRecord(
    val id: String,
    val format: String,
    val maxOvers: Int,
    val venueName: String,
    val teamAName: String,
    val teamBName: String,
    val winnerTeamName: String,
    val scoreSummary: String,
    val isPlayerWon: Boolean,
    val ratingChange: Int,
    val createdAt: String
)
