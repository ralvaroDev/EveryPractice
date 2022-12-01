package com.example.everypractice.data.repository

import com.example.everypractice.data.domain.login.*
import com.example.everypractice.data.models.*
import com.example.everypractice.data.repository.UserResponseStatus.DONE
import com.example.everypractice.data.repository.UserResponseStatus.ERROR
import com.example.everypractice.utils.*
import com.example.everypractice.utils.Result.Error
import com.example.everypractice.utils.Result.Success
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.*
import kotlinx.coroutines.*
import timber.log.*
import javax.inject.*
import kotlin.coroutines.*

enum class UserResponseStatus {
    DONE, ERROR
}

data class UserStatus(
    val status: UserResponseStatus,
    val value: Any? = null
)

class LoginRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun loginWithFirebase(userCredentials: LoginCredentials): Result<UserStatus> {

        val (email, password) = userCredentials
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { result ->
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //TODO AQUI NECESITO REGRESAR EL USUARIO? O UN NULL
                        result.resume(Success(UserStatus(DONE)))
                    } else {
                        try {
                            Timber.d("Error with credentials in corroutines")
                            val errorAuth = (it.exception as FirebaseAuthException).errorCode
                            result.resume(Success(UserStatus(ERROR, errorAuth)))
                        } catch (e: Exception) {
                            Timber.d("addOnFailureListener")
                            result.resume(Error(e))
                        }
                    }

                }
            }
        }
    }

    /**
     * Aqui necesito regresar un valor para usar el user y con este hacerle el update
     */
    suspend fun signUpWithFirebase(userCredentials: LoginCredentials): Result<UserStatus> {

        val (email, password) = userCredentials
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { result ->
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //TODO DEBERIA TRAER AQUI EL PREFERENCE
                        result.resume(Success(UserStatus(DONE, it.result.user!!)))
                    } else {
                        try {
                            //TODO ESTE EXTIENE DEL EXCEPTION, DEBE CHAPARSE COMO TAL VERDAD?
                            val errorAuth = (it.exception as FirebaseAuthException).errorCode
                            Timber.d("Repo Sign Error: $errorAuth")
                            result.resume(Success(UserStatus(ERROR, errorAuth)))
                        } catch (e: Exception) {
                            Timber.d("Repo Sign Error Exception ${e.message}")
                            result.resume(Error(e))
                        }
                    }
                }
            }
        }
    }

    //TODO ES NECESARIO EL LOGIN CREDENTIALS ENTERO?
    suspend fun updateProfile(update: Update): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { result ->
                val profileUpdates = userProfileChangeRequest {
                    displayName = update.name
                }
                update.user.updateProfile(profileUpdates)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            result.resume(Success(true))
                        } else {
                            val e = it.exception!!
                            result.resume(Error(e))
                        }
                    }
            }
        }
    }

}