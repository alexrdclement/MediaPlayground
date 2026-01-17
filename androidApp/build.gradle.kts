plugins {
    id(libs.plugins.alexrdclement.android.application.asProvider().get().pluginId)
    id(libs.plugins.alexrdclement.android.application.compose.get().pluginId)
    id(libs.plugins.alexrdclement.android.hilt.asProvider().get().pluginId)
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
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.lifecycle.runtime.compose)

    implementation(projects.app)
}
