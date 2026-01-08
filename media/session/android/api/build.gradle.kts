plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    id(libs.plugins.alexrdclement.android.hilt.asProvider().get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.session"
}
