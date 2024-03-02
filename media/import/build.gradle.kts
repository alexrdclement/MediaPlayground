plugins {
    alias(libs.plugins.mediaplayground.android.library)
    alias(libs.plugins.mediaplayground.android.hilt)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.mediaimport"
}

dependencies {
    implementation(libs.androidx.documentfile)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.media3.ui.leanback)
    implementation(libs.media3.session)
    implementation(libs.media3.cast)
    implementation(libs.media3.transformer)
    implementation(libs.media3.decoder)
    implementation(libs.media3.datasource)
    implementation(libs.media3.common)
    implementation(libs.okio)

    implementation(projects.model.audio)
    implementation(projects.model.result)
}
