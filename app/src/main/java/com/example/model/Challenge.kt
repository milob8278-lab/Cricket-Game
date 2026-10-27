package com.example.model

data class CricketChallenge(
    val id: String,
    val titleEn: String,
    val titleUr: String,
    val scenarioEn: String,
    val scenarioUr: String,
    val format: CricketFormat,
    val targetRuns: Int,
    val ballsRemaining: Int,
    val wicketsInHand: Int,
    val starDifficulty: Int, // 1 to 5
    var isCompleted: Boolean = false,
    var starsEarned: Int = 0
)

object DefaultChallenges {
    fun getChallenges(): List<CricketChallenge> {
        return listOf(
            CricketChallenge(
                id = "ch_1",
                titleEn = "Death Over Thriller",
                titleUr = "آخری اوور کا سنسنی خیز معرکہ",
                scenarioEn = "Score 18 runs in the final over with only 2 wickets remaining against express pace!",
                scenarioUr = "آخری اوور میں 18 رنز درکار ہیں اور صرف 2 وکٹیں باقی ہیں!",
                format = CricketFormat.T20,
                targetRuns = 18,
                ballsRemaining = 6,
                wicketsInHand = 2,
                starDifficulty = 3
            ),
            CricketChallenge(
                id = "ch_2",
                titleEn = "Defend the Fortress",
                titleUr = "قلعہ کا دفاع",
                scenarioEn = "You must defend 9 runs off the last 6 balls. Bowler has Yorker & Slower balls.",
                scenarioUr = "آخری 6 گیندوں پر 9 رنز کا کامیابی سے دفاع کریں۔",
                format = CricketFormat.T10,
                targetRuns = 9,
                ballsRemaining = 6,
                wicketsInHand = 3,
                starDifficulty = 4
            ),
            CricketChallenge(
                id = "ch_3",
                titleEn = "Twin Maximums",
                titleUr = "مسلسل دو چھکے",
                scenarioEn = "Hit back-to-back sixes on the next two deliveries to snatch a miracle victory!",
                scenarioUr = "فتح چھیننے کے لیے اگلی دو گیندوں پر مسلسل دو فلک شگاف چھکے لگائیں!",
                format = CricketFormat.T5,
                targetRuns = 12,
                ballsRemaining = 2,
                wicketsInHand = 1,
                starDifficulty = 5
            ),
            CricketChallenge(
                id = "ch_4",
                titleEn = "Spin Web Mastery",
                titleUr = "اسپنرز کا شکنجہ",
                scenarioEn = "Dry wicket spinning viciously. Take 2 wickets in 12 balls without conceding more than 8 runs.",
                scenarioUr = "خشک پچ پر 12 گیندوں میں 2 وکٹیں حاصل کریں اور 8 سے زائد رنز نہ دیں۔",
                format = CricketFormat.T20,
                targetRuns = 8,
                ballsRemaining = 12,
                wicketsInHand = 2,
                starDifficulty = 3
            ),
            CricketChallenge(
                id = "ch_5",
                titleEn = "Super Over Showdown",
                titleUr = "سپر اوور کا فائنل مقابلہ",
                scenarioEn = "Match tied at the MCG! Chase 15 runs in the sudden-death Super Over.",
                scenarioUr = "میچ برابر ہو گیا! سپر اوور میں 15 رنز کا ہدف حاصل کریں۔",
                format = CricketFormat.T20,
                targetRuns = 15,
                ballsRemaining = 6,
                wicketsInHand = 2,
                starDifficulty = 4
            )
        )
    }
}
