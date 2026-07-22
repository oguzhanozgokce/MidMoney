plugins {
    alias(libs.plugins.midmoney.android.application)
    alias(libs.plugins.midmoney.android.compose)
    alias(libs.plugins.midmoney.android.hilt)
    alias(libs.plugins.google.services)
}

android {
    namespace = "app.oguzhanozgokce.midmoney"

    defaultConfig {
        applicationId = "app.oguzhanozgokce.midmoney"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
}

dependencies {
    implementation(projects.library.designsystem)
    implementation(projects.library.common)
    implementation(projects.library.logger)
    implementation(projects.library.network)
    implementation(projects.library.datastore)
    implementation(projects.library.event)
    implementation(projects.library.navigation)
    implementation(projects.library.mvi)
    implementation(projects.plugin.user)

    implementation(projects.feature.login)
    implementation(projects.feature.market)
    implementation(projects.feature.marketlist)
    implementation(projects.feature.detail)
    implementation(projects.feature.watchlist)
    implementation(projects.feature.profile)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
