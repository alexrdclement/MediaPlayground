plugins {
    alias(libs.plugins.mediaplayground.android.feature)
    alias(libs.plugins.mediaplayground.android.library.compose)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.album"
}

dependencies {
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.uiPlayground.components)
    implementation(libs.uiPlayground.theme)
    implementation(libs.uiPlayground.log)

    implementation(projects.data.audio)
    implementation(projects.media.session.api)
    implementation(projects.model.audio)
    implementation(projects.model.result)
}
