

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.embarrasdf.github.release)
}

githubRelease {
    githubToken = System.getenv("GITHUB_TOKEN")
    repository = "alexrdclement/MediaPlayground"
    enabled = !version.toString().endsWith("SNAPSHOT")
    newTagRevision = System.getenv("GITHUB_SHA")
}
