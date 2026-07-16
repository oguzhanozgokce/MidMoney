package app.oguzhanozgokce.midmoney.plugin.user.data

import app.oguzhanozgokce.midmoney.common.coroutines.DispatcherProvider
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val dispatchers: DispatcherProvider,
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

    override suspend fun login(email: String, password: String): Result<Unit> =
        withContext(dispatchers.io) {
            runCatching {
                auth.signInWithEmailAndPassword(email, password).await()
                Unit
            }.toFriendlyResult()
        }

    override suspend fun register(email: String, password: String): Result<Unit> =
        withContext(dispatchers.io) {
            runCatching {
                auth.createUserWithEmailAndPassword(email, password).await()
                Unit
            }.toFriendlyResult()
        }

    override fun logout() {
        auth.signOut()
    }
}

/** Replaces raw Firebase exceptions with short, user-facing messages. */
private fun <T> Result<T>.toFriendlyResult(): Result<T> =
    recoverCatching { throw AuthException(it.toAuthMessage()) }

private fun Throwable.toAuthMessage(): String = when (this) {
    is FirebaseAuthWeakPasswordException -> "Password must be at least 6 characters."
    is FirebaseAuthUserCollisionException -> "Incorrect password for this account."
    is FirebaseAuthInvalidCredentialsException -> "Incorrect email or password."
    is FirebaseAuthInvalidUserException -> "No account found for this email."
    is FirebaseNetworkException -> "No internet connection. Check your network and try again."
    else -> "Something went wrong. Please try again."
}

private class AuthException(message: String) : Exception(message)
