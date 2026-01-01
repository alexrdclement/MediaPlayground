plugins {
    // See https://github.com/gradle/gradle/issues/17968
    alias(libs.plugins.alexrdclement.android.library.test.fixtures)
    alias(libs.plugins.alexrdclement.android.hilt.test.fixtures)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.engine.test.fixtures"
}

dependencies {
    api(projects.media.engine.android)
}
