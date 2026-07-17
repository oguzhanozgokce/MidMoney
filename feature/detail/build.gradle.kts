plugins {
    alias(libs.plugins.midmoney.android.feature)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.feature.detail"
}

dependencies {
    implementation(project(":plugin:market"))
    implementation(project(":library:error"))
    implementation(project(":plugin:news"))
}
