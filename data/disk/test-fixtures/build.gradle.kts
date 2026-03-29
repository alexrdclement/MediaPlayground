plugins {
    id(libs.plugins.alexrdclement.android.library.test.fixtures.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.disk.test.fixtures"
}

dependencies {
    api(projects.data.disk)

    implementation(libs.kotlinx.io.core)
}
