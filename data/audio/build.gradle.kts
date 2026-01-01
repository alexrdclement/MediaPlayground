plugins {
    // See https://github.com/gradle/gradle/issues/17968
    alias(libs.plugins.alexrdclement.android.library)
    alias(libs.plugins.alexrdclement.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.alexrdclement.mediaplayground.data.audio"
}

dependencies {
    api(projects.model.audio)
    api(projects.model.result)

    api(libs.kotlinx.io.core)

    implementation(libs.androidx.activity)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.appcompat)
    implementation(libs.spotify.api.kotlin.core)
    implementation(libs.paging)
    implementation(libs.loggable)

    implementation(projects.database)
    implementation(projects.media.import)

    testImplementation(libs.maindispatcherrule.junit4)

    testImplementation(projects.data.audio.testFixtures)
    testImplementation(projects.database.testFixtures)
    testImplementation(projects.model.audio.testFixtures)
}
