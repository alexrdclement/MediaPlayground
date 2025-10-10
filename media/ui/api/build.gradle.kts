plugins {
    alias(libs.plugins.mediaplayground.android.library)
    alias(libs.plugins.mediaplayground.android.library.compose)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.ui"
}

dependencies {
    api(libs.ui)
    api(projects.media.session.api)

    // TODO KMP: Use expect/actual for Android MediaPlayer
    implementation(projects.media.ui.android)
}
