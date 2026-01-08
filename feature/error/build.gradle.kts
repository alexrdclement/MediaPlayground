plugins {
    id(libs.plugins.mediaplayground.android.feature.get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.error"
}

dependencies {
    implementation(libs.palette.components)
    implementation(libs.palette.theme)
}
