plugins {
    id(libs.plugins.embarrasdf.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.metadata.test.fixtures"
}

dependencies {
    api(projects.media.metadata.impl)
}
