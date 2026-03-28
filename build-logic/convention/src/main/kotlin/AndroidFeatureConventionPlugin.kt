import com.alexrdclement.mediaplayground.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.alexrdclement.gradle.plugin.android.library")
                apply("dev.zacsweers.metro")
            }

            dependencies {
                add("implementation", libs.findLibrary("metrox-viewmodel-compose").get())
                add("implementation", libs.findLibrary("lifecycle-runtime-compose").get())

                add("implementation", project(":ui"))

                add("testImplementation", kotlin("test"))
            }
        }
    }
}
