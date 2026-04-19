plugins {
    id(libs.plugins.embarrasdf.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.audioasset.test.fixtures"
}

dependencies {
    api(projects.data.audioAsset)
    api(projects.database.testFixtures)
}
