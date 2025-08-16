plugins {
    alias(libs.plugins.mediaplayground.android.feature)
    alias(libs.plugins.mediaplayground.android.library.compose)
}

android {
    namespace = "com.alexrdclement.mediaplayground.feature.audio.library"
}

dependencies {
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.material3)
    implementation(libs.media3.datasource)
    implementation(libs.media3.common)
    implementation(libs.paging)
    implementation(libs.paging.compose)
    implementation(libs.uiPlayground.components)
    implementation(libs.uiPlayground.theme)

    implementation(projects.data.audio)
    implementation(projects.media.session)
    implementation(projects.model.audio)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)

    testImplementation(projects.data.audio.testFixtures)
    testImplementation(projects.media.session.testFixtures)
    testImplementation(projects.testing)
}
