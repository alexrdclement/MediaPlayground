plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.ui"
}

dependencies {
    api(projects.media.session.api)

    implementation(libs.palette.components)

    // TODO KMP: Use expect/actual for Android MediaPlayer
    implementation(projects.media.ui.android)
}
