plugins {
    id(libs.plugins.alexrdclement.android.library.asProvider().get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.session"
}

dependencies {
    api(projects.media.engine.api)
    api(projects.media.model)

    implementation(libs.kotlinx.coroutines.android)
}
