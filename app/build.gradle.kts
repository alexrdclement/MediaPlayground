plugins {
    id(libs.plugins.mediaplayground.android.feature.get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
    id(libs.plugins.alexrdclement.android.hilt.asProvider().get().pluginId)
    id(libs.plugins.alexrdclement.kotlin.serialization.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.app"
}

dependencies {
    implementation(libs.hilt.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.logger.impl)
    implementation(libs.navigation.compose)
    implementation(libs.palette.components)
    implementation(libs.palette.theme)
    implementation(libs.trace)
    implementation(libs.uievent)

    implementation(projects.data.audio)
    implementation(projects.feature.album)
    implementation(projects.feature.audioLibrary)
    implementation(projects.feature.camera)
    implementation(projects.feature.error)
    implementation(projects.feature.mediaControl)
    implementation(projects.feature.player)
    implementation(projects.feature.spotify)
    implementation(projects.media.engine.android)
    implementation(projects.media.session.android.impl)
    implementation(projects.media.ui.android)
    implementation(projects.model.audio)
    implementation(projects.ui)
}
