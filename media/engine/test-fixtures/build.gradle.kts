plugins {
    id(libs.plugins.embarrasdf.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.engine.test.fixtures"
}

dependencies {
    api(projects.media.engine.android)
}
