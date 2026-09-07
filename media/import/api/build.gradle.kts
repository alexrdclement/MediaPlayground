plugins {
    id(libs.plugins.embarrasdf.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.mediaimport.api"
}

dependencies {
    api(libs.kotlinx.io.core)
    api(libs.kotlinx.coroutines.android)
    api(libs.loggable)

    api(projects.media.metadata.api)
    api(projects.media.model)
    api(projects.model.result)
}
