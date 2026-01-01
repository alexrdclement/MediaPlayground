plugins {
    // See https://github.com/gradle/gradle/issues/17968
    alias(libs.plugins.alexrdclement.android.library)
    alias(libs.plugins.alexrdclement.android.hilt)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.session"
}

dependencies {
    api(projects.media.engine.api)
    api(projects.model.audio)

    implementation(libs.kotlinx.coroutines.android)
}
