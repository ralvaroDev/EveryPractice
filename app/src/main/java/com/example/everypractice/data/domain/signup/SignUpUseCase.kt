package com.example.everypractice.data.domain.signup

import com.example.everypractice.data.domain.*
import com.example.everypractice.data.models.*
import com.example.everypractice.data.repository.*
import com.example.everypractice.di.*
import com.example.everypractice.utils.Result.*
import kotlinx.coroutines.*
import javax.inject.*

class SignUpUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<LoginCredentials, UserStatus>(dispatcher) {

    override suspend fun execute(parameters: LoginCredentials): UserStatus {

        return when (
            val statusSignUp = loginRepository.signUpWithFirebase(parameters)
        ) {
            is Success -> {
                statusSignUp.data
            }
            is Error -> throw statusSignUp.exception
            Loading -> throw IllegalStateException()
        }

    }

}

/*
class SignUp @Inject constructor(
    private val loginRepository: LoginRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<LoginCredentials, UserStatus>(dispatcher) {
    override fun execute(parameters: LoginCredentials): Flow<Result<UserStatus>> {
        return when(
            val statusSignUp = loginRepository.signUpWithFirebase(parameters)
        )
    }

}
*/