plugins {
    alias(libs.plugins.midmoney.android.library)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.mvi"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
