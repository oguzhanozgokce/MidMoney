plugins {
    alias(libs.plugins.midmoney.android.library)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
