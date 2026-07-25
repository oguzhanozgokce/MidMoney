plugins {
    alias(libs.plugins.midmoney.android.library)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.testing"
}

dependencies {
    api(projects.library.navigation)
    api(projects.library.event)
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
}
