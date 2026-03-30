plugins {
    id(libs.plugins.alexrdclement.jvm.library.get().pluginId)
    id(libs.plugins.alexrdclement.kotlin.serialization.get().pluginId)
}

dependencies {
    api(projects.media.model.image.testFixtures)
    implementation(projects.media.model.audio)
}
