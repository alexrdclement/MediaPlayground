plugins {
    alias(libs.plugins.mediaplayground.android.feature)
    alias(libs.plugins.mediaplayground.android.library.compose)
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
