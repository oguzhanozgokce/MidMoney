plugins {
    alias(libs.plugins.midmoney.android.feature)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.feature.profile"
}

dependencies {
    implementation(projects.plugin.user)
}
