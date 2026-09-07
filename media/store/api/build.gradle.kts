plugins {
    id(libs.plugins.embarrasdf.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.store.api"
}

dependencies {
    api(libs.kotlinx.io.core)
    api(projects.model.result)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.loggable)
}
