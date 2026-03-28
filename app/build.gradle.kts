plugins {
    id(libs.plugins.mediaplayground.android.feature.get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
    alias(libs.plugins.metro)
    id(libs.plugins.alexrdclement.kotlin.serialization.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.app"
}

dependencies {
    // Expose Media3 session
    api(projects.media.session.android.impl)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.logger.impl)
    implementation(libs.palette.components)
    implementation(libs.palette.navigation)
    implementation(libs.palette.theme)
    implementation(libs.trace)
    implementation(libs.uievent)

    implementation(projects.database)
    implementation(projects.data.audio)
    implementation(projects.media.import)
    implementation(projects.media.model.audio)
    implementation(projects.feature.album)
    implementation(projects.feature.audioLibrary)
    implementation(projects.feature.camera)
    implementation(projects.feature.error)
    implementation(projects.feature.mediaControl)
    implementation(projects.feature.player)
    implementation(projects.media.engine.android)
    implementation(projects.media.ui.android)
    implementation(projects.ui)
}
