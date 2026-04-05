plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.mediaimport"
}

dependencies {
    api(libs.kotlinx.io.core)
    api(libs.kotlinx.coroutines.android)
    api(libs.loggable)

    api(projects.media.model)
    api(projects.model.result)
}
