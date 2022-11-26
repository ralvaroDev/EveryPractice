package com.example.everypractice.start.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everypractice.start.datastore.Result
import com.example.everypractice.start.datastore.data
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class LaunchViewModel (
    onboardingCompletedUsed: OnboardingCompletedUseCase
) : ViewModel() {

    val launchDestination = onboardingCompletedUsed(Unit).map { result ->
        if (result.data == false){
            LaunchNavigatonAction.NavigateToOnboardingAction
        } else {
            LaunchNavigatonAction.NavigateToMainActivityAction
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Result.Loading)
}

sealed class LaunchNavigatonAction {
    object NavigateToOnboardingAction : LaunchNavigatonAction()
    object NavigateToMainActivityAction : LaunchNavigatonAction()
}