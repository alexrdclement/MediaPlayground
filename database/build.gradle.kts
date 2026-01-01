plugins {
    alias(libs.plugins.alexrdclement.android.library)
    alias(libs.plugins.alexrdclement.android.hilt)
    alias(libs.plugins.alexrdclement.android.room)
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
