package app.oguzhanozgokce.midmoney.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRemoteConfigClient @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
) : RemoteConfig {

    override suspend fun activate() {
        remoteConfig.fetchAndActivate().await()
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        val value = remoteConfig.getValue(key)
        return if (value.source == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) default else value.asBoolean()
    }

    override fun getString(key: String, default: String): String {
        val value = remoteConfig.getValue(key)
        return if (value.source == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) default else value.asString()
    }
}
