plugins {
    id(libs.plugins.mediaplayground.android.feature.get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.camera"
}

dependencies {
    implementation(libs.calf.permissions)
    implementation(libs.ui)
    implementation(libs.palette.components)
    implementation(libs.palette.theme)

    implementation(projects.media.ui.api)
}
