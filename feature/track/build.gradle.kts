plugins {
    id(libs.plugins.mediaplayground.android.feature.get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
    id(libs.plugins.alexrdclement.kotlin.serialization.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.track"
}

dependencies {
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.uievent)
    implementation(libs.palette.components)
    implementation(libs.palette.navigation)
    implementation(libs.palette.theme)

    implementation(projects.data.artist)
    implementation(projects.data.track)
    implementation(projects.media.model.audio)
    implementation(projects.media.session.api)
}
