plugins {
    // See https://github.com/gradle/gradle/issues/17968
    id(libs.plugins.mediaplayground.android.library.asProvider().get().pluginId)
    id(libs.plugins.mediaplayground.android.hilt.asProvider().get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.engine"
}

dependencies {
    api(projects.media.engine.api)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)

    implementation(projects.data.audio)
    implementation(projects.media.session.android)
}
