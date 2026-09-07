plugins {
    id(libs.plugins.embarrasdf.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.metadata.impl"
}

dependencies {
    api(projects.media.metadata.api)

    implementation(libs.androidx.exifinterface)
}
