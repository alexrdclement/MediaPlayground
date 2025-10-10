plugins {
    // See https://github.com/gradle/gradle/issues/17968
    id(libs.plugins.mediaplayground.android.library.asProvider().get().pluginId)
    id(libs.plugins.mediaplayground.android.hilt.asProvider().get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.session"
}

dependencies {
    api(projects.media.session.api)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)

    implementation(projects.media.engine.api)
}
