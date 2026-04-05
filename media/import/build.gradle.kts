plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.mediaimport"
}

dependencies {
    api(libs.kotlinx.io.core)

    api(projects.media.model)
    api(projects.model.result)

    implementation(libs.androidx.documentfile)
    implementation(libs.androidx.exifinterface)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.loggable)
    implementation(libs.media3.cast)
    implementation(libs.media3.common)
    implementation(libs.media3.datasource)
    implementation(libs.media3.decoder)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)
    implementation(libs.media3.transformer)
    implementation(libs.media3.ui)
}
