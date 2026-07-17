package app.oguzhanozgokce.midmoney.plugin.user

import app.oguzhanozgokce.midmoney.plugin.user.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserClient @Inject constructor(
    private val repository: AuthRepository,
) {
    val currentUserId: Flow<String?> = repository.currentUserId
    val currentUserEmail: Flow<String?> = repository.currentUserEmail
    fun isCurrentlyLoggedIn(): Boolean = repository.isCurrentlyLoggedIn()
    fun logout() = repository.logout()

    suspend fun loginOrRegister(email: String, password: String): Result<Unit> {
        val login = repository.login(email, password)
        return if (login.isSuccess) login else repository.register(email, password)
    }
}
