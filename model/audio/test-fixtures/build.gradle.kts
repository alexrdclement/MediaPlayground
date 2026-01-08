plugins {
    id(libs.plugins.alexrdclement.jvm.library.get().pluginId)
    id(libs.plugins.alexrdclement.kotlin.serialization.get().pluginId)
}

dependencies {
    implementation(projects.model.audio)
}
