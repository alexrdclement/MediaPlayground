plugins {
    id(libs.plugins.mediaplayground.android.feature.get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.media.control"
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.logger.api)
    implementation(libs.palette.components)
    implementation(libs.palette.theme)

    implementation(projects.media.model.audio)
    implementation(projects.media.session.api)
}
