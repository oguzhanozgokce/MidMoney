plugins {
    alias(libs.plugins.midmoney.android.library)
    alias(libs.plugins.midmoney.android.hilt)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.error"
}

dependencies {
    implementation(project(":library:common"))
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.google.truth)
}
