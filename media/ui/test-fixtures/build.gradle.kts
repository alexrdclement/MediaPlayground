plugins {
    // See https://github.com/gradle/gradle/issues/17968
    alias(libs.plugins.alexrdclement.android.library.test.fixtures)
    alias(libs.plugins.alexrdclement.android.hilt.test.fixtures)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.ui.test.fixtures"
}

dependencies {
    // Temp
    api(projects.media.ui.android)
}
