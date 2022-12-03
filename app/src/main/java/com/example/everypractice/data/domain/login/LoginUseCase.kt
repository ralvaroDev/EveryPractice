package com.example.everypractice.data.domain.login

import com.example.everypractice.data.domain.*
import com.example.everypractice.data.models.login.*
import com.example.everypractice.data.models.login.UserResponseStatus.DONE
import com.example.everypractice.data.models.login.UserResponseStatus.ERROR
import com.example.everypractice.data.repository.*
import com.example.everypractice.di.*
import com.example.everypractice.utils.*
import com.example.everypractice.utils.Result.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

class LoginUseCaseDeprecated @Inject constructor(
    private val loginRepository: LoginRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<LoginCredentials, UserStatus>(dispatcher) {

    override suspend fun execute(parameters: LoginCredentials): UserStatus {

        return when (
            val statusSignIn =
                loginRepository.loginWithFirebase(parameters)
        ) {
            is Success -> {
                statusSignIn.data
            }
            is Error -> throw statusSignIn.exception
            Loading -> throw IllegalStateException()
        }

    }

}

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : SuspendFlowUseCase<LoginCredentials, UserStatus>(dispatcher) {
    override suspend fun execute(parameters: LoginCredentials): Flow<Result<UserStatus>> {
        return loginRepository.loginWithFirebase2(parameters).map {
            when (it.status) {
                DONE -> Success(it)
                ERROR -> Success(it)
                else -> Loading
            }
        }

    }

}

class LoginUseCaseSTABLE @Inject constructor(
    private val loginRepository: LoginRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : SuspendFlowUseCase<LoginCredentials, UserStatus>(dispatcher) {
    override suspend fun execute(parameters: LoginCredentials): Flow<Result<UserStatus>> {
        //TODO CON ESTE MODO OBTENEMOS DOBLE RECEPCION DE VALOR
        /*return flow { emit(loginRepository.loginWithFirebaseSTABLE(parameters)) }*/
        return loginRepository.loginWithFirebaseSTABLE(parameters)
    }

}