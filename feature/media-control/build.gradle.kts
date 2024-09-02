plugins {
    alias(libs.plugins.mediaplayground.android.feature)
    alias(libs.plugins.mediaplayground.android.library.compose)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.media.control"
}

dependencies {
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.material3)
    implementation(libs.navigation.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.playground.components)

    implementation(projects.media.session)
    implementation(projects.model.audio)
}
