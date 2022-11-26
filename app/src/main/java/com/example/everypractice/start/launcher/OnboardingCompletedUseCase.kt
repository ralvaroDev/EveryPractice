package com.example.everypractice.start.launcher

import com.example.everypractice.start.datastore.FlowUseCase
import com.example.everypractice.start.datastore.PreferenceStorage
import com.example.everypractice.start.datastore.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class OnboardingCompletedUseCase (
    private val preferenceStorage: PreferenceStorage,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<Unit, Boolean>(dispatcher) {
    override fun execute(parameters: Unit): Flow<Result<Boolean>> =
        preferenceStorage.onboardingCompleted.map {
            Result.Success(it)
        }
}