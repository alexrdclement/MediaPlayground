plugins {
    alias(libs.plugins.mediaplayground.android.application)
    alias(libs.plugins.mediaplayground.android.application.compose)
    alias(libs.plugins.mediaplayground.android.hilt)
}

android {
    namespace = "com.alexrdclement.mediaplayground"

    defaultConfig {
        applicationId = "com.alexrdclement.mediaplayground"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
        
        manifestPlaceholders["redirectSchemeName"] = "comalexrdclementmediaplayground"
        manifestPlaceholders["redirectHostName"] = "callback"
    }

    signingConfigs {
        create("release") {
            storeFile = file("${System.getenv("KEYSTORE_FILE_PATH")}")
            storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("ANDROID_KEY_ALIAS")
            keyPassword = System.getenv("ANDROID_KEY_PASSWORD")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            signingConfig = signingConfigs.getByName("release")

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
    implementation(libs.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.material3)
    implementation(libs.hilt.android)
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.media3.ui.leanback)
    implementation(libs.media3.session)
    implementation(libs.media3.cast)
    implementation(libs.media3.transformer)
    implementation(libs.media3.decoder)
    implementation(libs.media3.datasource)
    implementation(libs.media3.common)
    implementation(libs.uiPlayground.components)
    implementation(libs.uiPlayground.theme)

    implementation(projects.data.audio)
    implementation(projects.feature.album)
    implementation(projects.feature.audioLibrary)
    implementation(projects.feature.mediaControl)
    implementation(projects.feature.player)
    implementation(projects.feature.spotify)
    implementation(projects.media.session)
    implementation(projects.model.audio)
    implementation(projects.ui)
}
