package com.example.everypractice.data.domain.signup

import com.example.everypractice.data.domain.*
import com.example.everypractice.data.repository.*
import com.example.everypractice.di.*
import com.example.everypractice.utils.Result.*
import com.google.firebase.auth.*
import kotlinx.coroutines.*
import javax.inject.*

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

data class Update(
    val user: FirebaseUser,
    val name: String?
)