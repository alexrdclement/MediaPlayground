plugins {
    id(libs.plugins.mediaplayground.android.library.asProvider().get().pluginId)
    id(libs.plugins.mediaplayground.android.hilt.asProvider().get().pluginId)
    id(libs.plugins.mediaplayground.android.room.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.alexrdclement.mediaplayground.database"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.room.paging)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(projects.database.testFixtures)
}
