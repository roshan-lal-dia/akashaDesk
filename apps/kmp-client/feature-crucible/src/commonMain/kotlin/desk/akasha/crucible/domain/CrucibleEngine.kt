package desk.akasha.crucible.domain

data class CrucibleProfile(
    val displayName: String,
    val hp: Int,
    val xp: Int,
    val streakDays: Int,
) {
    val level: Int
        get() = (xp / XP_PER_LEVEL) + 1

    val xpIntoLevel: Int
        get() = xp % XP_PER_LEVEL

    companion object {
        const val XP_PER_LEVEL = 100
    }
}

data class TrackedQuest(
    val quest: Quest,
    val status: QuestStatus,
    val progress: QuestProgress,
    val completions: Int = 0,
    val failures: Int = 0,
    val participantCount: Int = 1,
)

data class CrucibleEvent(
    val label: String,
    val detail: String,
)

data class CrucibleState(
    val profile: CrucibleProfile,
    val quests: List<TrackedQuest>,
    val selectedQuestId: QuestId? = quests.firstOrNull()?.quest?.id,
    val eventLog: List<CrucibleEvent> = emptyList(),
) {
    val selectedQuest: TrackedQuest?
        get() = quests.firstOrNull { it.quest.id == selectedQuestId }

    val activeQuestCount: Int
        get() = quests.count { it.status == QuestStatus.ACTIVE }
}

class CrucibleEngine {
    fun completeQuest(state: CrucibleState, questId: QuestId): CrucibleState {
        val trackedQuest = state.quests.firstOrNull { it.quest.id == questId } ?: return state
        if (trackedQuest.status != QuestStatus.ACTIVE) {
            return state.pushEvent("IGNORED", "${trackedQuest.quest.title} is not active.")
        }

        val reward = xpRewardFor(trackedQuest.quest)
        val updatedQuest = trackedQuest.copy(
            status = statusAfterCompletion(trackedQuest.quest),
            progress = trackedQuest.progress.copy(
                currentXp = trackedQuest.progress.currentXp + reward,
                streakDays = trackedQuest.progress.streakDays + 1,
            ),
            completions = trackedQuest.completions + 1,
        )

        return state.copy(
            profile = state.profile.copy(
                xp = state.profile.xp + reward,
                streakDays = state.profile.streakDays + 1,
            ),
            quests = state.replaceQuest(updatedQuest),
        ).pushEvent("COMPLETE", "+$reward XP / ${trackedQuest.quest.title}")
    }

    fun failQuest(state: CrucibleState, questId: QuestId): CrucibleState {
        val trackedQuest = state.quests.firstOrNull { it.quest.id == questId } ?: return state
        if (trackedQuest.status != QuestStatus.ACTIVE) {
            return state.pushEvent("IGNORED", "${trackedQuest.quest.title} is not active.")
        }

        val damage = hpDamageFor(trackedQuest.quest)
        val updatedQuest = trackedQuest.copy(
            status = statusAfterFailure(trackedQuest.quest),
            progress = trackedQuest.progress.copy(
                currentHp = (trackedQuest.progress.currentHp - damage).coerceAtLeast(0),
                streakDays = 0,
            ),
            failures = trackedQuest.failures + 1,
        )

        return state.copy(
            profile = state.profile.copy(
                hp = (state.profile.hp - damage).coerceAtLeast(0),
                streakDays = 0,
            ),
            quests = state.replaceQuest(updatedQuest),
        ).pushEvent("FAIL", "-$damage HP / ${trackedQuest.quest.title}")
    }

    fun addQuest(
        state: CrucibleState,
        kind: QuestKind,
        title: String,
        createdAt: EpochMillis,
    ): CrucibleState {
        val normalizedTitle = title.trim().ifBlank { defaultTitleFor(kind) }
        val quest = when (kind) {
            QuestKind.ENDURANCE -> EnduranceQuest(
                id = nextQuestId(state),
                title = normalizedTitle,
                createdAt = createdAt,
                damageOnFailure = 35,
            )
            QuestKind.GRIND -> GrindQuest(
                id = nextQuestId(state),
                title = normalizedTitle,
                createdAt = createdAt,
                dailyXpReward = 15,
            )
            QuestKind.SPRINT -> SprintQuest(
                id = nextQuestId(state),
                title = normalizedTitle,
                createdAt = createdAt,
                startsAt = createdAt,
                endsAt = EpochMillis(createdAt.value + SPRINT_DURATION_MS),
                completionXpBounty = 75,
            )
        }
        val trackedQuest = TrackedQuest(
            quest = quest,
            status = QuestStatus.ACTIVE,
            progress = QuestProgress(currentHp = 100, currentXp = 0, streakDays = 0),
        )
        return state.copy(
            quests = listOf(trackedQuest) + state.quests,
            selectedQuestId = quest.id,
        ).pushEvent("CREATED", "${quest.kind.label()} / ${quest.title}")
    }

    fun selectQuest(state: CrucibleState, questId: QuestId): CrucibleState {
        return if (state.quests.any { it.quest.id == questId }) {
            state.copy(selectedQuestId = questId)
        } else {
            state
        }
    }

    private fun CrucibleState.replaceQuest(updatedQuest: TrackedQuest): List<TrackedQuest> {
        return quests.map { current ->
            if (current.quest.id == updatedQuest.quest.id) updatedQuest else current
        }
    }

    private fun CrucibleState.pushEvent(label: String, detail: String): CrucibleState {
        return copy(eventLog = (listOf(CrucibleEvent(label, detail)) + eventLog).take(MAX_EVENT_LOG))
    }

    private fun xpRewardFor(quest: Quest): Int {
        return when (quest) {
            is EnduranceQuest -> 0
            is GrindQuest -> quest.dailyXpReward
            is SprintQuest -> quest.completionXpBounty
        }
    }

    private fun hpDamageFor(quest: Quest): Int {
        return when (quest) {
            is EnduranceQuest -> quest.damageOnFailure
            is GrindQuest -> 8
            is SprintQuest -> 25
        }
    }

    private fun statusAfterCompletion(quest: Quest): QuestStatus {
        return when (quest) {
            is SprintQuest -> QuestStatus.COMPLETED
            is EnduranceQuest,
            is GrindQuest -> QuestStatus.ACTIVE
        }
    }

    private fun statusAfterFailure(quest: Quest): QuestStatus {
        return when (quest) {
            is EnduranceQuest,
            is SprintQuest -> QuestStatus.FAILED
            is GrindQuest -> QuestStatus.ACTIVE
        }
    }

    private fun nextQuestId(state: CrucibleState): QuestId {
        return QuestId("quest-${state.quests.size + 1}")
    }

    private fun defaultTitleFor(kind: QuestKind): String {
        return when (kind) {
            QuestKind.ENDURANCE -> "No Sugar"
            QuestKind.GRIND -> "Exercise 30 mins"
            QuestKind.SPRINT -> "7-Day Code Challenge"
        }
    }

    private companion object {
        const val MAX_EVENT_LOG = 8
        const val SPRINT_DURATION_MS = 7L * 24L * 60L * 60L * 1000L
    }
}

fun QuestKind.label(): String {
    return when (this) {
        QuestKind.ENDURANCE -> "ENDURANCE"
        QuestKind.GRIND -> "GRIND"
        QuestKind.SPRINT -> "SPRINT"
    }
}

fun QuestStatus.label(): String {
    return when (this) {
        QuestStatus.ACTIVE -> "ACTIVE"
        QuestStatus.COMPLETED -> "DONE"
        QuestStatus.FAILED -> "FAILED"
        QuestStatus.PAUSED -> "PAUSED"
    }
}

fun seedCrucibleState(): CrucibleState {
    val createdAt = EpochMillis(0)
    return CrucibleState(
        profile = CrucibleProfile(
            displayName = "DIA",
            hp = 100,
            xp = 40,
            streakDays = 3,
        ),
        quests = listOf(
            TrackedQuest(
                quest = EnduranceQuest(
                    id = QuestId("quest-1"),
                    title = "No Sugar",
                    createdAt = createdAt,
                    damageOnFailure = 35,
                ),
                status = QuestStatus.ACTIVE,
                progress = QuestProgress(currentHp = 100, currentXp = 0, streakDays = 3),
                participantCount = 1,
            ),
            TrackedQuest(
                quest = GrindQuest(
                    id = QuestId("quest-2"),
                    title = "Strength Training",
                    createdAt = createdAt,
                    dailyXpReward = 15,
                ),
                status = QuestStatus.ACTIVE,
                progress = QuestProgress(currentHp = 100, currentXp = 30, streakDays = 2),
                participantCount = 1,
            ),
            TrackedQuest(
                quest = SprintQuest(
                    id = QuestId("quest-3"),
                    title = "7-Day Code Challenge",
                    createdAt = createdAt,
                    startsAt = createdAt,
                    endsAt = EpochMillis(SPRINT_SEED_END_MS),
                    completionXpBounty = 75,
                ),
                status = QuestStatus.ACTIVE,
                progress = QuestProgress(currentHp = 100, currentXp = 0, streakDays = 0),
                participantCount = 2,
            ),
        ),
        eventLog = listOf(CrucibleEvent("SYNC", "Local Crucible state ready.")),
    )
}

private const val SPRINT_SEED_END_MS = 7L * 24L * 60L * 60L * 1000L
