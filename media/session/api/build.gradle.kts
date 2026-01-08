plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    id(libs.plugins.alexrdclement.android.hilt.asProvider().get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.session"
}

dependencies {
    api(projects.media.engine.api)
    api(projects.model.audio)

    implementation(libs.kotlinx.coroutines.android)
}
