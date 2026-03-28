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

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

dependencies {
    implementation(libs.metro.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidFeature") {
            id = "mediaplayground.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
    }
}
