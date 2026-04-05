plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.store"
}

dependencies {
    api(libs.kotlinx.io.core)
    api(projects.model.result)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.loggable)
}
