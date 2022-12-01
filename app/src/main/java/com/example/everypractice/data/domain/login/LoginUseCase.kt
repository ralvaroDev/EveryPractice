package com.example.everypractice.data.domain.login

import com.example.everypractice.data.domain.*
import com.example.everypractice.data.models.*
import com.example.everypractice.data.repository.*
import com.example.everypractice.di.*
import com.example.everypractice.utils.Result.*
import kotlinx.coroutines.*
import javax.inject.*

class LoginUseCase @Inject constructor(
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