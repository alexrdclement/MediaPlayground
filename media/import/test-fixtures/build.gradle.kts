plugins {
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.mediaimport.test.fixtures"
}

dependencies {
    api(projects.media.import.impl)
    implementation(projects.media.metadata.testFixtures)
    implementation(projects.media.store.testFixtures)
}
