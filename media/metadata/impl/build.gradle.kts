plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.metadata"
}

dependencies {
    api(projects.media.metadata.api)

    implementation(libs.androidx.exifinterface)
}
