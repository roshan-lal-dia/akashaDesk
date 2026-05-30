plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm("desktop")

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

