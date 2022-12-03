package com.example.everypractice.data.domain.signup

import com.example.everypractice.data.domain.*
import com.example.everypractice.data.models.login.*
import com.example.everypractice.data.repository.*
import com.example.everypractice.di.*
import com.example.everypractice.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

class SignUpUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : SuspendFlowUseCase<LoginCredentials, UserStatus>(dispatcher) {

    override suspend fun execute(parameters: LoginCredentials): Flow<Result<UserStatus>> {
        return loginRepository.signUpWithFirebase(parameters)
    }

}