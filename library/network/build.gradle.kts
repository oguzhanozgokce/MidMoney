import java.util.Properties

plugins {
    alias(libs.plugins.midmoney.android.library)
    alias(libs.plugins.midmoney.android.hilt)
}

// Read the Finnhub API key from local.properties (never committed) or an env var (for CI).
val finnhubApiKey: String = run {
    val props = Properties()
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { props.load(it) }
    props.getProperty("finnhub.apiKey") ?: System.getenv("FINNHUB_API_KEY").orEmpty()
}

android {
    namespace = "app.oguzhanozgokce.midmoney.network"

    defaultConfig {
        buildConfigField("String", "FINNHUB_BASE_URL", "\"https://finnhub.io/api/v1/\"")
        buildConfigField("String", "FINNHUB_API_KEY", "\"$finnhubApiKey\"")
    }
}

dependencies {
    implementation(projects.library.logger)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
}
