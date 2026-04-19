plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.mediacollection"
}

dependencies {
    api(projects.media.model)

    implementation(libs.kotlinx.coroutines.android)

    implementation(projects.data.album)
}
