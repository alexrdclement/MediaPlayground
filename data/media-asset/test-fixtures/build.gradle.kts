plugins {
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.mediaasset.test.fixtures"
}

dependencies {
    api(projects.data.mediaAsset)
    api(projects.data.disk.testFixtures)
    api(projects.database.testFixtures)
}
