plugins {
    id(libs.plugins.embarrasdf.jvm.library.get().pluginId)
    id(libs.plugins.embarrasdf.kotlin.serialization.get().pluginId)
}

dependencies {
    api(libs.kotlinx.collections.immutable)

    implementation(libs.kotlinx.serialization.json)
}
