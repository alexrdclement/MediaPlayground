plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.metadata"
}

dependencies {
    api(libs.kotlinx.io.core)
    api(projects.media.model)
}
