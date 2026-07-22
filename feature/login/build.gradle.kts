plugins {
    alias(libs.plugins.midmoney.android.feature)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.feature.login"
}

dependencies {
    implementation(projects.plugin.user)
}
