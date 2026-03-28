plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    id(libs.plugins.alexrdclement.android.hilt.asProvider().get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.engine"
}

dependencies {
    api(libs.loggable)

    api(projects.media.model.audio)
    api(projects.model.result)
}
