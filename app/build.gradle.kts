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
    implementation(project(":library:designsystem"))
    implementation(project(":library:common"))
    implementation(project(":library:logger"))
    implementation(project(":library:network"))
    implementation(project(":library:datastore"))
    implementation(project(":library:event"))
    implementation(project(":library:navigation"))
    implementation(project(":library:mvi"))
    implementation(project(":plugin:user"))

    implementation(project(":feature:login"))
    implementation(project(":feature:market"))
    implementation(project(":feature:detail"))
    implementation(project(":feature:watchlist"))

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
