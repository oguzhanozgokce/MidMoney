plugins {
    `kotlin-dsl`
}

group = "app.oguzhanozgokce.midmoney.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "midmoney.android.application"
            implementationClass = "app.oguzhanozgokce.midmoney.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "midmoney.android.library"
            implementationClass = "app.oguzhanozgokce.midmoney.convention.AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "midmoney.android.compose"
            implementationClass = "app.oguzhanozgokce.midmoney.convention.AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "midmoney.android.hilt"
            implementationClass = "app.oguzhanozgokce.midmoney.convention.AndroidHiltConventionPlugin"
        }
        register("androidFeature") {
            id = "midmoney.android.feature"
            implementationClass = "app.oguzhanozgokce.midmoney.convention.AndroidFeatureConventionPlugin"
        }
    }
}
