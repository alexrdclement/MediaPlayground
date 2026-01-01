plugins {
    alias(libs.plugins.mediaplayground.android.feature)
    alias(libs.plugins.mediaplayground.android.library.compose)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.player"
}

dependencies {
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.ui)
    implementation(libs.palette.components)
    implementation(libs.palette.theme)

    implementation(projects.data.audio)
    implementation(projects.model.audio)
    implementation(projects.media.session.api)
    implementation(projects.media.ui.api)
}
