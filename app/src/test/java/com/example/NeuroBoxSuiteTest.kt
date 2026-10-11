package com.example

import com.example.data.GamificationManager
import com.example.data.TreeState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NeuroBoxSuiteTest {

    @Before
    fun setUp() {
        GamificationManager.resetAllUserDataToZero()
    }

    @Test
    fun `test initial state starts completely at zero`() {
        val state = GamificationManager.state.value
        assertEquals(0, state.currentXp)
        assertEquals(1, state.currentLevel)
        assertEquals(0, state.totalFocusMinutes)
        assertEquals(0, state.gamesPlayedTotal)
        assertEquals(0, state.currentStreakDays)
        assertEquals(0, state.tree.auraFruits)
        assertEquals(1, state.tree.growthDays)
        assertEquals("Zen Apprentice", state.rankTitle)
    }

    @Test
    fun `test addXp calculates leveling and promotions`() {
        GamificationManager.addXp(120, "Test Bonus")
        val state = GamificationManager.state.value
        assertEquals(2, state.currentLevel)
        assertTrue(state.currentXp >= 20)
    }

    @Test
    fun `test focus session increments focus minutes and awards xp`() {
        GamificationManager.recordFocusSession(30)
        val state = GamificationManager.state.value
        assertEquals(30, state.totalFocusMinutes)
        assertTrue(state.currentXp > 0)
    }

    @Test
    fun `test tree stage progression through growth days`() {
        val seed = TreeState(growthDays = 1)
        assertEquals(1, seed.stage)
        assertEquals("🌰", seed.stageEmoji)

        val sprout = TreeState(growthDays = 3)
        assertEquals(2, sprout.stage)
        assertEquals("🌱", sprout.stageEmoji)

        val youngTree = TreeState(growthDays = 5)
        assertEquals(3, youngTree.stage)
        assertEquals("🌿", youngTree.stageEmoji)

        val matureTree = TreeState(growthDays = 8)
        assertEquals(4, matureTree.stage)
        assertEquals("🌳", matureTree.stageEmoji)

        val harvestTree = TreeState(growthDays = 12)
        assertEquals(5, harvestTree.stage)
        assertEquals("🍎", harvestTree.stageEmoji)

        val garden = TreeState(growthDays = 18)
        assertEquals(6, garden.stage)
        assertEquals("🏞️", garden.stageEmoji)
    }

    @Test
    fun `test game match record and personal best update`() {
        val isFirstPb = GamificationManager.recordGameMatch(
            gameId = "g1",
            gameTitle = "Mental Math Sprint",
            score = 150,
            accuracyPercent = 95,
            reactionTimeMs = 240
        )
        assertTrue(isFirstPb)

        val state = GamificationManager.state.value
        assertEquals(1, state.gamesPlayedTotal)
        assertEquals(150, state.personalBests["g1"]?.highScore)
        assertEquals(95, state.personalBests["g1"]?.bestAccuracy)
        assertEquals(1, state.matchHistory.size)

        // Second match with lower score is not a new PB
        val isSecondPb = GamificationManager.recordGameMatch(
            gameId = "g1",
            gameTitle = "Mental Math Sprint",
            score = 110,
            accuracyPercent = 90,
            reactionTimeMs = 280
        )
        assertFalse(isSecondPb)
        assertEquals(150, GamificationManager.state.value.personalBests["g1"]?.highScore)
    }

    @Test
    fun `test water and harvest tree increases fruits and growth`() {
        val initialFruits = GamificationManager.state.value.tree.auraFruits
        val initialGrowth = GamificationManager.state.value.tree.growthDays

        val gained = GamificationManager.waterAndHarvestTree()
        val newState = GamificationManager.state.value

        assertTrue(gained > 0)
        assertEquals(initialFruits + gained, newState.tree.auraFruits)
        assertEquals(initialGrowth + 1, newState.tree.growthDays)
        assertTrue(newState.tree.isWateredToday)
    }

    @Test
    fun `test fruit expenditure for reward unlocking`() {
        GamificationManager.waterAndHarvestTree()
        val fruits = GamificationManager.state.value.tree.auraFruits
        assertTrue(fruits >= 15)

        val success = GamificationManager.spendAuraFruits(10, "neon_theme_unlock")
        assertTrue(success)
        assertEquals(fruits - 10, GamificationManager.state.value.tree.auraFruits)
        assertTrue(GamificationManager.state.value.tree.unlockedRewards.contains("neon_theme_unlock"))

        // Failing to spend more fruits than owned
        val fail = GamificationManager.spendAuraFruits(9999, "impossible_reward")
        assertFalse(fail)
    }

    @Test
    fun `test profile customization updates`() {
        GamificationManager.updateProfile("Samir Ahmmed", "Crafting high-flow cognitive experiences.", "⚡")
        val state = GamificationManager.state.value
        assertEquals("Samir Ahmmed", state.userName)
        assertEquals("Crafting high-flow cognitive experiences.", state.userBio)
        assertEquals("⚡", state.userAvatarEmoji)
    }

    @Test
    fun `test wipe all user data completely resets state`() {
        GamificationManager.addXp(500)
        GamificationManager.recordFocusSession(60)
        GamificationManager.waterAndHarvestTree()

        assertTrue(GamificationManager.state.value.currentXp > 0)
        assertTrue(GamificationManager.state.value.totalFocusMinutes > 0)

        GamificationManager.resetAllUserDataToZero()
        val resetState = GamificationManager.state.value
        assertEquals(0, resetState.currentXp)
        assertEquals(1, resetState.currentLevel)
        assertEquals(0, resetState.totalFocusMinutes)
        assertEquals(0, resetState.tree.auraFruits)
    }

    @Test
    fun `test privacy consent acknowledgement updates state`() {
        assertFalse(GamificationManager.state.value.hasAcknowledgedPrivacy)
        GamificationManager.acknowledgePrivacy()
        assertTrue(GamificationManager.state.value.hasAcknowledgedPrivacy)
    }

    @Test
    fun `test form validation on profile names and bios`() {
        // Empty name should fail
        val emptyResult = com.example.util.FormValidationHelper.validateProfileName("   ")
        assertFalse(emptyResult.isValid)

        // Valid name
        val validResult = com.example.util.FormValidationHelper.validateProfileName("Samir Ahmmed")
        assertTrue(validResult.isValid)
        assertEquals("Samir Ahmmed", validResult.sanitizedValue)

        // Bio length limit
        val longBio = "A".repeat(150)
        val bioResult = com.example.util.FormValidationHelper.validateProfileBio(longBio)
        assertFalse(bioResult.isValid)
        assertEquals(120, bioResult.sanitizedValue.length)
    }

    @Test
    fun `test debounce helper allows first click and throttles rapid bursts`() {
        assertTrue(com.example.util.DebounceHelper.canClick(100L))
        // Immediate next call within 100ms should be throttled
        assertFalse(com.example.util.DebounceHelper.canClick(100L))
    }

    @Test
    fun `test suite 4 games recording and leveling progression`() {
        // Record matches for new cognitive games g43 and g48
        GamificationManager.recordGameMatch(
            gameId = "g43",
            gameTitle = "Quantum Mirror Entanglement",
            score = 120,
            accuracyPercent = 90,
            reactionMs = 320
        )
        val stateAfterG43 = GamificationManager.state.value
        assertEquals(1, stateAfterG43.gamesPlayedTotal)
        assertTrue(stateAfterG43.currentXp > 0)

        // Record Hanoi optimizer match
        GamificationManager.recordGameMatch(
            gameId = "g48",
            gameTitle = "Tower of Hanoi Flow",
            score = 95,
            accuracyPercent = 100,
            reactionMs = 450
        )
        val stateAfterG48 = GamificationManager.state.value
        assertEquals(2, stateAfterG48.gamesPlayedTotal)
    }

    @Test
    fun `test productivity tool xp rewards and streak preservation`() {
        val initialXp = GamificationManager.state.value.currentXp
        GamificationManager.addXp(30, "Parkinson Task Smashed")
        GamificationManager.addXp(25, "Rubber Duck Insight Logged")
        GamificationManager.addXp(30, "Mental Rehearsal Done")

        val newXp = GamificationManager.state.value.currentXp
        assertEquals(initialXp + 85, newXp)
    }

    @Test
    fun `test suite 5 games recording and high score updates`() {
        // Record matches for new cognitive games g53 and g54
        val pb1 = GamificationManager.recordGameMatch(
            gameId = "g53",
            gameTitle = "Quantum Polarization Align",
            score = 150,
            accuracyPercent = 95,
            reactionMs = 280
        )
        assertTrue(pb1)
        val stateAfterG53 = GamificationManager.state.value
        assertEquals(1, stateAfterG53.gamesPlayedTotal)
        assertEquals(150, stateAfterG53.personalBests["g53"]?.highScore)

        // Record Schulte Table match
        val pb2 = GamificationManager.recordGameMatch(
            gameId = "g54",
            gameTitle = "Schulte Table Matrix",
            score = 200,
            accuracyPercent = 100,
            reactionMs = 190
        )
        assertTrue(pb2)
        val stateAfterG54 = GamificationManager.state.value
        assertEquals(2, stateAfterG54.gamesPlayedTotal)
        assertEquals(200, stateAfterG54.personalBests["g54"]?.highScore)
    }

    @Test
    fun `test suite 4 productivity tools xp allocation and gamification`() {
        val initialXp = GamificationManager.state.value.currentXp
        GamificationManager.addXp(20, "Eisenhower Quadrant Completed")
        GamificationManager.addXp(35, "Atomic Habit Microdose Executed")
        GamificationManager.addXp(25, "Chronotype Rhythm Synced")
        GamificationManager.addXp(40, "Attention Firewall Complete")
        GamificationManager.addXp(50, "Play Store Launchpad Rewarded")

        val expectedXp = initialXp + 170
        assertEquals(expectedXp, GamificationManager.state.value.currentXp)
    }
}
