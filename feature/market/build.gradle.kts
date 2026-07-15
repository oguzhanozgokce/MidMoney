plugins {
    alias(libs.plugins.midmoney.android.feature)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.feature.market"
}

dependencies {
    implementation(project(":plugin:market"))
}
