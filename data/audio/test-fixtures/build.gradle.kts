plugins {
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.audio.test.fixtures"
}

dependencies {
    api(projects.data.audio)
    api(projects.media.import.testFixtures)
    api(libs.kotlinx.coroutines.android)

    implementation(projects.database.testFixtures)
}
