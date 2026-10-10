package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

data class AchievementBadge(
    val id: String,
    val title: String,
    val desc: String,
    val iconEmoji: String,
    val isUnlocked: Boolean = false
)

data class GameMatchRecord(
    val id: String,
    val gameId: String,
    val gameTitle: String,
    val score: Int,
    val accuracyPercent: Int,
    val reactionTimeMs: Int,
    val timestamp: Long,
    val isPersonalBest: Boolean,
    val streakAtMatch: Int
)

data class GamePersonalBest(
    val gameId: String,
    val gameTitle: String,
    val highScore: Int,
    val bestStreak: Int,
    val bestAccuracy: Int,
    val dateAchieved: String
)

data class CustomWisdom(
    val id: String,
    val quote: String,
    val author: String,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class TreeState(
    val growthDays: Int = 4,
    val auraFruits: Int = 35,
    val gardenTreesCount: Int = 1,
    val streakShields: Int = 1,
    val lastCheckInDate: String = "",
    val penaltyNotice: String? = null,
    val nextWateringHours: Int = 8,
    val isWateredToday: Boolean = true,
    val unlockedRewards: Set<String> = setOf("reward_starter_pack")
) {
    val stage: Int
        get() = when {
            growthDays <= 1 -> 1 // Seed (বীজ) 🌰
            growthDays <= 3 -> 2 // Sprout (অঙ্কুর) 🌱
            growthDays <= 6 -> 3 // Growing Tree (গাছ) 🌿
            growthDays <= 10 -> 4 // Mature Spreading Tree 🌳
            growthDays <= 15 -> 5 // Food & Aura Fruits (ফল) 🍎
            else -> 6 // Flourishing Zen Sanctuary Garden (বাগান) 🏞️
        }

    val stageTitle: String
        get() = when (stage) {
            1 -> "Sacred Seed (বীজ)"
            2 -> "Living Sprout (অঙ্কুর)"
            3 -> "Growing Tree (গাছ)"
            4 -> "Majestic Mature Tree"
            5 -> "Aura Fruit Harvest (Food / ফল)"
            else -> "Zen Sanctuary Garden (বাগান)"
        }

    val stageEmoji: String
        get() = when (stage) {
            1 -> "🌰"
            2 -> "🌱"
            3 -> "🌿"
            4 -> "🌳"
            5 -> "🍎"
            else -> "🏞️"
        }
}

data class DailyMoodCheckIn(
    val date: String,
    val moodEmoji: String,
    val moodLabel: String,
    val energyLevel: Int, // 1 to 10
    val primaryIntent: String,
    val plannedFocusMinutes: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class GamificationState(
    val currentLevel: Int = 4,
    val currentXp: Int = 420,
    val xpForNextLevel: Int = 650,
    val totalFocusMinutes: Int = 240,
    val habitsCompletedTotal: Int = 28,
    val gamesPlayedTotal: Int = 14,
    val currentStreakDays: Int = 5,
    val rankTitle: String = "Focus Adept",
    val userName: String = "Focus Voyager",
    val userBio: String = "Seeking deep clarity, flow state & daily calm.",
    val userAvatarEmoji: String = "⚡",
    val musicPlayerDesign: Int = 0, // 0: Circular Vinyl, 1: Circular Aura Sphere, 2: Horizontal Waveform, 3: Horizontal Cassette, 4: Horizontal Soundbar
    val lastMoodCheckInDate: String? = null,
    val moodCheckInHistory: List<DailyMoodCheckIn> = listOf(
        DailyMoodCheckIn("Yesterday", "⚡", "High Energy", 9, "Build peak cognitive performance", 150, System.currentTimeMillis() - 86400000),
        DailyMoodCheckIn("2 days ago", "🧘", "Deep Focus", 8, "Deep work math sprint & habits", 120, System.currentTimeMillis() - 172800000),
        DailyMoodCheckIn("3 days ago", "🌊", "Calm Zen", 7, "Stoic reflection & binaural meditation", 90, System.currentTimeMillis() - 259200000)
    ),
    val tree: TreeState = TreeState(),
    val matchHistory: List<GameMatchRecord> = listOf(
        GameMatchRecord("m1", "g1", "Mental Math Sprint", 120, 95, 230, System.currentTimeMillis() - 7200000, true, 3),
        GameMatchRecord("m2", "g2", "Stroop Color Clash", 150, 90, 310, System.currentTimeMillis() - 14400000, true, 2),
        GameMatchRecord("m3", "g4", "Speed Sequence", 180, 100, 195, System.currentTimeMillis() - 21600000, true, 4)
    ),
    val personalBests: Map<String, GamePersonalBest> = mapOf(
        "g1" to GamePersonalBest("g1", "Mental Math Sprint", 120, 3, 95, "Today"),
        "g2" to GamePersonalBest("g2", "Stroop Color Clash", 150, 2, 90, "Today"),
        "g4" to GamePersonalBest("g4", "Speed Sequence", 180, 4, 100, "Today")
    ),
    val gameStreaks: Map<String, Int> = mapOf(
        "g1" to 3,
        "g2" to 2,
        "g4" to 4
    ),
    val customWisdomList: List<CustomWisdom> = listOf(
        CustomWisdom("w1", "Discipline is choosing between what you want now, and what you want most.", "Abraham Lincoln", "Focus & Willpower"),
        CustomWisdom("w2", "You have power over your mind - not outside events. Realize this, and you will find strength.", "Marcus Aurelius", "Mental Fortitude"),
        CustomWisdom("w3", "Simplicity is the prerequisite for great productivity.", "Edsger Dijkstra", "Clarity")
    ),
    val badges: List<AchievementBadge> = listOf(
        AchievementBadge("b1", "Lone Wolf Initiate", "Completed first 40Hz Lone Wolf deep focus", "🐺", true),
        AchievementBadge("b2", "Alpha Synced", "Mastered 5 Alpha brainwave flow sessions", "⚡", true),
        AchievementBadge("b3", "Flamekeeper", "Kept an unbroken 7-day habit streak alive", "🔥", false),
        AchievementBadge("b4", "Math Sprint Prodigy", "Solved rapid mental arithmetic challenge", "🧮", true),
        AchievementBadge("b5", "Acoustic Alchemist", "Mixed 3 layers in Soundscape Stacker", "🎛️", true),
        AchievementBadge("b6", "Zenith Stoic", "Accumulated 300+ minutes of deep focus", "🧘", false),
        AchievementBadge("b7", "Garden Arborist", "Grew the Daily Login Tree to Stage 4 Bonsai", "🌸", true),
        AchievementBadge("b8", "Record Breaker", "Set 5 new game high score personal bests", "🏆", true)
    )
)

object GamificationManager {
    private val _state = MutableStateFlow(GamificationState())
    val state: StateFlow<GamificationState> = _state.asStateFlow()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun addXp(amount: Int, reason: String = "") {
        val current = _state.value
        var newXp = current.currentXp + amount
        var newLevel = current.currentLevel
        var targetXp = current.xpForNextLevel

        while (newXp >= targetXp) {
            newXp -= targetXp
            newLevel++
            targetXp = (targetXp * 1.35f).toInt()
        }

        val rank = when {
            newLevel < 3 -> "Zen Apprentice"
            newLevel < 6 -> "Flow Practitioner"
            newLevel < 10 -> "Focus Adept"
            newLevel < 15 -> "Cognitive Master"
            else -> "Lone Wolf Grandmaster"
        }

        _state.value = current.copy(
            currentLevel = newLevel,
            currentXp = newXp,
            xpForNextLevel = targetXp,
            rankTitle = rank
        )
    }

    fun recordFocusSession(minutes: Int) {
        addXp(minutes * 4, "Focus Session")
        val current = _state.value
        _state.value = current.copy(totalFocusMinutes = current.totalFocusMinutes + minutes)
    }

    fun recordHabitCompleted() {
        addXp(50, "Habit Check-in")
        val current = _state.value
        _state.value = current.copy(habitsCompletedTotal = current.habitsCompletedTotal + 1)
    }

    fun recordGamePlayed(count: Int = 1) {
        addXp(30 * count, "Brain Training Game")
        val current = _state.value
        _state.value = current.copy(gamesPlayedTotal = current.gamesPlayedTotal + count)
    }

    fun recordSessionCompleted(minutes: Int = 25) {
        recordFocusSession(minutes)
    }

    fun claimDailyBonus(): Int {
        return waterAndHarvestTree()
    }

    fun updateProfile(name: String, bio: String, avatar: String) {
        val current = _state.value
        _state.value = current.copy(
            userName = name.trim().ifEmpty { "Focus Voyager" },
            userBio = bio.trim().ifEmpty { "Seeking deep clarity & flow." },
            userAvatarEmoji = avatar.ifEmpty { "⚡" }
        )
    }

    // =========================================================================
    // DAILY LOGIN STREAK & GROWING TREE / ZEN GARDEN ENGINE
    // =========================================================================

    /**
     * Called on app startup or manual water check-in.
     * Rule: Consecutive day -> +1 day growth & +10 fruits.
     * Rule: Missed a day -> 2/3 of growth is lost! (1/3 remains).
     */
    fun checkDailyLogin() {
        val current = _state.value
        val todayStr = dateFormat.format(Date())
        val tree = current.tree

        if (tree.lastCheckInDate.isEmpty()) {
            _state.value = current.copy(
                tree = tree.copy(
                    lastCheckInDate = todayStr,
                    isWateredToday = true
                )
            )
            return
        }

        if (tree.lastCheckInDate == todayStr) {
            // Already checked in today
            return
        }

        // Calculate day difference
        try {
            val lastDate = dateFormat.parse(tree.lastCheckInDate)
            val todayDate = dateFormat.parse(todayStr)
            val diffMs = (todayDate?.time ?: 0L) - (lastDate?.time ?: 0L)
            val diffDays = (diffMs / (1000 * 60 * 60 * 24)).toInt()

            if (diffDays == 1) {
                // Continuous Streak! Tree grows!
                val newGrowth = tree.growthDays + 1
                val newStreak = current.currentStreakDays + 1
                val newGardenCount = if (newGrowth >= 12) max(1, newGrowth / 5) else 1
                val fruitsBonus = 10 + (tree.stage * 3)

                _state.value = current.copy(
                    currentStreakDays = newStreak,
                    tree = tree.copy(
                        growthDays = newGrowth,
                        auraFruits = tree.auraFruits + fruitsBonus,
                        gardenTreesCount = newGardenCount,
                        lastCheckInDate = todayStr,
                        isWateredToday = true,
                        penaltyNotice = null
                    )
                )
                addXp(120, "Daily Tree Growth")
            } else if (diffDays > 1) {
                // MISSED DAY! Apply Psychological Loss Penalty:
                // User loses 2/3 of growth! (1/3 remains)
                val retainedGrowth = max(1, tree.growthDays / 3)
                val lostDays = tree.growthDays - retainedGrowth

                _state.value = current.copy(
                    currentStreakDays = 1, // Streak reset to 1
                    tree = tree.copy(
                        growthDays = retainedGrowth,
                        lastCheckInDate = todayStr,
                        isWateredToday = true,
                        penaltyNotice = "⚠️ Missed $diffDays days! Tree withered and lost $lostDays days of growth (retained $retainedGrowth). Keep login streak unbroken to protect it!"
                    )
                )
            }
        } catch (_: Exception) {
            _state.value = current.copy(
                tree = tree.copy(lastCheckInDate = todayStr, isWateredToday = true)
            )
        }
    }

    /**
     * Water & harvest the tree for daily fruit drop.
     */
    fun waterAndHarvestTree(): Int {
        val current = _state.value
        val tree = current.tree
        val fruitsGained = 15 + (tree.stage * 5)
        val newGrowth = tree.growthDays + 1
        val newGardenCount = if (newGrowth >= 12) max(1, newGrowth / 4) else tree.gardenTreesCount

        _state.value = current.copy(
            tree = tree.copy(
                growthDays = newGrowth,
                auraFruits = tree.auraFruits + fruitsGained,
                gardenTreesCount = newGardenCount,
                isWateredToday = true,
                penaltyNotice = null
            )
        )
        addXp(100, "Zen Tree Watered")
        return fruitsGained
    }

    fun spendAuraFruits(amount: Int, rewardId: String): Boolean {
        val current = _state.value
        val tree = current.tree
        if (tree.auraFruits >= amount) {
            _state.value = current.copy(
                tree = tree.copy(
                    auraFruits = tree.auraFruits - amount,
                    unlockedRewards = tree.unlockedRewards + rewardId
                )
            )
            addXp(50, "Reward Unlocked")
            return true
        }
        return false
    }

    // =========================================================================
    // GAME MATCH HISTORY & PERSONAL BESTS TRACKER
    // =========================================================================

    fun recordGameMatch(
        gameId: String,
        gameTitle: String,
        score: Int,
        accuracyPercent: Int = 90,
        reactionTimeMs: Int = 260
    ): Boolean {
        val current = _state.value
        val previousPb = current.personalBests[gameId]
        val isNewPb = previousPb == null || score > previousPb.highScore

        // Update streak for this game
        val currentStreak = (current.gameStreaks[gameId] ?: 0) + 1
        val newStreaks = current.gameStreaks.toMutableMap().apply {
            put(gameId, currentStreak)
        }

        // Update Personal Best
        val newPbMap = current.personalBests.toMutableMap()
        if (isNewPb) {
            val dateStr = SimpleDateFormat("MMM d", Locale.US).format(Date())
            newPbMap[gameId] = GamePersonalBest(
                gameId = gameId,
                gameTitle = gameTitle,
                highScore = score,
                bestStreak = max(previousPb?.bestStreak ?: 0, currentStreak),
                bestAccuracy = max(previousPb?.bestAccuracy ?: 0, accuracyPercent),
                dateAchieved = dateStr
            )
        }

        // New match history entry
        val match = GameMatchRecord(
            id = System.currentTimeMillis().toString(),
            gameId = gameId,
            gameTitle = gameTitle,
            score = score,
            accuracyPercent = accuracyPercent,
            reactionTimeMs = reactionTimeMs,
            timestamp = System.currentTimeMillis(),
            isPersonalBest = isNewPb,
            streakAtMatch = currentStreak
        )

        val updatedHistory = (listOf(match) + current.matchHistory).take(35)

        _state.value = current.copy(
            gamesPlayedTotal = current.gamesPlayedTotal + 1,
            matchHistory = updatedHistory,
            personalBests = newPbMap,
            gameStreaks = newStreaks
        )

        // Award XP and fruits for playing and breaking records
        val bonusXp = if (isNewPb) 150 else 60
        addXp(bonusXp, if (isNewPb) "New Personal Best!" else "Brain Match Finished")

        if (isNewPb) {
            _state.value = _state.value.copy(
                tree = _state.value.tree.copy(auraFruits = _state.value.tree.auraFruits + 15)
            )
        }

        return isNewPb
    }

    // =========================================================================
    // CUSTOM STOIC WISDOM / QUOTES SYSTEM
    // =========================================================================

    fun addCustomWisdom(quote: String, author: String, category: String) {
        val current = _state.value
        val newQuote = CustomWisdom(
            id = System.currentTimeMillis().toString(),
            quote = quote.trim(),
            author = author.trim().ifEmpty { "Personal Reflection" },
            category = category.trim().ifEmpty { "Mindful Clarity" }
        )
        _state.value = current.copy(
            customWisdomList = listOf(newQuote) + current.customWisdomList
        )
        addXp(50, "Wisdom Added")
    }

    fun removeCustomWisdom(id: String) {
        val current = _state.value
        _state.value = current.copy(
            customWisdomList = current.customWisdomList.filterNot { it.id == id }
        )
    }

    // =========================================================================
    // DAILY MORNING MOOD & PRODUCTIVITY CHECK-IN
    // =========================================================================

    fun recordDailyCheckIn(
        moodEmoji: String,
        moodLabel: String,
        energyLevel: Int,
        primaryIntent: String,
        plannedFocusMinutes: Int
    ) {
        val today = dateFormat.format(Date())
        val checkIn = DailyMoodCheckIn(
            date = today,
            moodEmoji = moodEmoji,
            moodLabel = moodLabel,
            energyLevel = energyLevel,
            primaryIntent = primaryIntent,
            plannedFocusMinutes = plannedFocusMinutes
        )
        val current = _state.value
        val updatedHistory = listOf(checkIn) + current.moodCheckInHistory.filter { it.date != today }
        _state.value = current.copy(
            lastMoodCheckInDate = today,
            moodCheckInHistory = updatedHistory
        )
        addXp(60, "Daily Morning Check-in")
    }

    fun setMusicPlayerDesign(designIndex: Int) {
        val clamped = designIndex.coerceIn(0, 4)
        _state.value = _state.value.copy(musicPlayerDesign = clamped)
    }
}

