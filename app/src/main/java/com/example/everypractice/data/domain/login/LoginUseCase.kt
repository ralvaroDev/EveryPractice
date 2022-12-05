package com.example.everypractice.data.domain.login

import com.example.everypractice.data.*
import com.example.everypractice.data.models.login.*
import com.example.everypractice.di.*
import com.example.everypractice.utils.*
import com.example.everypractice.utils.Result.Error
import com.example.everypractice.utils.Result.Success
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*
import javax.inject.*
import kotlin.coroutines.*

class LoginUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun receiveLoginResponseFromServer(credentials: LoginCredentials): Flow<Result<Unit>> {
        return withContext(dispatcher) {
            suspendCancellableCoroutine { result ->
                firebaseRepository.loginWithCredential(credentials).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Timber.d("It was success")
                        result.resume(flow { emit(Success(Unit)) })
                    } else {
                        Timber.d("Error with credentials or establishing connection")
                        result.resume(flow { emit(Error(it.exception as Exception)) })
                    }
                }
            }
        }

    }
}




















