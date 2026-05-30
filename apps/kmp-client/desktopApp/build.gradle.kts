plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    application
}

dependencies {
    implementation(project(":apps:kmp-client:shared"))
    implementation(compose.desktop.currentOs)
}

application {
    mainClass.set("desk.akasha.desktop.MainKt")
}

compose.desktop {
    application {
        mainClass = "desk.akasha.desktop.MainKt"
        nativeDistributions {
            packageName = "AkashaDesk"
            packageVersion = "0.1.0"
        }
    }
}

