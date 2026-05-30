package desk.akasha.app.crucible

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import desk.akasha.crucible.domain.CrucibleEngine
import desk.akasha.crucible.domain.CrucibleEvent
import desk.akasha.crucible.domain.CrucibleProfile
import desk.akasha.crucible.domain.CrucibleState
import desk.akasha.crucible.domain.EpochMillis
import desk.akasha.crucible.domain.QuestKind
import desk.akasha.crucible.domain.QuestStatus
import desk.akasha.crucible.domain.TrackedQuest
import desk.akasha.crucible.domain.label
import desk.akasha.crucible.domain.seedCrucibleState

private val TerminalGreen = Color(0xFF00FF66)
private val TerminalPink = Color(0xFFFF66FF)
private val TerminalCyan = Color(0xFF00CCFF)
private val Surface = Color(0xFF0A0014)
private val SurfaceHigh = Color(0xFF1D0C26)
private val Border = Color(0xFF402E49)
private val TextPrimary = Color(0xFFF4DAFE)
private val TextMuted = Color(0xFFB9CCB5)

@Composable
fun CrucibleDashboard() {
    val engine = remember { CrucibleEngine() }
    var state by remember { mutableStateOf(seedCrucibleState()) }
    var newQuestKind by remember { mutableStateOf(QuestKind.GRIND) }
    var newQuestTitle by remember { mutableStateOf("") }
    var nextCreatedAt by remember { mutableStateOf(1_000L) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        CrucibleHeader(state.profile, state.activeQuestCount)
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            QuestRail(
                state = state,
                onSelect = { questId -> state = engine.selectQuest(state, questId) },
                modifier = Modifier.width(280.dp).fillMaxHeight(),
            )
            QuestDetail(
                state = state,
                onComplete = { questId -> state = engine.completeQuest(state, questId) },
                onFail = { questId -> state = engine.failQuest(state, questId) },
                modifier = Modifier.weight(1f).fillMaxHeight(),
            )
            CommandPanel(
                state = state,
                newQuestKind = newQuestKind,
                newQuestTitle = newQuestTitle,
                onKindSelected = { newQuestKind = it },
                onTitleChanged = { newQuestTitle = it },
                onCreateQuest = {
                    state = engine.addQuest(
                        state = state,
                        kind = newQuestKind,
                        title = newQuestTitle,
                        createdAt = EpochMillis(nextCreatedAt),
                    )
                    nextCreatedAt += 1_000L
                    newQuestTitle = ""
                },
                modifier = Modifier.width(340.dp).fillMaxHeight(),
            )
        }
    }
}

@Composable
private fun CrucibleHeader(profile: CrucibleProfile, activeQuestCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .border(1.dp, TerminalGreen)
            .background(Surface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = "AKASHADESK / CRUCIBLE",
                color = TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.sp,
            )
            Text(
                text = "HP ${profile.hp} / XP ${profile.xp} / LVL ${profile.level} / STREAK ${profile.streakDays}",
                color = TerminalCyan,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
            SignalCell("ACTIVE", activeQuestCount.toString(), TerminalGreen)
            SignalCell("PILOT", profile.displayName, TerminalPink)
        }
    }
}

@Composable
private fun QuestRail(
    state: CrucibleState,
    onSelect: (desk.akasha.crucible.domain.QuestId) -> Unit,
    modifier: Modifier = Modifier,
) {
    Panel(title = "QUEST INDEX", modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            items(state.quests) { trackedQuest ->
                QuestRow(
                    trackedQuest = trackedQuest,
                    selected = trackedQuest.quest.id == state.selectedQuestId,
                    onClick = { onSelect(trackedQuest.quest.id) },
                )
            }
        }
    }
}

@Composable
private fun QuestDetail(
    state: CrucibleState,
    onComplete: (desk.akasha.crucible.domain.QuestId) -> Unit,
    onFail: (desk.akasha.crucible.domain.QuestId) -> Unit,
    modifier: Modifier = Modifier,
) {
    Panel(title = "CRUCIBLE ARENA", modifier = modifier) {
        val trackedQuest = state.selectedQuest
        if (trackedQuest == null) {
            EmptySignal("NO QUEST SELECTED")
            return@Panel
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = trackedQuest.quest.title.uppercase(),
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.sp,
                    )
                    Text(
                        text = "${trackedQuest.quest.kind.label()} / ${trackedQuest.status.label()} / PARTY ${trackedQuest.participantCount}",
                        color = colorForKind(trackedQuest.quest.kind),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp,
                    )
                }
                StatusBlock(trackedQuest.status)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                MetricBlock("QUEST HP", trackedQuest.progress.currentHp.toString(), TerminalGreen, Modifier.weight(1f))
                MetricBlock("QUEST XP", trackedQuest.progress.currentXp.toString(), TerminalCyan, Modifier.weight(1f))
                MetricBlock("STREAK", trackedQuest.progress.streakDays.toString(), TerminalPink, Modifier.weight(1f))
            }

            Meter(label = "GLOBAL HP", value = state.profile.hp, max = 100, accent = TerminalGreen)
            Meter(
                label = "LEVEL XP",
                value = state.profile.xpIntoLevel,
                max = CrucibleProfile.XP_PER_LEVEL,
                accent = TerminalCyan,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                TerminalButton(
                    label = "COMPLETE",
                    accent = TerminalGreen,
                    enabled = trackedQuest.status == QuestStatus.ACTIVE,
                    modifier = Modifier.weight(1f),
                    onClick = { onComplete(trackedQuest.quest.id) },
                )
                TerminalButton(
                    label = "FAIL",
                    accent = TerminalPink,
                    enabled = trackedQuest.status == QuestStatus.ACTIVE,
                    modifier = Modifier.weight(1f),
                    onClick = { onFail(trackedQuest.quest.id) },
                )
            }

            EventLog(events = state.eventLog, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun CommandPanel(
    state: CrucibleState,
    newQuestKind: QuestKind,
    newQuestTitle: String,
    onKindSelected: (QuestKind) -> Unit,
    onTitleChanged: (String) -> Unit,
    onCreateQuest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Panel(title = "COMMAND", modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "NEW QUEST",
                color = TerminalCyan,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp,
            )
            KindSelector(
                selected = newQuestKind,
                onSelected = onKindSelected,
            )
            TerminalInput(
                value = newQuestTitle,
                onValueChange = onTitleChanged,
                placeholder = "TITLE",
            )
            TerminalButton(
                label = "CREATE",
                accent = TerminalGreen,
                enabled = true,
                onClick = onCreateQuest,
            )

            Spacer(modifier = Modifier.height(4.dp))

            MetricBlock("TOTAL QUESTS", state.quests.size.toString(), TerminalCyan)
            MetricBlock("ACTIVE QUESTS", state.activeQuestCount.toString(), TerminalGreen)
            MetricBlock("LEVEL", state.profile.level.toString(), TerminalPink)
        }
    }
}

@Composable
private fun Panel(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .border(1.dp, Border)
            .background(Surface)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            color = TerminalCyan,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp,
        )
        content()
    }
}

@Composable
private fun QuestRow(
    trackedQuest: TrackedQuest,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val accent = colorForKind(trackedQuest.quest.kind)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .border(1.dp, if (selected) accent else Border)
            .background(if (selected) SurfaceHigh else Color.Black)
            .clickable(onClick = onClick)
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(6.dp)
                .height(42.dp)
                .background(accent),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = trackedQuest.quest.title.uppercase(),
                color = TextPrimary,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp,
            )
            Text(
                text = "${trackedQuest.quest.kind.label()} / ${trackedQuest.status.label()}",
                color = TextMuted,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.sp,
            )
        }
    }
}

@Composable
private fun EventLog(events: List<CrucibleEvent>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Border)
            .background(Color.Black)
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        events.forEach { event ->
            Text(
                text = "${event.label} :: ${event.detail}",
                color = TextMuted,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.sp,
            )
        }
    }
}

@Composable
private fun Meter(label: String, value: Int, max: Int, accent: Color) {
    val fraction = (value.toFloat() / max.toFloat()).coerceIn(0f, 1f)
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                color = TextMuted,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.sp,
            )
            Text(
                text = "$value/$max",
                color = accent,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .border(1.dp, Border)
                .background(Color.Black),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .fillMaxHeight()
                    .background(accent),
            )
        }
    }
}

@Composable
private fun KindSelector(selected: QuestKind, onSelected: (QuestKind) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
        QuestKind.entries.forEach { kind ->
            TerminalButton(
                label = kind.label(),
                accent = colorForKind(kind),
                enabled = true,
                active = kind == selected,
                modifier = Modifier.weight(1f),
                onClick = { onSelected(kind) },
            )
        }
    }
}

@Composable
private fun TerminalInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(
            color = TextPrimary,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.sp,
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .border(1.dp, Border)
                    .background(Color.Black)
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (value.isBlank()) {
                    Text(
                        text = placeholder,
                        color = TextMuted,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.sp,
                    )
                }
                innerTextField()
            }
        },
    )
}

@Composable
private fun TerminalButton(
    label: String,
    accent: Color,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    onClick: () -> Unit,
) {
    val borderColor = if (enabled) accent else Border
    val background = when {
        !enabled -> SurfaceHigh
        active -> accent
        else -> Color.Black
    }
    val content = when {
        !enabled -> TextMuted
        active -> Color.Black
        else -> accent
    }
    Box(
        modifier = modifier
            .height(44.dp)
            .border(1.dp, borderColor)
            .background(background)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = content,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp,
        )
    }
}

@Composable
private fun SignalCell(label: String, value: String, accent: Color) {
    Column(
        modifier = Modifier
            .width(112.dp)
            .height(48.dp)
            .border(1.dp, accent)
            .background(Color.Black)
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            color = TextMuted,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.sp,
        )
        Text(
            text = value,
            color = accent,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp,
        )
    }
}

@Composable
private fun MetricBlock(label: String, value: String, accent: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(68.dp)
            .border(1.dp, Border)
            .background(Color.Black)
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            color = TextMuted,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.sp,
        )
        Text(
            text = value,
            color = accent,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp,
        )
    }
}

@Composable
private fun StatusBlock(status: QuestStatus) {
    val accent = when (status) {
        QuestStatus.ACTIVE -> TerminalGreen
        QuestStatus.COMPLETED -> TerminalCyan
        QuestStatus.FAILED -> TerminalPink
        QuestStatus.PAUSED -> TextMuted
    }
    Box(
        modifier = Modifier
            .width(112.dp)
            .height(36.dp)
            .border(1.dp, accent)
            .background(accent),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = status.label(),
            color = Color.Black,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp,
        )
    }
}

@Composable
private fun EmptySignal(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, Border)
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = TextMuted,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp,
        )
    }
}

private fun colorForKind(kind: QuestKind): Color {
    return when (kind) {
        QuestKind.ENDURANCE -> TerminalPink
        QuestKind.GRIND -> TerminalGreen
        QuestKind.SPRINT -> TerminalCyan
    }
}
