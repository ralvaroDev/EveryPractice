package com.example.everypractice.ui.home

import androidx.lifecycle.*
import com.example.everypractice.data.domain.home.*
import com.example.everypractice.data.domain.onboarding.*
import com.example.everypractice.ui.StartView.LOGIN
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import javax.inject.*

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val initialStateActionUseCase: InitialStateActionUseCase,
    loadRecommendedSavedMoviesUseCase: LoadRecommendedSavedMoviesUseCase
) : ViewModel() {

    /**
     * Return a FlowList with only 5 random elements from favourite movies Database
     */
    val showRecommendedMoviesHome = loadRecommendedSavedMoviesUseCase(Unit)

    fun setPreferenceUnLogged() {
        viewModelScope.launch {
            initialStateActionUseCase(LOGIN)
        }
    }

}