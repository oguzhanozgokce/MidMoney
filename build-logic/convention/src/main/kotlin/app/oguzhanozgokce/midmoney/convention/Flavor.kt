package app.oguzhanozgokce.midmoney.convention

import com.android.build.api.dsl.ApplicationExtension

enum class FlavorDimension { environment }

enum class Flavor(
    val dimension: FlavorDimension,
    val appName: String,
    val baseUrl: String,
    val isPreprod: Boolean,
    val versionNameSuffix: String? = null,
) {
    preprod(
        dimension = FlavorDimension.environment,
        appName = "MidMoney Preprod",
        baseUrl = "https://finnhub.io/api/v1/",
        isPreprod = true,
        versionNameSuffix = "-preprod",
    ),
    prod(
        dimension = FlavorDimension.environment,
        appName = "MidMoney",
        baseUrl = "https://finnhub.io/api/v1/",
        isPreprod = false,
    ),
}

fun configureFlavors(extension: ApplicationExtension) {
    with(extension) {
        flavorDimensions += FlavorDimension.environment.name
        productFlavors {
            Flavor.entries.forEach { flavor ->
                create(flavor.name) {
                    dimension = flavor.dimension.name
                    flavor.versionNameSuffix?.let { versionNameSuffix = it }
                    resValue("string", "app_name", flavor.appName)
                    buildConfigField("String", "BASE_URL", "\"${flavor.baseUrl}\"")
                    buildConfigField("String", "ENVIRONMENT", "\"${flavor.name}\"")
                    buildConfigField("boolean", "IS_PREPROD", flavor.isPreprod.toString())
                }
            }
        }
    }
}
