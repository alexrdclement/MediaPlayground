plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
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
