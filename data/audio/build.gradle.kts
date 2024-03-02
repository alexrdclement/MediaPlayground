plugins {
    alias(libs.plugins.mediaplayground.android.library)
    alias(libs.plugins.mediaplayground.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.audio"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.appcompat)
    implementation(libs.spotify.api.kotlin.core)
    implementation(libs.paging)

    implementation(projects.media.import)
    implementation(projects.model.audio)
    implementation(projects.model.result)
}
