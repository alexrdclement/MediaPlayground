plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.database.mapping"
}

dependencies {
    api(projects.database)
    api(projects.media.model)
    api(libs.kotlinx.collections.immutable)
    api(libs.kotlinx.io.core)

    implementation(projects.data.disk)
}
