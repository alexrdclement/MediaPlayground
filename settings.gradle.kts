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
include(":feature:error")
include(":feature:media-control")
include(":feature:player")
include(":feature:spotify")
include(":testing")
include(":ui")

if (file("../UiPlayground").exists()) {
    includeBuild("../UiPlayground") {
        dependencySubstitution {
            substitute(module("com.alexrdclement.uiplayground:ui-playground-components")).using(project(":components"))
            substitute(module("com.alexrdclement.uiplayground:ui-playground-log")).using(project(":log"))
            substitute(module("com.alexrdclement.uiplayground:ui-playground-loggable")).using(project(":loggable"))
            substitute(module("com.alexrdclement.uiplayground:ui-playground-theme")).using(project(":theme"))
            substitute(module("com.alexrdclement.uiplayground:ui-playground-uievent")).using(project(":uievent"))
        }
    }
}
