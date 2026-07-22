plugins {
    alias(libs.plugins.midmoney.android.feature)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.feature.watchlist"
}

dependencies {
    implementation(projects.plugin.market)
    implementation(projects.library.error)
}
