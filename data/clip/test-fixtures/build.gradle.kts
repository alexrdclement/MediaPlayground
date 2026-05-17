plugins {
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.clip.test.fixtures"
}

dependencies {
    api(projects.data.audioAsset.testFixtures)
    api(projects.data.clip)
    api(projects.data.disk.testFixtures)
    api(projects.database.testFixtures)
}
