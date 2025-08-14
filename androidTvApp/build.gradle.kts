plugins {
    alias(libs.plugins.mediaplayground.android.application)
    alias(libs.plugins.mediaplayground.android.application.compose)
    alias(libs.plugins.mediaplayground.android.hilt)
}

android {
    namespace = "com.alexrdclement.mediaplayground.tv"

    defaultConfig {
        applicationId = "com.alexrdclement.mediaplayground.tv"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["redirectSchemeName"] = "comalexrdclementmediaplaygroundtv"
        manifestPlaceholders["redirectHostName"] = "callback"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            // Not planning to release the app. Doing this for simplicity.
            signingConfig = signingConfigs.getByName("debug")

            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.tv.material)
    implementation(libs.lifecycle.runtime.ktx)

    implementation(projects.data.audio)
    implementation(projects.model.audio)
    implementation(projects.ui.tv)
}
