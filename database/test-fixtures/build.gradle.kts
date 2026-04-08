plugins {
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.database.test.fixtures"
}

dependencies {
    api(projects.database)
    api(projects.media.store.api)
    api(projects.media.store.testFixtures)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.io.core)
    implementation(libs.paging)
    implementation(libs.paging.testing)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinx.coroutines.test)
}
