plugins {
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.media.store.test.fixtures"
}

dependencies {
    api(projects.media.store.impl)
}
