plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.engine"
}

dependencies {
    api(libs.loggable)
    api(libs.kotlinx.coroutines.android)

    api(projects.media.model)
    api(projects.model.result)
}
