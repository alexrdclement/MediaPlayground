pluginManagement {
    includeBuild("build-logic")
    if (file("../gradle-plugins").exists()) {
        includeBuild("../gradle-plugins")
    }
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
    versionCatalogs {
        if (file("../gradle-plugins").exists()) {
            create("alexrdclementPluginLibs") {
                from(files("../gradle-plugins/gradle/libs.versions.toml"))
            }
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "MediaPlayground"

include(":androidApp")
include(":app")
include(":data:album")
include(":data:album:test-fixtures")
include(":data:artist")
include(":data:artist:test-fixtures")
include(":data:disk")
include(":data:disk:test-fixtures")
include(":data:image")
include(":data:image:test-fixtures")
include(":data:media")
include(":data:track")
include(":data:track:test-fixtures")
include(":database")
include(":database:test-fixtures")
include(":media:engine:android")
include(":media:engine:api")
include(":media:engine:test-fixtures")
include(":media:import:impl")
include(":media:import:api")
include(":media:import:test-fixtures")
include(":media:metadata:api")
include(":media:metadata:impl")
include(":media:metadata:test-fixtures")
include(":media:model")
include(":media:model:test-fixtures")
include(":media:session:android:api")
include(":media:session:android:impl")
include(":media:session:api")
include(":media:session:test-fixtures")
include(":media:store:api")
include(":media:store:impl")
include(":media:store:test-fixtures")
include(":media:ui:android")
include(":media:ui:api")
include(":media:ui:test-fixtures")
include(":model:result")
include(":feature:album")
include(":feature:artist")
include(":feature:audio-library")
include(":feature:camera")
include(":feature:error")
include(":feature:image")
include(":feature:image-library")
include(":feature:media-control")
include(":feature:playback-control")
include(":feature:player")
include(":feature:track")
include(":ui")

val localPropsFile = rootDir.resolve("local.properties").takeIf { it.exists() }
val localProps = java.util.Properties().apply {
    localPropsFile?.inputStream()?.use { load(it) }
}

val includeLogging = localProps.getProperty("includeLogging")?.toBoolean() ?: false
if (includeLogging && file("../logging").exists()) {
    includeBuild("../logging") {
        dependencySubstitution {
            substitute(module("com.alexrdclement.logging:logger-api")).using(project(":logger-api"))
            substitute(module("com.alexrdclement.logging:logger-impl")).using(project(":logger-impl"))
            substitute(module("com.alexrdclement.logging:loggable")).using(project(":loggable"))
        }
    }
}

val includeTesting = localProps.getProperty("includeTesting")?.toBoolean() ?: false
if (includeTesting && file("../testing").exists()) {
    includeBuild("../testing") {
        dependencySubstitution {
            substitute(module("com.alexrdclement.testing:maindispatcher-rule")).using(project(":maindispatcher-rule"))
            substitute(module("com.alexrdclement.testing:maindispatcher-extension")).using(project(":maindispatcher-extension"))
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

val includePalette = localProps.getProperty("includePalette")?.toBoolean() ?: false
if (includePalette && file("../palette").exists()) {
    includeBuild("../palette") {
        dependencySubstitution {
            substitute(module("com.alexrdclement.palette:palette-components")).using(project(":components"))
            substitute(module("com.alexrdclement.palette:palette-modifiers")).using(project(":modifiers"))
            substitute(module("com.alexrdclement.palette:palette-navigation")).using(project(":navigation"))
            substitute(module("com.alexrdclement.palette:palette-theme")).using(project(":theme"))
        }
    }
}
