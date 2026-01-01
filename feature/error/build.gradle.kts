plugins {
    alias(libs.plugins.mediaplayground.android.feature)
    alias(libs.plugins.mediaplayground.android.library.compose)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.error"
}

dependencies {
    implementation(libs.palette.components)
    implementation(libs.palette.theme)
}
