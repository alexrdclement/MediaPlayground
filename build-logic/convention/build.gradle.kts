import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.alexrdclement.mediaplayground.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.room.gradle.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "mediaplayground.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "mediaplayground.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "mediaplayground.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "mediaplayground.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryTestFixtures") {
            id = "mediaplayground.android.library.test.fixtures"
            implementationClass = "AndroidLibraryTestFixturesConventionPlugin"
        }
        register("androidFeature") {
            id = "mediaplayground.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidTest") {
            id = "mediaplayground.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidHilt") {
            id = "mediaplayground.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidHiltTestFixtures") {
            id = "mediaplayground.android.hilt.test.fixtures"
            implementationClass = "AndroidHiltTestFixturesConventionPlugin"
        }
        register("androidRoom") {
            id = "mediaplayground.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("jvmLibrary") {
            id = "mediaplayground.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
