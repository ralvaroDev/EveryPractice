package com.example.everypractice.data.domain.onboarding

import com.example.everypractice.data.domain.*
import com.example.everypractice.di.*
import com.example.everypractice.ui.*
import com.example.everypractice.ui.signin.datastore.*
import kotlinx.coroutines.*
import javax.inject.*

class InitialStateActionUseCase @Inject constructor(
    private val preferenceRepository: UserPreferenceRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<StartView, Unit>(dispatcher) {
    override suspend fun execute(parameters: StartView) {
        preferenceRepository.initialState(parameters)
    }
}