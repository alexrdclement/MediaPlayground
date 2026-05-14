plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.disk"
}

dependencies {
    api(projects.media.store.api)

    implementation(libs.kotlinx.io.core)
    implementation(libs.androidx.documentfile)
}
