plugins {
    alias(libs.plugins.midmoney.android.library)
    alias(libs.plugins.midmoney.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.plugin.news"
}

dependencies {
    implementation(projects.library.network)
    implementation(projects.library.common)
    implementation(projects.library.error)

    implementation(libs.retrofit.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.google.truth)
}
