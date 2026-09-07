plugins {
    id(libs.plugins.embarrasdf.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.disk"
}

dependencies {
    implementation(libs.kotlinx.io.core)
}
