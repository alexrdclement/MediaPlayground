plugins {
    id(libs.plugins.embarrasdf.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.album.test.fixtures"
}

dependencies {
    api(projects.data.album)

    implementation(projects.data.track.testFixtures)
}
