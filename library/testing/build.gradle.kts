plugins {
    alias(libs.plugins.midmoney.android.library)
}

android {
    namespace = "app.oguzhanozgokce.midmoney.testing"
}

dependencies {
    // `api`, not `implementation`: these types are part of this module's public surface —
    // MainDispatcherRule extends JUnit's TestWatcher, FakeNavigator implements Navigator and
    // FakeAnalytics implements Analytics, so every consumer sees them.
    api(projects.library.navigation)
    api(projects.library.event)
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
}
