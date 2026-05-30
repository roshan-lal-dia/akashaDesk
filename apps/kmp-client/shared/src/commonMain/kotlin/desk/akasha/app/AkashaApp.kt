package desk.akasha.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AkashaApp() {
    val terminalGreen = Color(0xFF00FF66)
    val terminalPink = Color(0xFFFF66FF)
    val terminalCyan = Color(0xFF00CCFF)
    val panel = Color(0xFF0A0014)

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Color.Black,
            surface = panel,
            primary = terminalGreen,
            secondary = terminalPink,
            tertiary = terminalCyan,
            onBackground = Color(0xFFF4DAFE),
            onSurface = Color(0xFFF4DAFE),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            CommandHeader()
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                StatusPanel(
                    title = "CRUCIBLE",
                    signal = "HP 100 / XP 000",
                    accent = terminalGreen,
                    modifier = Modifier.weight(1f),
                )
                StatusPanel(
                    title = "OBSIDIAN",
                    signal = "VAULT LOCAL",
                    accent = terminalCyan,
                    modifier = Modifier.weight(1f),
                )
                StatusPanel(
                    title = "MEGAPHONE",
                    signal = "DRAFT READY",
                    accent = terminalPink,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun CommandHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .border(1.dp, Color(0xFF00FF66))
            .background(Color(0xFF0A0014))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Column {
            Text(
                text = "AKASHADESK",
                color = Color(0xFFF4DAFE),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.sp,
            )
            Text(
                text = "SOVEREIGN TERMINAL / LOCAL-FIRST CONTROL",
                color = Color(0xFF00CCFF),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp,
            )
        }
    }
}

@Composable
private fun StatusPanel(
    title: String,
    signal: String,
    accent: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .border(1.dp, Color(0xFF402E49))
            .background(Color(0xFF0A0014))
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            color = accent,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(24.dp)
                    .background(accent),
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = signal,
                color = Color(0xFFF4DAFE),
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp,
            )
        }
    }
}

