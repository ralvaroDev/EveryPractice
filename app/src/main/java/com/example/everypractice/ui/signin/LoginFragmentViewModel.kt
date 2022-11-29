package com.example.everypractice.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.everypractice.ui.signin.datastore.UserPreferenceRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginFragmentViewModel(
    private val userPreferencesRepository: UserPreferenceRepository
) : ViewModel() {

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
    private val userPreferencesRepository: UserPreferenceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginFragmentViewModel::class.java)) {
            return LoginFragmentViewModel(userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}