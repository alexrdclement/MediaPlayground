plugins {
    // See https://github.com/gradle/gradle/issues/17968
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
    id(libs.plugins.alexrdclement.android.hilt.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.audio.test.fixtures"
}

dependencies {
    api(projects.data.audio)
    api(projects.media.import.testFixtures)
    
    implementation(projects.database.testFixtures)
}
