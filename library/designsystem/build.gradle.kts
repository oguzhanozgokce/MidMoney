plugins {
    alias(libs.plugins.midmoney.android.library)
    alias(libs.plugins.midmoney.android.compose)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.designsystem"
}

dependencies {
    // Exposed (api) so feature modules can pass icons to components like MidMoneyEmptyState.
    // Release builds tree-shake unused icons; in a size-critical app prefer core icons or vectors.
    api(libs.androidx.compose.material.icons.extended)
}
