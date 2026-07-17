package app.oguzhanozgokce.midmoney.plugin.user.domain.model

enum class AuthError {
    WeakPassword,
    InvalidCredentials,
    NoAccount,
    Network,
    Unknown,
}

class AuthException(val error: AuthError) : Exception()
