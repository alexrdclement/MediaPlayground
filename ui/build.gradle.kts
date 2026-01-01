plugins {
    alias(libs.plugins.mediaplayground.android.library)
    alias(libs.plugins.mediaplayground.android.library.compose)
}

android {
    namespace = "com.alexrdclement.mediaplayground.ui"
}

dependencies {
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.paging)
    implementation(libs.paging.compose)
    implementation(libs.coil.compose)
    implementation(libs.palette.components)
    implementation(libs.palette.theme)

    implementation(projects.model.audio)
    implementation(projects.model.audio.testFixtures) // Preview data
}
