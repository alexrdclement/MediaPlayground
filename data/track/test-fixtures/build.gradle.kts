plugins {
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.track.test.fixtures"
}

dependencies {
    api(projects.data.track)
    api(projects.data.disk.testFixtures)
    api(projects.media.import.testFixtures)
    api(projects.database.testFixtures)
    api(libs.kotlinx.coroutines.android)

    implementation(projects.data.album)
    implementation(projects.data.artist)
    implementation(projects.data.clip)
    implementation(projects.data.clip.testFixtures)
    implementation(projects.data.image)
    implementation(projects.data.mediaAsset)
    implementation(projects.data.mediaAsset.testFixtures)
    implementation(projects.media.store.api)
}
