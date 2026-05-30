plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(project(":apps:kmp-client:shared"))
    implementation(compose.desktop.currentOs)
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
