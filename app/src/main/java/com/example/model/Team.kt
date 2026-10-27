package com.example.model

import androidx.compose.ui.graphics.Color

data class Team(
    val id: String,
    val nameEn: String,
    val nameUr: String,
    val shortCode: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val cityEn: String,
    val cityUr: String,
    val squad: List<Player>,
    var playingXIIds: List<String>,
    var captainId: String,
    var wicketkeeperId: String
) {
    fun getPlayingXI(): List<Player> {
        val xiMap = squad.associateBy { it.id }
        return playingXIIds.mapNotNull { xiMap[it] }.ifEmpty { squad.take(11) }
    }

    fun getCaptain(): Player = squad.find { it.id == captainId } ?: squad.first()
    fun getWicketkeeper(): Player = squad.find { it.id == wicketkeeperId } ?: squad.first { it.role == PlayerRole.WICKETKEEPER_BAT }
}

object DefaultTeams {
    fun getInitialTeams(): List<Team> {
        return listOf(
            createLahoreLions(),
            createKarachiKingsmen(),
            createIslamabadUniteds(),
            createMelbourneStrikers(),
            createLondonRoyals(),
            createDubaiFalcons(),
            createMumbaiTitans(),
            createSydneyThunderbirds()
        )
    }

    private fun createLahoreLions(): Team {
        val squad = listOf(
            Player("lhr_1", "Babar Zaman", PlayerRole.TOP_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(94, 88, 55, 60, 88, 90), 56),
            Player("lhr_2", "Fakhar Qadir", PlayerRole.TOP_ORDER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_SPIN, PlayerRating(86, 92, 50, 52, 82, 88), 39),
            Player("lhr_3", "Abdullah Shafiq", PlayerRole.TOP_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(84, 80, 52, 55, 85, 86), 14),
            Player("lhr_4", "Rizwan Siddique", PlayerRole.WICKETKEEPER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(91, 85, 45, 50, 95, 96), 16),
            Player("lhr_5", "Salman Agha", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(83, 82, 78, 80, 84, 86), 67),
            Player("lhr_6", "Iftikhar Ahmed", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(80, 95, 75, 76, 80, 84), 95),
            Player("lhr_7", "Shadab Khan", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(82, 85, 88, 87, 94, 90), 7),
            Player("lhr_8", "Shaheen Afridi", PlayerRole.FAST_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_PACE, PlayerRating(70, 78, 96, 92, 90, 94), 10),
            Player("lhr_9", "Naseem Shah", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(65, 72, 94, 90, 88, 92), 71),
            Player("lhr_10", "Haris Rauf", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(58, 65, 95, 86, 85, 95), 150),
            Player("lhr_11", "Abrar Ahmed", PlayerRole.SPIN_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(52, 55, 89, 90, 78, 84), 40),
            Player("lhr_12", "Mohammad Wasim", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(60, 68, 88, 84, 85, 86), 74),
            Player("lhr_13", "Usama Mir", PlayerRole.SPIN_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(62, 70, 86, 85, 80, 82), 24),
            Player("lhr_14", "Saim Ayub", PlayerRole.TOP_ORDER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(82, 86, 68, 70, 86, 85), 63),
            Player("lhr_15", "Azam Khan", PlayerRole.WICKETKEEPER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(74, 95, 40, 40, 68, 70), 77)
        )
        return Team(
            id = "team_lahore",
            nameEn = "Lahore Lions",
            nameUr = "لاہور لائنز",
            shortCode = "LHR",
            primaryColor = Color(0xFF15803D), // Forest Green
            secondaryColor = Color(0xFFFACC15), // Gold
            cityEn = "Lahore",
            cityUr = "لاہور",
            squad = squad,
            playingXIIds = squad.take(11).map { it.id },
            captainId = "lhr_1",
            wicketkeeperId = "lhr_4"
        )
    }

    private fun createKarachiKingsmen(): Team {
        val squad = listOf(
            Player("khi_1", "Shan Masood", PlayerRole.TOP_ORDER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.MEDIUM, PlayerRating(84, 78, 55, 60, 86, 88), 94),
            Player("khi_2", "Sharjeel Khan", PlayerRole.TOP_ORDER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(80, 96, 45, 50, 75, 78), 98),
            Player("khi_3", "Shoaib Malik", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(85, 84, 80, 84, 88, 92), 18),
            Player("khi_4", "Sarfaraz Ahmed", PlayerRole.WICKETKEEPER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(83, 76, 40, 45, 92, 90), 54),
            Player("khi_5", "Imad Wasim", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_SPIN, PlayerRating(82, 86, 89, 93, 85, 88), 9),
            Player("khi_6", "Anwar Ali", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(75, 84, 82, 80, 84, 85), 33),
            Player("khi_7", "Mohammad Amir", PlayerRole.FAST_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_PACE, PlayerRating(68, 75, 93, 91, 88, 90), 5),
            Player("khi_8", "Tabraiz Shamsi", PlayerRole.SPIN_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_SPIN, PlayerRating(50, 52, 91, 88, 76, 82), 68),
            Player("khi_9", "Mir Hamza", PlayerRole.FAST_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_PACE, PlayerRating(54, 58, 88, 90, 82, 85), 31),
            Player("khi_10", "Zahid Mahmood", PlayerRole.SPIN_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(50, 55, 86, 87, 78, 80), 85),
            Player("khi_11", "Akif Javed", PlayerRole.FAST_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.FAST, PlayerRating(52, 54, 89, 84, 80, 82), 22),
            Player("khi_12", "Haider Ali", PlayerRole.MIDDLE_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(78, 88, 48, 50, 82, 84), 45),
            Player("khi_13", "Qasim Akram", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(76, 78, 78, 80, 88, 86), 26),
            Player("khi_14", "Muhammad Akhlaq", PlayerRole.WICKETKEEPER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(74, 80, 42, 45, 80, 82), 12),
            Player("khi_15", "Aamer Yamin", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(75, 82, 84, 82, 80, 82), 48)
        )
        return Team(
            id = "team_karachi",
            nameEn = "Karachi Kingsmen",
            nameUr = "کراچی کنگزمین",
            shortCode = "KHI",
            primaryColor = Color(0xFF1E40AF), // Royal Blue
            secondaryColor = Color(0xFFDC2626), // Crimson
            cityEn = "Karachi",
            cityUr = "کراچی",
            squad = squad,
            playingXIIds = squad.take(11).map { it.id },
            captainId = "khi_1",
            wicketkeeperId = "khi_4"
        )
    }

    private fun createIslamabadUniteds(): Team {
        val squad = listOf(
            Player("isb_1", "Alex Hales", PlayerRole.TOP_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(89, 94, 45, 45, 84, 88), 10),
            Player("isb_2", "Colin Munro", PlayerRole.TOP_ORDER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.MEDIUM, PlayerRating(87, 93, 50, 52, 82, 86), 82),
            Player("lhr_isb3", "Azam Khan", PlayerRole.WICKETKEEPER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(82, 97, 40, 42, 75, 75), 77),
            Player("isb_4", "Asif Ali", PlayerRole.MIDDLE_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(76, 96, 45, 45, 86, 85), 45),
            Player("isb_5", "Shadab Khan", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(84, 87, 90, 88, 96, 92), 7),
            Player("isb_6", "Faheem Ashraf", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.LEFT_HAND, BowlingArchetype.MEDIUM, PlayerRating(78, 86, 86, 85, 86, 88), 41),
            Player("isb_7", "Jordan Cox", PlayerRole.MIDDLE_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(81, 84, 42, 45, 88, 87), 23),
            Player("isb_8", "Naseem Shah", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(68, 74, 95, 92, 90, 94), 71),
            Player("isb_9", "Tymal Mills", PlayerRole.FAST_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_PACE, PlayerRating(55, 60, 91, 85, 82, 86), 56),
            Player("isb_10", "Rumman Raees", PlayerRole.FAST_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_PACE, PlayerRating(58, 62, 88, 91, 84, 86), 9),
            Player("isb_11", "Hunain Shah", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(56, 60, 89, 85, 85, 88), 12),
            Player("isb_12", "Haider Ali", PlayerRole.MIDDLE_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(78, 86, 46, 50, 84, 84), 46),
            Player("isb_13", "Imad Wasim", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_SPIN, PlayerRating(83, 85, 90, 92, 88, 90), 91),
            Player("isb_14", "Qasim Akram", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(76, 78, 79, 82, 87, 86), 27),
            Player("isb_15", "Obed McCoy", PlayerRole.FAST_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_PACE, PlayerRating(55, 62, 88, 83, 79, 82), 61)
        )
        return Team(
            id = "team_islamabad",
            nameEn = "Islamabad Uniteds",
            nameUr = "اسلام آباد یونائیٹڈز",
            shortCode = "ISB",
            primaryColor = Color(0xFFDC2626), // Fire Red
            secondaryColor = Color(0xFFF97316), // Orange
            cityEn = "Islamabad",
            cityUr = "اسلام آباد",
            squad = squad,
            playingXIIds = squad.take(11).map { it.id },
            captainId = "isb_5",
            wicketkeeperId = "lhr_isb3"
        )
    }

    private fun createMelbourneStrikers(): Team {
        val squad = listOf(
            Player("mel_1", "Travis Warner", PlayerRole.TOP_ORDER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(92, 94, 60, 62, 88, 90), 31),
            Player("mel_2", "Matthew Short", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(88, 91, 80, 82, 89, 90), 20),
            Player("mel_3", "Glenn Maxwell", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(89, 98, 82, 80, 95, 92), 32),
            Player("mel_4", "Marcus Stoinis", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(85, 93, 83, 81, 88, 94), 17),
            Player("mel_5", "Alex Carey", PlayerRole.WICKETKEEPER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.MEDIUM, PlayerRating(86, 84, 40, 40, 93, 91), 4),
            Player("mel_6", "Tim David", PlayerRole.MIDDLE_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(80, 99, 52, 50, 86, 92), 85),
            Player("mel_7", "Pat Cummins", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(78, 82, 96, 94, 92, 96), 30),
            Player("mel_8", "Mitchell Starc", PlayerRole.FAST_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_PACE, PlayerRating(65, 75, 98, 88, 86, 92), 56),
            Player("mel_9", "Adam Zampa", PlayerRole.SPIN_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(54, 56, 93, 91, 84, 88), 88),
            Player("mel_10", "Josh Hazlewood", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(58, 62, 95, 97, 85, 92), 38),
            Player("mel_11", "Spencer Johnson", PlayerRole.FAST_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_PACE, PlayerRating(52, 58, 94, 86, 82, 88), 45),
            Player("mel_12", "Aaron Hardie", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(82, 84, 84, 83, 85, 87), 11),
            Player("mel_13", "Cameron Green", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(86, 89, 87, 86, 90, 92), 42),
            Player("mel_14", "Josh Inglis", PlayerRole.WICKETKEEPER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(84, 88, 40, 42, 90, 89), 29),
            Player("mel_15", "Tanveer Sangha", PlayerRole.SPIN_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(50, 52, 87, 86, 80, 82), 26)
        )
        return Team(
            id = "team_melbourne",
            nameEn = "Melbourne Strikers",
            nameUr = "میلبورن اسٹرائیکرز",
            shortCode = "MEL",
            primaryColor = Color(0xFF0284C7), // Sky Cyan
            secondaryColor = Color(0xFF1E293B), // Midnight
            cityEn = "Melbourne",
            cityUr = "میلبورن",
            squad = squad,
            playingXIIds = squad.take(11).map { it.id },
            captainId = "mel_7",
            wicketkeeperId = "mel_5"
        )
    }

    private fun createLondonRoyals(): Team {
        val squad = listOf(
            Player("lon_1", "Jos Buttler", PlayerRole.WICKETKEEPER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(94, 96, 40, 45, 94, 92), 63),
            Player("lon_2", "Phil Salt", PlayerRole.TOP_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(89, 95, 42, 45, 85, 88), 28),
            Player("lon_3", "Jonny Bairstow", PlayerRole.TOP_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(88, 92, 45, 48, 86, 89), 51),
            Player("lon_4", "Harry Brook", PlayerRole.MIDDLE_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(90, 91, 58, 60, 88, 90), 88),
            Player("lon_5", "Liam Livingstone", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(84, 98, 82, 80, 89, 91), 23),
            Player("lon_6", "Moeen Ali", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.LEFT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(84, 88, 85, 86, 87, 88), 18),
            Player("lon_7", "Sam Curran", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_PACE, PlayerRating(82, 85, 88, 89, 91, 92), 58),
            Player("lon_8", "Chris Woakes", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(76, 80, 89, 93, 86, 88), 19),
            Player("lon_9", "Jofra Archer", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(68, 76, 97, 92, 90, 90), 22),
            Player("lon_10", "Adil Rashid", PlayerRole.SPIN_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(55, 58, 94, 93, 82, 86), 95),
            Player("lon_11", "Mark Wood", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(58, 64, 99, 87, 85, 93), 33),
            Player("lon_12", "Reece Topley", PlayerRole.FAST_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_PACE, PlayerRating(52, 56, 89, 91, 80, 84), 83),
            Player("lon_13", "Ben Duckett", PlayerRole.TOP_ORDER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(86, 83, 40, 42, 84, 85), 17),
            Player("lon_14", "Will Jacks", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(83, 89, 78, 80, 86, 87), 29),
            Player("lon_15", "Gus Atkinson", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(60, 68, 92, 88, 84, 87), 37)
        )
        return Team(
            id = "team_london",
            nameEn = "London Royals",
            nameUr = "لندن رائلز",
            shortCode = "LDN",
            primaryColor = Color(0xFF4F46E5), // Indigo
            secondaryColor = Color(0xFFE11D48), // Rose
            cityEn = "London",
            cityUr = "لندن",
            squad = squad,
            playingXIIds = squad.take(11).map { it.id },
            captainId = "lon_1",
            wicketkeeperId = "lon_1"
        )
    }

    private fun createDubaiFalcons(): Team {
        val squad = listOf(
            Player("dub_1", "Muhammad Waseem", PlayerRole.TOP_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(87, 93, 50, 52, 85, 87), 1),
            Player("dub_2", "Rohan Mustafa", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.LEFT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(82, 84, 83, 85, 86, 88), 7),
            Player("dub_3", "Vriitya Aravind", PlayerRole.WICKETKEEPER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(84, 82, 40, 42, 90, 86), 14),
            Player("dub_4", "Basil Hameed", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(80, 85, 82, 83, 85, 85), 12),
            Player("dub_5", "Asif Khan", PlayerRole.MIDDLE_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(83, 91, 50, 52, 82, 84), 48),
            Player("dub_6", "Aayan Afzal Khan", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.LEFT_ARM_SPIN, PlayerRating(78, 82, 88, 90, 89, 90), 8),
            Player("dub_7", "Junaid Siddique", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(62, 68, 90, 88, 84, 87), 19),
            Player("dub_8", "Zahoor Khan", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(60, 65, 87, 91, 82, 86), 25),
            Player("dub_9", "Karthik Meiyappan", PlayerRole.SPIN_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(54, 58, 89, 87, 80, 84), 99),
            Player("dub_10", "Ali Naseer", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.LEFT_HAND, BowlingArchetype.FAST, PlayerRating(75, 80, 88, 85, 86, 88), 17),
            Player("dub_11", "Zawar Farid", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(55, 62, 86, 84, 81, 83), 33),
            Player("dub_12", "Chirag Suri", PlayerRole.TOP_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(80, 81, 45, 48, 83, 84), 3),
            Player("dub_13", "Aryansh Sharma", PlayerRole.WICKETKEEPER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(78, 79, 40, 42, 85, 84), 16),
            Player("dub_14", "Hazrat Luqman", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(56, 64, 85, 83, 79, 81), 44),
            Player("dub_15", "Nilansh Keswani", PlayerRole.SPIN_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_SPIN, PlayerRating(52, 54, 84, 85, 80, 81), 22)
        )
        return Team(
            id = "team_dubai",
            nameEn = "Dubai Falcons",
            nameUr = "دبئی فالکنز",
            shortCode = "DUB",
            primaryColor = Color(0xFFD97706), // Amber Gold
            secondaryColor = Color(0xFF0F172A), // Dark Slate
            cityEn = "Dubai",
            cityUr = "دبئی",
            squad = squad,
            playingXIIds = squad.take(11).map { it.id },
            captainId = "dub_1",
            wicketkeeperId = "dub_3"
        )
    }

    private fun createMumbaiTitans(): Team {
        val squad = listOf(
            Player("mum_1", "Rohit Sharma", PlayerRole.TOP_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(93, 95, 52, 55, 86, 88), 45),
            Player("mum_2", "Ishan Kishan", PlayerRole.WICKETKEEPER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.MEDIUM, PlayerRating(86, 92, 40, 40, 90, 89), 23),
            Player("mum_3", "Suryakumar Yadav", PlayerRole.MIDDLE_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(96, 97, 50, 52, 92, 91), 63),
            Player("mum_4", "Tilak Varma", PlayerRole.MIDDLE_ORDER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(86, 88, 70, 72, 88, 88), 9),
            Player("mum_5", "Hardik Pandya", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(87, 94, 89, 88, 93, 94), 33),
            Player("mum_6", "Tim David", PlayerRole.MIDDLE_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(82, 98, 55, 52, 85, 90), 85),
            Player("mum_7", "Gerald Coetzee", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(65, 75, 95, 88, 86, 91), 62),
            Player("mum_8", "Jasprit Bumrah", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(70, 74, 99, 98, 94, 96), 93),
            Player("mum_9", "Piyush Chawla", PlayerRole.SPIN_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(60, 68, 89, 92, 78, 80), 11),
            Player("mum_10", "Nuwan Thushara", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(55, 60, 92, 89, 82, 86), 40),
            Player("mum_11", "Akash Madhwal", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(56, 62, 89, 90, 84, 87), 98),
            Player("mum_12", "Dewald Brevis", PlayerRole.MIDDLE_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(82, 91, 62, 60, 86, 86), 17),
            Player("mum_13", "Nehal Wadhera", PlayerRole.MIDDLE_ORDER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(81, 87, 65, 66, 84, 85), 21),
            Player("mum_14", "Romario Shepherd", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(77, 95, 86, 82, 85, 88), 90),
            Player("mum_15", "Kumar Kartikeya", PlayerRole.SPIN_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.LEFT_ARM_SPIN, PlayerRating(50, 52, 86, 88, 80, 82), 26)
        )
        return Team(
            id = "team_mumbai",
            nameEn = "Mumbai Titans",
            nameUr = "ممبئی ٹائٹنز",
            shortCode = "MUM",
            primaryColor = Color(0xFF0369A1), // Ocean Blue
            secondaryColor = Color(0xFFEAB308), // Bright Gold
            cityEn = "Mumbai",
            cityUr = "ممبئی",
            squad = squad,
            playingXIIds = squad.take(11).map { it.id },
            captainId = "mum_5",
            wicketkeeperId = "mum_2"
        )
    }

    private fun createSydneyThunderbirds(): Team {
        val squad = listOf(
            Player("syd_1", "David Warner", PlayerRole.TOP_ORDER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(91, 93, 50, 50, 89, 89), 31),
            Player("syd_2", "Josh Philippe", PlayerRole.WICKETKEEPER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(85, 89, 40, 42, 91, 90), 22),
            Player("syd_3", "Steven Smith", PlayerRole.TOP_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(95, 84, 65, 68, 94, 92), 49),
            Player("syd_4", "Moises Henriques", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(84, 86, 81, 83, 88, 88), 21),
            Player("syd_5", "Jordan Silk", PlayerRole.MIDDLE_ORDER_BAT, BattingHand.RIGHT_HAND, BowlingArchetype.MEDIUM, PlayerRating(83, 85, 40, 42, 97, 93), 14),
            Player("syd_6", "Sean Abbott", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(79, 86, 91, 90, 92, 91), 77),
            Player("syd_7", "Ben Dwarshuis", PlayerRole.FAST_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.LEFT_ARM_PACE, PlayerRating(70, 78, 89, 90, 86, 88), 82),
            Player("syd_8", "Jackson Bird", PlayerRole.FAST_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(55, 60, 88, 93, 80, 84), 9),
            Player("syd_9", "Todd Murphy", PlayerRole.SPIN_BOWLER, BattingHand.LEFT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(54, 56, 90, 92, 83, 85), 36),
            Player("syd_10", "Steve O'Keefe", PlayerRole.SPIN_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.LEFT_ARM_SPIN, PlayerRating(60, 64, 89, 94, 82, 84), 72),
            Player("syd_11", "Hayden Kerr", PlayerRole.BOWLING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.LEFT_ARM_PACE, PlayerRating(76, 82, 87, 86, 88, 88), 16),
            Player("syd_12", "Daniel Hughes", PlayerRole.TOP_ORDER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.MEDIUM, PlayerRating(82, 83, 40, 42, 85, 86), 18),
            Player("syd_13", "Jack Edwards", PlayerRole.BATTING_ALL_ROUNDER, BattingHand.RIGHT_HAND, BowlingArchetype.FAST, PlayerRating(80, 83, 84, 83, 88, 88), 12),
            Player("syd_14", "Kurtis Patterson", PlayerRole.TOP_ORDER_BAT, BattingHand.LEFT_HAND, BowlingArchetype.OFF_SPIN, PlayerRating(80, 80, 40, 42, 84, 84), 5),
            Player("syd_15", "Izharulhaq Naveed", PlayerRole.SPIN_BOWLER, BattingHand.RIGHT_HAND, BowlingArchetype.LEG_SPIN, PlayerRating(50, 52, 86, 84, 78, 80), 91)
        )
        return Team(
            id = "team_sydney",
            nameEn = "Sydney Thunderbirds",
            nameUr = "سڈنی تھنڈر برڈز",
            shortCode = "SYD",
            primaryColor = Color(0xFFE11D48), // Magenta Rose
            secondaryColor = Color(0xFF1E293B), // Navy
            cityEn = "Sydney",
            cityUr = "سڈنی",
            squad = squad,
            playingXIIds = squad.take(11).map { it.id },
            captainId = "syd_4",
            wicketkeeperId = "syd_2"
        )
    }
}
