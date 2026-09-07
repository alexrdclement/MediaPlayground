plugins {
    id(libs.plugins.mediaplayground.android.feature.get().pluginId)
    id(libs.plugins.embarrasdf.android.library.compose.get().pluginId)
    id(libs.plugins.embarrasdf.kotlin.serialization.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.artist"
}

dependencies {
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.uievent)
    implementation(libs.palette.components)
    implementation(libs.palette.navigation)
    implementation(libs.palette.theme)

    implementation(projects.data.artist)
    implementation(projects.media.model)
    implementation(projects.media.session.api)
}
