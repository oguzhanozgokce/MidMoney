plugins {
    alias(libs.plugins.midmoney.android.feature)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.feature.marketlist"
}

dependencies {
    implementation(projects.plugin.market)
    implementation(projects.library.error)
}
