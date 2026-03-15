plugins {
    id(libs.plugins.mediaplayground.android.feature.get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
    id(libs.plugins.alexrdclement.kotlin.serialization.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.error"
}

dependencies {
    implementation(libs.palette.components)
    implementation(libs.palette.navigation)
    implementation(libs.palette.theme)
}
