import com.alexrdclement.mediaplayground.convention.libs
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("mediaplayground.android.library")
                apply("mediaplayground.android.hilt")
            }

            dependencies {
                add("implementation", libs.findLibrary("hilt-navigation-compose").get())
                add("implementation", libs.findLibrary("lifecycle-runtime-compose").get())

                add("implementation", project(":ui"))

                add("testImplementation", kotlin("test"))
            }
        }
    }
}
