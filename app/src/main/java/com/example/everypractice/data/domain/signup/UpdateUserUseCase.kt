package com.example.everypractice.data.domain.signup

import com.example.everypractice.data.*
import com.example.everypractice.di.*
import com.example.everypractice.utils.*
import com.example.everypractice.utils.Result.Error
import com.example.everypractice.utils.Result.Success
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*
import javax.inject.*
import kotlin.coroutines.*

class UpdateUserUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun receiveSignUpResponseFromServer(name: String): Flow<Result<Unit>>{
        return withContext(dispatcher){
            suspendCancellableCoroutine { result ->
                firebaseRepository.updateUser(name)?.addOnCompleteListener {
                    if (it.isSuccessful){
                        //Timber.d("The update of the user was Success")
                        result.resume(flow { emit(Success(Unit)) })
                    } else {
                        Timber.d("Error updating user: ${it.exception?.message}")
                        result.resume(flow { emit(Error(it.exception as Exception)) })
                    }
                }
            }
        }
    }
}
