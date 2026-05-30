package desk.akasha.crucible.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CrucibleEngineTest {
    private val engine = CrucibleEngine()

    @Test
    fun grindCompletionAwardsDailyXpAndKeepsQuestActive() {
        val result = engine.completeQuest(seedCrucibleState(), QuestId("quest-2"))

        assertEquals(55, result.profile.xp)
        assertEquals(4, result.profile.streakDays)
        assertEquals(QuestStatus.ACTIVE, result.selectedQuestStatus("quest-2"))
    }

    @Test
    fun enduranceFailureDamagesHpAndFailsQuest() {
        val result = engine.failQuest(seedCrucibleState(), QuestId("quest-1"))

        assertEquals(65, result.profile.hp)
        assertEquals(0, result.profile.streakDays)
        assertEquals(QuestStatus.FAILED, result.selectedQuestStatus("quest-1"))
    }

    @Test
    fun sprintCompletionAwardsBountyAndCompletesQuest() {
        val result = engine.completeQuest(seedCrucibleState(), QuestId("quest-3"))

        assertEquals(115, result.profile.xp)
        assertEquals(QuestStatus.COMPLETED, result.selectedQuestStatus("quest-3"))
    }

    @Test
    fun addingQuestSelectsNewQuest() {
        val result = engine.addQuest(
            state = seedCrucibleState(),
            kind = QuestKind.GRIND,
            title = "Walk 8k steps",
            createdAt = EpochMillis(123),
        )

        assertEquals("Walk 8k steps", result.selectedQuest?.quest?.title)
        assertEquals(4, result.quests.size)
    }

    private fun CrucibleState.selectedQuestStatus(id: String): QuestStatus? {
        return quests.firstOrNull { it.quest.id == QuestId(id) }?.status
    }
}
