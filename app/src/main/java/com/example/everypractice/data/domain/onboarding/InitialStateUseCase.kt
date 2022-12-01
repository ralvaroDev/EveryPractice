package com.example.everypractice.data.domain.onboarding

import com.example.everypractice.data.domain.*
import com.example.everypractice.di.*
import com.example.everypractice.ui.signin.datastore.*
import com.example.everypractice.utils.*
import com.example.everypractice.utils.Result.Success
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*


class InitialStateUseCase @Inject constructor(
    private val preferenceRepository: UserPreferenceRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Int>(dispatcher) {

    override fun execute(parameters: Unit): Flow<Result<Int>> {
        return preferenceRepository.initialState.map {
            Success(it)
        }
    }

}