plugins {
    alias(libs.plugins.mediaplayground.android.feature)
    alias(libs.plugins.mediaplayground.android.library.compose)
    alias(libs.plugins.mediaplayground.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.alexrdclement.mediaplayground.app"
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.hilt.android)
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.uiPlayground.components)
    implementation(libs.uiPlayground.log)
    implementation(libs.uiPlayground.theme)
    implementation(libs.uiPlayground.uievent)

    implementation(projects.data.audio)
    implementation(projects.feature.album)
    implementation(projects.feature.audioLibrary)
    implementation(projects.feature.camera)
    implementation(projects.feature.error)
    implementation(projects.feature.mediaControl)
    implementation(projects.feature.player)
    implementation(projects.feature.spotify)
    implementation(projects.media.ui.android)
    implementation(projects.media.engine.android)
    implementation(projects.media.session.android.impl)
    implementation(projects.model.audio)
    implementation(projects.ui)
}
