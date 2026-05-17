plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.clip"
}

dependencies {
    api(projects.media.model)
    api(libs.kotlinx.coroutines.android)

    implementation(libs.kotlinx.io.core)
    implementation(libs.paging)

    implementation(projects.data.disk)
    implementation(projects.database)
    implementation(projects.database.mapping)
    implementation(projects.media.store.api)

    testImplementation(libs.maindispatcher.rule)
    testImplementation(projects.data.disk.testFixtures)
    testImplementation(projects.data.mediaAsset.testFixtures)
    testImplementation(projects.database.testFixtures)
    testImplementation(projects.media.model.testFixtures)
    testImplementation(projects.data.clip.testFixtures)
}
