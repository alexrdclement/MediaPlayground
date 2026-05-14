plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.album"
}

dependencies {
    api(projects.media.model)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.io.core)
    implementation(libs.paging)

    implementation(projects.data.disk)
    implementation(projects.database)
    implementation(projects.database.mapping)
    implementation(projects.media.store.api)

    testImplementation(libs.maindispatcher.rule)
    testImplementation(projects.data.album.testFixtures)
    testImplementation(projects.data.track)
    testImplementation(projects.media.model.testFixtures)
}
