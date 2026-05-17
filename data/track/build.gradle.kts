plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.track"
}

dependencies {
    api(projects.media.model)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.io.core)
    implementation(libs.loggable)
    implementation(libs.paging)

    implementation(projects.data.disk)
    implementation(projects.database)
    implementation(projects.database.mapping)
    implementation(projects.media.import.api)
    implementation(projects.media.store.api)
    implementation(projects.model.result)

    testImplementation(libs.maindispatcher.rule)
    testImplementation(projects.data.album)
    testImplementation(projects.data.track.testFixtures)
    testImplementation(projects.media.model.testFixtures)
}
