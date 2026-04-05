plugins {
    id(libs.plugins.mediaplayground.android.feature.get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
    id(libs.plugins.alexrdclement.kotlin.serialization.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.album"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.uievent)
    implementation(libs.logger.api)
    implementation(libs.palette.components)
    implementation(libs.palette.navigation)
    implementation(libs.palette.theme)

    implementation(projects.data.album)
    implementation(projects.data.artist)
    implementation(projects.data.image)
    implementation(projects.data.track)
    implementation(projects.media.model)
    implementation(projects.media.session.api)
    implementation(projects.model.result)
}
