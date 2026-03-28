plugins {
    id(libs.plugins.alexrdclement.jvm.library.get().pluginId)
    id(libs.plugins.alexrdclement.kotlin.serialization.get().pluginId)
}

dependencies {
    api(libs.kotlinx.collections.immutable)

    implementation(libs.kotlinx.serialization.json)
}
