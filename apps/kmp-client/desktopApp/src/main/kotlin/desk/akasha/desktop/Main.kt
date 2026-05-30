package desk.akasha.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import desk.akasha.app.AkashaApp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AkashaDesk",
    ) {
        AkashaApp()
    }
}

