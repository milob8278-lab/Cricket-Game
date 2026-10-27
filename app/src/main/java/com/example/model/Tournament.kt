package com.example.model

data class TournamentTeamStats(
    val team: Team,
    var matchesPlayed: Int = 0,
    var won: Int = 0,
    var lost: Int = 0,
    var tied: Int = 0,
    var points: Int = 0,
    var totalRunsScored: Int = 0,
    var totalOversFaced: Double = 0.0,
    var totalRunsConceded: Int = 0,
    var totalOversBowled: Double = 0.0
) {
    val netRunRate: Double
        get() {
            val forRate = if (totalOversFaced > 0) totalRunsScored / totalOversFaced else 0.0
            val againstRate = if (totalOversBowled > 0) totalRunsConceded / totalOversBowled else 0.0
            return forRate - againstRate
        }
}

data class TournamentFixture(
    val id: String,
    val roundName: String,
    val teamA: Team,
    val teamB: Team,
    var isCompleted: Boolean = false,
    var winnerTeam: Team? = null,
    var scoreSummary: String = ""
)

data class TournamentState(
    val nameEn: String = "Champions Super League",
    val nameUr: String = "چیمپئنز سپر لیگ",
    val format: CricketFormat = CricketFormat.T20,
    val teams: List<Team>,
    var standings: MutableList<TournamentTeamStats> = mutableListOf(),
    var fixtures: MutableList<TournamentFixture> = mutableListOf(),
    var currentRoundIndex: Int = 0,
    var isTournamentWon: Boolean = false,
    var trophyWinner: Team? = null
)
