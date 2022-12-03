package com.example.everypractice.data.repository

import com.example.everypractice.data.models.login.*
import com.example.everypractice.data.models.login.UserResponseStatus.DONE
import com.example.everypractice.data.models.login.UserResponseStatus.ERROR
import com.example.everypractice.utils.*
import com.example.everypractice.utils.Result.Error
import com.example.everypractice.utils.Result.Success
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*
import javax.inject.*
import kotlin.coroutines.*


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

    suspend fun loginWithFirebase2(userCredentials: LoginCredentials): Flow<UserStatus> {
        val (email, password) = userCredentials
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { result ->
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        result.resume(flow { emit(UserStatus(DONE, it.result.user)) })
                    } else {
                        Timber.d("Error with credentials in coroutines")
                        result.resume(flow { emit(UserStatus(ERROR, it.exception)) })

                    }
                }
            }
        }
    }
    
    suspend fun loginWithFirebaseSTABLE(userCredentials: LoginCredentials): Flow<Result<UserStatus>> {
        val (email, password) = userCredentials
        Timber.d("Before suspendCancelable")
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { result ->
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Timber.d("It was success")
                        result.resume(flow { emit(Success(UserStatus(DONE))) })
                        /*result.resume(Success(UserStatus(DONE)))*/
                    } else {
                        Timber.d("Error with credentials in coroutines")
                        result.resume(flow { emit(Error(it.exception as Exception)) })
                        /*result.resume(Error(it.exception as Exception))*/

                    }
                }
            }
        }
    }

    /**
     * Aqui necesito regresar un valor para usar el user y con este hacerle el update
     */
    suspend fun signUpWithFirebase(userCredentials: LoginCredentials): Flow<Result<UserStatus>> {

        val (email, password) = userCredentials
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { result ->
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        result.resume(flow { emit(Success(UserStatus(DONE))) })
                    } else {
                        Timber.d("Error with credentials in coroutines")
                        result.resume(flow { emit(Error(it.exception as Exception)) })
                    }
                }
            }
        }
    }

    //TODO ES NECESARIO EL LOGIN CREDENTIALS ENTERO?
    suspend fun updateProfile(name: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { result ->
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }
                firebaseAuth.currentUser!!.updateProfile(profileUpdates)
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