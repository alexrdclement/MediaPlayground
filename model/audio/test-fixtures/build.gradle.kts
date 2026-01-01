plugins {
    id(libs.plugins.alexrdclement.jvm.library.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.model.audio)
}
