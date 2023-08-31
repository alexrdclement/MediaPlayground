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

// https://youtrack.jetbrains.com/issue/KTIJ-24981/Gradle-8.-project-sync-fails-with-an-error-No-matching-toolchains-found-for-requested-specification-if-there-is-no-necessary-JDK
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.4.0")
}

rootProject.name = "MediaPlayground"
include(":app")
include(":data:audio")
include(":mediaplaygroundtv")
include(":ui")
include(":ui-tv")
include(":ui-shared")
