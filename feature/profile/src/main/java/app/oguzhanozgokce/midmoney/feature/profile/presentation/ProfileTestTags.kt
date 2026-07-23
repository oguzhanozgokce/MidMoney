package app.oguzhanozgokce.midmoney.feature.profile.presentation

object ProfileTestTags {
    const val SCREEN = "profile.screen"
    const val ENV_BADGE = "profile.env_badge"
    const val LOGOUT = "profile.logout"
    const val VERSION = "profile.version"

    fun menu(key: String): String = "profile.menu.$key"
}
