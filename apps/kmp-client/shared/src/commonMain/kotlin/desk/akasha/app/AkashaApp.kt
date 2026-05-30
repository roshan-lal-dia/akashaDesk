package desk.akasha.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import desk.akasha.app.crucible.CrucibleDashboard

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
        CrucibleDashboard()
    }
}
