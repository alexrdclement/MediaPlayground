import org.shipkit.changelog.GenerateChangelogTask
import org.shipkit.github.release.GithubReleaseTask

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false

    alias(libs.plugins.shipkit.autoversion) apply true
    alias(libs.plugins.shipkit.changelog) apply true
    alias(libs.plugins.shipkit.githubrelease) apply true
}

tasks.named<GenerateChangelogTask>("generateChangelog") {
    previousRevision = project.extra["shipkit-auto-version.previous-tag"] as String
    githubToken = System.getenv("GITHUB_TOKEN")
    repository = "alexrdclement/MediaPlayground"
}

tasks.named<GithubReleaseTask>("githubRelease") {
    dependsOn(tasks.named("generateChangelog"))
    val isSnapshot = version.toString().endsWith("SNAPSHOT")
    enabled = !isSnapshot
    repository = "alexrdclement/MediaPlayground"
    changelog = tasks.named("generateChangelog").get().outputs.files.singleFile
    githubToken = System.getenv("GITHUB_TOKEN")
    newTagRevision = System.getenv("GITHUB_SHA")
}
