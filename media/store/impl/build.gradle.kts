plugins {
    id(libs.plugins.embarrasdf.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.store.impl"
}

dependencies {
    api(projects.media.store.api)

    implementation(libs.androidx.documentfile)
    implementation(libs.kotlinx.coroutines.android)
}
