package app.oguzhanozgokce.midmoney.plugin.user.data

import app.oguzhanozgokce.midmoney.error.ErrorHandler
import app.oguzhanozgokce.midmoney.plugin.user.domain.model.AuthError
import app.oguzhanozgokce.midmoney.plugin.user.domain.model.AuthException
import app.oguzhanozgokce.midmoney.plugin.user.domain.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val errorHandler: ErrorHandler,
) : AuthRepository {

    override val currentUserId: Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.uid)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override val currentUserEmail: Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.email)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override fun isCurrentlyLoggedIn(): Boolean = auth.currentUser != null

    // The AuthResult is not needed — only whether the call succeeded. `T` is fixed to Unit by the
    // declared return type, so the lambda coerces to Unit without an explicit `Unit` statement.
    override suspend fun login(email: String, password: String): Result<Unit> =
        errorHandler.call(transform = ::toAuthException) {
            auth.signInWithEmailAndPassword(email, password).await()
        }

    override suspend fun register(email: String, password: String): Result<Unit> =
        errorHandler.call(transform = ::toAuthException) {
            auth.createUserWithEmailAndPassword(email, password).await()
        }

    override fun logout() {
        auth.signOut()
    }
}

private fun toAuthException(throwable: Throwable): Throwable = AuthException(
    when (throwable) {
        is FirebaseAuthWeakPasswordException -> AuthError.WeakPassword
        is FirebaseAuthUserCollisionException -> AuthError.InvalidCredentials
        is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials
        is FirebaseAuthInvalidUserException -> AuthError.NoAccount
        is FirebaseNetworkException -> AuthError.Network
        else -> AuthError.Unknown
    },
)
