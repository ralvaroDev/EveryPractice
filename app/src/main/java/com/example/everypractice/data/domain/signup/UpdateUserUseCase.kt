package com.example.everypractice.data.domain.signup

import com.example.everypractice.data.domain.*
import com.example.everypractice.data.repository.*
import com.example.everypractice.di.*
import com.example.everypractice.utils.Result.*
import kotlinx.coroutines.*
import javax.inject.*

class UpdateUserUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<String, Boolean>(dispatcher) {
    override suspend fun execute(parameters: String): Boolean {

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
