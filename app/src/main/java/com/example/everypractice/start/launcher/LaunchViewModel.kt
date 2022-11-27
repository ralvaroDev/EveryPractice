package com.example.everypractice.start.launcher

/*


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
*/

sealed class LaunchNavigatonAction {
    object NavigateToOnboardingAction : LaunchNavigatonAction()
    object NavigateToMainActivityAction : LaunchNavigatonAction()
}