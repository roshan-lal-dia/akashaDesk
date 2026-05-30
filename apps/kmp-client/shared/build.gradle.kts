plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(project(":apps:kmp-client:core-network"))
            implementation(project(":apps:kmp-client:feature-crucible"))
            implementation(project(":apps:kmp-client:feature-megaphone"))
            implementation(project(":apps:kmp-client:feature-obsidian"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

