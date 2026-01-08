plugins {
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
    id(libs.plugins.alexrdclement.android.hilt.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.ui.test.fixtures"
}

dependencies {
    // Temp
    api(projects.media.ui.android)
}
