plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.mediaimport"
}

dependencies {
    api(projects.media.import.api)

    implementation(libs.androidx.exifinterface)
    implementation(libs.kotlinx.coroutines.android)

    implementation(projects.media.metadata.api)
    implementation(projects.media.store.api)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(projects.media.import.testFixtures)
}
