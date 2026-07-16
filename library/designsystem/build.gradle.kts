plugins {
    alias(libs.plugins.midmoney.android.library)
    alias(libs.plugins.midmoney.android.compose)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.designsystem"
}

dependencies {
    api(libs.androidx.compose.material.icons.extended)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
