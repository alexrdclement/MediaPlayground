plugins {
    alias(libs.plugins.mediaplayground.android.feature)
    alias(libs.plugins.mediaplayground.android.library.compose)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.spotify"
}

dependencies {
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.material3)
    implementation(libs.media3.datasource)
    implementation(libs.media3.common)
    implementation(libs.paging)
    implementation(libs.paging.compose)
    implementation(libs.uiPlayground.components)
    implementation(libs.uiPlayground.theme)

    implementation(projects.data.audio)
    implementation(projects.media.session.api)
    implementation(projects.model.audio)
}
