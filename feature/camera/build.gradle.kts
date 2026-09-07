plugins {
    id(libs.plugins.mediaplayground.android.feature.get().pluginId)
    id(libs.plugins.embarrasdf.android.library.compose.get().pluginId)
    id(libs.plugins.embarrasdf.kotlin.serialization.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.camera"
}

dependencies {
    implementation(libs.calf.permissions)
    implementation(libs.palette.components)
    implementation(libs.palette.navigation)
    implementation(libs.palette.theme)

    implementation(projects.media.ui.api)
}
