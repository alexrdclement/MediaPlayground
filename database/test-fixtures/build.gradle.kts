plugins {
    // See https://github.com/gradle/gradle/issues/17968
    id(libs.plugins.mediaplayground.android.library.test.fixtures.get().pluginId)
    id(libs.plugins.mediaplayground.android.hilt.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.database.test.fixtures"
}

dependencies {
    api(projects.database)
    implementation(libs.paging)
    implementation(libs.paging.testing)
}
