package app.oguzhanozgokce.midmoney.remoteconfig

interface RemoteConfig {
    suspend fun activate()
    fun getBoolean(key: String, default: Boolean): Boolean
    fun getString(key: String, default: String): String
}
