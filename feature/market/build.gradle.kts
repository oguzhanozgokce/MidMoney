plugins {
    alias(libs.plugins.midmoney.android.feature)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.feature.market"
}

dependencies {
    implementation(projects.plugin.market)
    implementation(projects.library.error)
    implementation(projects.library.remoteconfig)
}
