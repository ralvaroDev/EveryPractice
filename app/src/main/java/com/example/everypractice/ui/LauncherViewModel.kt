package com.example.everypractice.ui

import androidx.lifecycle.*
import com.example.everypractice.data.domain.onboarding.*
import com.example.everypractice.ui.StartView.*
import com.example.everypractice.utils.*
import com.example.everypractice.utils.Result.Loading
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.flow.*
import javax.inject.*

@HiltViewModel
class LauncherViewModel @Inject constructor(
    initialStateUseCase: InitialStateUseCase
) : ViewModel() {

    val launchDestination = initialStateUseCase(Unit).map {
        when (it.data) {
            ONBOARDING.ordinal -> ONBOARDING
            LOGIN.ordinal -> LOGIN
            else -> MAIN
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Loading)

}

enum class StartView {
    ONBOARDING, LOGIN, MAIN
}
