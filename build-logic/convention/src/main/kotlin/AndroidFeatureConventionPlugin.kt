import com.alexrdclement.mediaplayground.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

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
            }
        }
    }
}
