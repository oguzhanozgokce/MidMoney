plugins {
    alias(libs.plugins.midmoney.android.library)
    alias(libs.plugins.midmoney.android.hilt)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
