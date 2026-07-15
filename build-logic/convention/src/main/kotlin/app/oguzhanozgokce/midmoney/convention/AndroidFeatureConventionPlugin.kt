package app.oguzhanozgokce.midmoney.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("midmoney.android.library")
                apply("midmoney.android.compose")
                apply("midmoney.android.hilt")
            }

            dependencies {
                add("implementation", project(":library:mvi"))
                add("implementation", project(":library:navigation"))
                add("implementation", project(":library:designsystem"))

                add("implementation", libs.findLibrary("androidx-navigation3-runtime").get())
                add("implementation", libs.findLibrary("androidx-hilt-navigation-compose").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
            }
        }
    }
}
