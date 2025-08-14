plugins {
    alias(libs.plugins.mediaplayground.android.library)
    alias(libs.plugins.mediaplayground.android.library.compose)
}

android {
    namespace = "com.alexrdclement.mediaplayground.ui.tv"
}

dependencies {
    api(projects.ui.shared)

    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.tv.material)
    implementation(libs.uiPlayground.theme)

    implementation(projects.model.audio)
}
