plugins {
    // See https://github.com/gradle/gradle/issues/17968
    alias(libs.plugins.alexrdclement.android.library)
    alias(libs.plugins.alexrdclement.android.hilt)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.engine"
}

dependencies {
    api(projects.model.audio)
    api(projects.model.result)
    api(libs.loggable)
}
