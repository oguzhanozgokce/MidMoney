package app.oguzhanozgokce.midmoney.plugin.user.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserId: Flow<String?>
    fun isCurrentlyLoggedIn(): Boolean
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    fun logout()
}
