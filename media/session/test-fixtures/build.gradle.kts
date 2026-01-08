plugins {
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
    id(libs.plugins.alexrdclement.android.hilt.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.session.test.fixtures"
}

dependencies {
    api(projects.media.session.android.impl)

    implementation(projects.media.engine.testFixtures)
}
