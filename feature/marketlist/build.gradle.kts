plugins {
    alias(libs.plugins.midmoney.android.feature)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.feature.marketlist"
}

dependencies {
    implementation(project(":plugin:market"))
    implementation(project(":library:error"))
}
