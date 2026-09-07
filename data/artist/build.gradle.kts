plugins {
    id(libs.plugins.embarrasdf.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.artist"
}

dependencies {
    api(projects.media.model)

    implementation(libs.kotlinx.coroutines.android)

    implementation(projects.database)
    testImplementation(libs.kotlin.test.junit)

    testImplementation(libs.maindispatcher.rule)
    testImplementation(projects.database.testFixtures)
    testImplementation(projects.media.model.testFixtures)
}
