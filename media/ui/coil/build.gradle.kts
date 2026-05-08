plugins {
    id(libs.plugins.embarrasdf.android.library.asProvider().get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.ui.coil"
}

dependencies {
    api(libs.coil.core)
    api(projects.media.model)
    api(projects.media.store.api)
}
