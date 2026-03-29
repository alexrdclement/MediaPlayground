plugins {
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.image.test.fixtures"
}

dependencies {
    api(projects.data.image)
    api(projects.data.disk.testFixtures)
    api(projects.database.testFixtures)
}
