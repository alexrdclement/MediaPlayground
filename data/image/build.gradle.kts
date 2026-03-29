plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.image"
}

dependencies {
    api(projects.media.model.audio)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.io.core)

    implementation(projects.data.disk)
    implementation(projects.database)

    testImplementation(libs.maindispatcher.rule)
    testImplementation(projects.data.disk.testFixtures)
    testImplementation(projects.database.testFixtures)
    testImplementation(projects.media.model.audio.testFixtures)
    testImplementation(projects.data.image.testFixtures)
}
