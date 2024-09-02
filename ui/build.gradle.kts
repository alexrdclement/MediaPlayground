plugins {
    alias(libs.plugins.mediaplayground.android.library)
    alias(libs.plugins.mediaplayground.android.library.compose)
}

android {
    namespace = "com.alexrdclement.mediaplayground.ui"
}

dependencies {
    api(projects.ui.shared)

    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.paging)
    implementation(libs.paging.compose)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.ui.playground.components)

    implementation(projects.model.audio)
}
