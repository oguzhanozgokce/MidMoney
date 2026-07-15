plugins {
    alias(libs.plugins.midmoney.android.library)
    alias(libs.plugins.midmoney.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.navigation"
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.coroutines.core)
}
