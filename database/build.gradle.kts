plugins {
    id(libs.plugins.embarrasdf.android.library.asProvider().get().pluginId)
    alias(libs.plugins.metro)
    id(libs.plugins.embarrasdf.android.room.get().pluginId)
    id(libs.plugins.embarrasdf.kotlin.serialization.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.database"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.room.paging)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(projects.database.testFixtures)
}
