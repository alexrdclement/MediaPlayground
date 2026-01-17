plugins {
    id(libs.plugins.mediaplayground.android.feature.get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.album"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.logger.api)
    implementation(libs.palette.components)
    implementation(libs.palette.theme)

    implementation(projects.data.audio)
    implementation(projects.media.session.api)
    implementation(projects.model.audio)
    implementation(projects.model.result)
}
