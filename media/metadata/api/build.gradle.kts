plugins {
    id(libs.plugins.embarrasdf.android.library.asProvider().get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.metadata.api"
}

dependencies {
    api(libs.kotlinx.io.core)
    api(projects.media.model)
}
