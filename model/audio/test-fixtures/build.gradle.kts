plugins {
    alias(libs.plugins.mediaplayground.jvm.library)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.model.audio)
}
