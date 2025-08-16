plugins {
    alias(libs.plugins.mediaplayground.android.library)
}

android {
    namespace = "com.alexrdclement.mediaplayground.testing"
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(libs.junit)
}
