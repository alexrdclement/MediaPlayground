plugins {
    alias(libs.plugins.mediaplayground.android.library)
    alias(libs.plugins.mediaplayground.android.library.compose)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.ui"
}

dependencies {
    // TODO KMP: Temp to avoid circular dependency
//    api(projects.media.ui.api)

    api(projects.media.session.api)

    implementation(libs.ui)
    implementation(libs.compose.foundation)
    implementation(libs.media3.common)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)

    implementation(projects.ui)
    implementation(projects.media.engine.android)
}
