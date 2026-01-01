pluginManagement {
    includeBuild("build-logic")
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

rootProject.name = "MediaPlayground"

include(":androidApp")
include(":app")
include(":data:audio")
include(":data:audio:test-fixtures")
include(":database")
include(":database:test-fixtures")
include(":media:engine:android")
include(":media:engine:api")
include(":media:engine:test-fixtures")
include(":media:import")
include(":media:import:test-fixtures")
include(":media:session:android:api")
include(":media:session:android:impl")
include(":media:session:api")
include(":media:session:test-fixtures")
include(":media:ui:android")
include(":media:ui:api")
include(":media:ui:test-fixtures")
include(":model:audio")
include(":model:audio:test-fixtures")
include(":model:result")
include(":feature:album")
include(":feature:audio-library")
include(":feature:camera")
include(":feature:error")
include(":feature:media-control")
include(":feature:player")
include(":feature:spotify")
include(":ui")

val localPropsFile = rootDir.resolve("local.properties").takeIf { it.exists() }
val localProps = java.util.Properties().apply {
    localPropsFile?.inputStream()?.use { load(it) }
}

val includeLogging = localProps.getProperty("includeLogging")?.toBoolean() ?: false
if (includeLogging && file("../logging").exists()) {
    includeBuild("../logging") {
        dependencySubstitution {
            substitute(module("com.alexrdclement.log:logger-apl")).using(project(":logger-api"))
            substitute(module("com.alexrdclement.log:logger-impl")).using(project(":logger-impl"))
            substitute(module("com.alexrdclement.log:loggable")).using(project(":loggable"))
        }
    }
}

val includeMaindispatcherrule = localProps.getProperty("includeMaindispatcherrule")?.toBoolean() ?: false
if (includeMaindispatcherrule && file("../maindispatcherrule").exists()) {
    includeBuild("../maindispatcherrule") {
        dependencySubstitution {
            substitute(module("com.alexrdclement.testing:maindispatcherrule-junit4")).using(project(":maindispatcherrule-junit4"))
            substitute(module("com.alexrdclement.testing:maindispatcherrule-junit-jupiter")).using(project(":maindispatcherrule-junit-jupiter"))
        }
    }
}

val includeUievent = localProps.getProperty("includeUievent")?.toBoolean() ?: false
if (includeUievent && file("../uievent").exists()) {
    includeBuild("../uievent") {
        dependencySubstitution {
            substitute(module("com.alexrdclement.uievent:uievent")).using(project(":uievent"))
        }
    }
}

val includeUiplayground = localProps.getProperty("includeUiplayground")?.toBoolean() ?: false
if (file("../UiPlayground").exists()) {
    includeBuild("../UiPlayground") {
        dependencySubstitution {
            substitute(module("com.alexrdclement.uiplayground:ui-playground-components")).using(project(":components"))
            substitute(module("com.alexrdclement.uiplayground:ui-playground-theme")).using(project(":theme"))
        }
    }
}
