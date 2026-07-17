package app.oguzhanozgokce.midmoney.common.config

interface AppConfig {
    val baseUrl: String
    val environment: String
    val isPreprod: Boolean
}
