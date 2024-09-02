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
    implementation(libs.material3)
    implementation(libs.media3.datasource)
    implementation(libs.media3.common)
    implementation(libs.ui.playground.components)

    implementation(projects.data.audio)
    implementation(projects.media.session)
    implementation(projects.model.audio)
    implementation(projects.model.result)
}
