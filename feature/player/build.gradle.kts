plugins {
    id(libs.plugins.mediaplayground.android.feature.get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.player"
}

dependencies {
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.palette.components)
    implementation(libs.palette.theme)

    implementation(projects.data.audio)
    implementation(projects.model.audio)
    implementation(projects.media.session.api)
    implementation(projects.media.ui.api)
}
