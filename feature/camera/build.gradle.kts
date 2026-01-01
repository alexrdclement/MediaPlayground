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
    implementation(libs.uiPlayground.components)
    implementation(libs.uiPlayground.theme)

    implementation(projects.media.ui.api)
}
