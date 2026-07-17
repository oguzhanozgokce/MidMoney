plugins {
    alias(libs.plugins.midmoney.android.library)
    alias(libs.plugins.midmoney.android.hilt)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.datastore"
}

dependencies {
    api(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.core)
}
