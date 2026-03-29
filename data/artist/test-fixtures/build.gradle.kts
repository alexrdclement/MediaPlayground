plugins {
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.artist.test.fixtures"
}

dependencies {
    api(projects.data.artist)
    api(projects.database.testFixtures)
}
