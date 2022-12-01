package com.example.everypractice.data.domain.login

import com.example.everypractice.data.domain.*
import com.example.everypractice.data.models.*
import com.example.everypractice.data.repository.*
import com.example.everypractice.di.*
import com.example.everypractice.utils.Result.*
import com.google.firebase.auth.*
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

data class Update(
    val user: FirebaseUser,
    val name: String?
)

class UpdateUserUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Update, Boolean>(dispatcher) {
    override suspend fun execute(parameters: Update): Boolean {

        return when (
            val statusUpdate = loginRepository.updateProfile(parameters)
        ) {
            is Success -> {
                statusUpdate.data
            }
            is Error -> throw statusUpdate.exception
            Loading -> throw IllegalStateException()
        }

    }

}








