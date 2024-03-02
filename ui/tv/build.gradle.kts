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
    implementation(libs.ui.tooling.preview)
    implementation(libs.tv.foundation)
    implementation(libs.tv.material)

    implementation(projects.model.audio)
}
