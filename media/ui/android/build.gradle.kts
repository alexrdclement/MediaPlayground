plugins {
    alias(libs.plugins.alexrdclement.android.library)
    alias(libs.plugins.alexrdclement.android.library.compose)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.ui"
}

dependencies {
    // TODO KMP: Temp to avoid circular dependency
//    api(projects.media.ui.api)

    api(projects.media.session.api)

    implementation(libs.camera.camera2)
    implementation(libs.camera.compose)
    implementation(libs.camera.core)
    implementation(libs.camera.lifecycle)
    implementation(libs.compose.foundation)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.media3.common)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.ui)

    implementation(projects.ui)
    implementation(projects.media.engine.android)
}
