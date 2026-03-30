plugins {
    id(libs.plugins.alexrdclement.jvm.library.get().pluginId)
    id(libs.plugins.alexrdclement.kotlin.serialization.get().pluginId)
}

dependencies {
    api(libs.kotlinx.collections.immutable)
    api(projects.media.model.image)

    implementation(libs.kotlinx.serialization.json)
}
