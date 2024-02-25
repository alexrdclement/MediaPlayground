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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// https://youtrack.jetbrains.com/issue/KTIJ-24981/Gradle-8.-project-sync-fails-with-an-error-No-matching-toolchains-found-for-requested-specification-if-there-is-no-necessary-JDK
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.4.0")
}

rootProject.name = "MediaPlayground"
include(":androidApp")
include(":androidTvApp")
include(":data:audio")
include(":media:import")
include(":media:session")
include(":model:audio")
include(":model:result")
include(":feature:album")
include(":feature:audio-library")
include(":feature:player")
include(":feature:spotify")
include(":ui")
include(":ui:shared")
include(":ui:tv")

if (file("../UiPlayground").exists()) {
    includeBuild("../UiPlayground") {
        dependencySubstitution {
            substitute(module("com.alexrdclement.uiplayground:components")).using(project(":components"))
        }
    }
}
