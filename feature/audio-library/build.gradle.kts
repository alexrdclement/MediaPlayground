plugins {
    id(libs.plugins.mediaplayground.android.feature.get().pluginId)
    id(libs.plugins.alexrdclement.android.library.compose.get().pluginId)
    id(libs.plugins.alexrdclement.kotlin.serialization.get().pluginId)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.audio.library"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.logger.api)
    implementation(libs.paging)
    implementation(libs.paging.compose)
    implementation(libs.palette.components)
    implementation(libs.palette.navigation)
    implementation(libs.palette.theme)

    implementation(projects.data.album)
    implementation(projects.data.track)
    implementation(projects.media.model)
    implementation(projects.media.session.api)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.maindispatcher.rule)
    testImplementation(libs.turbine)

    testImplementation(projects.data.track.testFixtures)
    testImplementation(projects.media.model.testFixtures)
    testImplementation(projects.media.session.testFixtures)
}
