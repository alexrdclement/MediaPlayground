plugins {
    alias(libs.plugins.mediaplayground.android.feature)
    alias(libs.plugins.alexrdclement.android.library.compose)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.album"
}

dependencies {
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.logger.api)
    implementation(libs.palette.components)
    implementation(libs.palette.theme)

    implementation(projects.data.audio)
    implementation(projects.media.session.api)
    implementation(projects.model.audio)
    implementation(projects.model.result)
}
