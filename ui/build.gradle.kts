plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.ui"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.paging)
    implementation(libs.paging.compose)
    implementation(libs.palette.components)
    implementation(libs.palette.theme)

    implementation(projects.media.engine.api)
    implementation(projects.media.model.audio)
    implementation(projects.media.model.audio.testFixtures) // Preview data
    implementation(projects.media.ui.api)
}
