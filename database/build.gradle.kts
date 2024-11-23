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
}
