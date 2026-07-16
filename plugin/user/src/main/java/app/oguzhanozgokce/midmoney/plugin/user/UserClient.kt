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
    suspend fun login(email: String, password: String): Result<Unit> = repository.login(email, password)
    suspend fun register(email: String, password: String): Result<Unit> = repository.register(email, password)
    fun logout() = repository.logout()

    /**
     * Signs the user in, creating the account on the fly if it does not exist yet. Keeps the demo to
     * a single login screen: the first sign-in for an email registers it, later ones just log in.
     */
    suspend fun loginOrRegister(email: String, password: String): Result<Unit> {
        val login = repository.login(email, password)
        return if (login.isSuccess) login else repository.register(email, password)
    }
}
