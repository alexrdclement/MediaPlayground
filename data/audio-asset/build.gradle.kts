plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.audioasset"
}

dependencies {
    api(projects.media.model)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.paging)

    implementation(projects.database)
    implementation(projects.database.mapping)
    implementation(projects.media.store.api)

    testImplementation(libs.maindispatcher.rule)
    testImplementation(projects.database.testFixtures)
    testImplementation(projects.media.model.testFixtures)
    testImplementation(projects.data.audioAsset.testFixtures)
}
