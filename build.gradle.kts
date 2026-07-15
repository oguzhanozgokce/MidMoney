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
}
