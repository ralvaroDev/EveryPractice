package com.example.everypractice.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.everypractice.start.datastore.UserPreferenceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber


sealed class MyState {
    object Fetched : MyState()
    object Error : MyState()
}

class MainViewModel(
    networkStatusTracker: NetworkStatusTracker,
    private val userPreferencesRepository: UserPreferenceRepository
) : ViewModel() {

    /*
     * TRACKING INTERNET ============================
     */
    val state = networkStatusTracker.networkStatus
        .map(
            onAvailable = { MyState.Fetched },
            onUnavailable = { MyState.Error }
        )
    val stateHot = state.stateIn(viewModelScope, SharingStarted.Eagerly, MyState.Error)

    /*
     * Preference DataStore
     */

    /**
     * This val contains the info if the user is current logged
     */
    val userPreferenceFlow = userPreferencesRepository.userPreferenceFlow

    /**
     * This fun update the state of the user
     */
    fun updateExistUser(exist: Boolean, nameUser: String="No name",emailUser: String="No email"){
        Timber.d("SUCCES UPDATING USER: ${exist}")
        viewModelScope.launch {
            userPreferencesRepository.updateUserLastSession(exist,nameUser, emailUser)
        }
    }


}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val networkStatusTracker: NetworkStatusTracker,
    private val userPreferencesRepository: UserPreferenceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(networkStatusTracker,userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}