package app.oguzhanozgokce.midmoney.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Enables Jetpack Compose for the module: applies the Compose compiler plugin,
 * turns on the `compose` build feature, and wires the shared Compose dependencies
 * (BOM + core UI + Material 3 + tooling) so UI modules don't repeat them.
 */
class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.findByType(ApplicationExtension::class.java)?.apply {
                buildFeatures.compose = true
            }
            extensions.findByType(LibraryExtension::class.java)?.apply {
                buildFeatures.compose = true
            }

            val metricsEnabled = providers.gradleProperty("composeMetrics")
                .getOrElse("false").toBoolean()

            // Opt-in stability/skippability report:
            // `./gradlew <task> -PcomposeMetrics=true --rerun-tasks` writes per-module reports to
            // build/compose_compiler. Off by default because the extra compiler output slows the
            // build; used to decide whether a @Stable/@Immutable annotation is actually needed
            // instead of guessing.
            if (metricsEnabled) {
                extensions.configure<ComposeCompilerGradlePluginExtension> {
                    val dir = layout.buildDirectory.dir("compose_compiler")
                    reportsDestination.set(dir)
                    metricsDestination.set(dir)
                }
            }

            val bom = libs.findLibrary("androidx-compose-bom").get()
            dependencies {
                add("implementation", platform(bom))
                add("androidTestImplementation", platform(bom))

                add("implementation", libs.findLibrary("androidx-compose-ui").get())
                add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())
                add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
                add("implementation", libs.findLibrary("androidx-compose-material3").get())

                add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
                add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())
                add("androidTestImplementation", libs.findLibrary("androidx-compose-ui-test-junit4").get())
            }
        }
    }
}
