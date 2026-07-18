plugins {
    alias(libs.plugins.midmoney.android.library)
    alias(libs.plugins.midmoney.android.hilt)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.event"
}

dependencies {
    implementation(project(":library:logger"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
