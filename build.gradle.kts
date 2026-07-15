// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Plugins are declared here with `apply false` so their versions are pinned on the build classpath;
// the convention plugins in `build-logic` then apply them by id to the modules that need them.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.google.services) apply false
}

// Static analysis + formatting applied to every module.
subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "dev.detekt")

    configure<dev.detekt.gradle.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
    }
}

// One-time setup: points git at the committed .githooks directory so the pre-commit hook runs.
tasks.register<Exec>("installGitHooks") {
    group = "git hooks"
    description = "Configures git to use the .githooks directory for this repository."
    commandLine("git", "config", "core.hooksPath", ".githooks")
}
