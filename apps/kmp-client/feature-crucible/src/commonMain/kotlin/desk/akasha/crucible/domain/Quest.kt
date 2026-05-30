package desk.akasha.crucible.domain

@JvmInline
value class QuestId(val value: String)

@JvmInline
value class CompanionId(val value: String)

@JvmInline
value class EpochMillis(val value: Long)

sealed interface Quest {
    val id: QuestId
    val title: String
    val createdAt: EpochMillis
    val kind: QuestKind
}

data class EnduranceQuest(
    override val id: QuestId,
    override val title: String,
    override val createdAt: EpochMillis,
    val damageOnFailure: Int,
) : Quest {
    override val kind: QuestKind = QuestKind.ENDURANCE
}

data class GrindQuest(
    override val id: QuestId,
    override val title: String,
    override val createdAt: EpochMillis,
    val dailyXpReward: Int,
) : Quest {
    override val kind: QuestKind = QuestKind.GRIND
}

data class SprintQuest(
    override val id: QuestId,
    override val title: String,
    override val createdAt: EpochMillis,
    val startsAt: EpochMillis,
    val endsAt: EpochMillis,
    val completionXpBounty: Int,
) : Quest {
    override val kind: QuestKind = QuestKind.SPRINT
}

enum class QuestKind {
    ENDURANCE,
    GRIND,
    SPRINT,
}

enum class QuestStatus {
    ACTIVE,
    COMPLETED,
    FAILED,
    PAUSED,
}

data class QuestProgress(
    val currentHp: Int,
    val currentXp: Int,
    val streakDays: Int,
)

data class ArenaInvite(
    val inviteCode: String,
    val questId: QuestId,
    val inviterId: CompanionId,
    val expiresAt: EpochMillis,
)
