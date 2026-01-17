plugins {
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
    id(libs.plugins.alexrdclement.android.hilt.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.database.test.fixtures"
}

dependencies {
    api(projects.database)

    implementation(libs.kotlinx.datetime)
    implementation(libs.paging)
    implementation(libs.paging.testing)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinx.coroutines.test)
}
