plugins {
    alias(libs.plugins.midmoney.android.application)
    alias(libs.plugins.midmoney.android.compose)
    alias(libs.plugins.midmoney.android.hilt)
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
