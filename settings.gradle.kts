pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "akashadesk"

include(":apps:kmp-client:core-database")
include(":apps:kmp-client:core-network")
include(":apps:kmp-client:desktopApp")
include(":apps:kmp-client:feature-crucible")
include(":apps:kmp-client:feature-megaphone")
include(":apps:kmp-client:feature-obsidian")
include(":apps:kmp-client:shared")
include(":services:core-engine")

