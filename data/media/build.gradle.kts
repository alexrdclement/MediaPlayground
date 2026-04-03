plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.media"
}

dependencies {
    implementation(projects.data.album)
    implementation(projects.data.track)
    implementation(projects.media.engine.api)
}
