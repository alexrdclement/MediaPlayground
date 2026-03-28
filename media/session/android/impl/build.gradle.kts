plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.session"
}

dependencies {
    api(libs.media3.session)
    api(projects.media.session.api)
    api(projects.media.session.android.api)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.media3.exoplayer)

    implementation(projects.media.engine.android)
}
