package com.example.everypractice.ui.signin

import androidx.lifecycle.*
import com.example.everypractice.data.domain.login.*
import com.example.everypractice.data.domain.onboarding.*
import com.example.everypractice.data.domain.signup.*
import com.example.everypractice.data.models.*
import com.example.everypractice.data.repository.*
import com.example.everypractice.data.repository.UserResponseStatus.DONE
import com.example.everypractice.data.repository.UserResponseStatus.ERROR
import com.example.everypractice.ui.StartView.MAIN
import com.example.everypractice.ui.signin.datastore.*
import com.example.everypractice.utils.Result.*
import com.google.firebase.auth.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*
import javax.inject.*

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferenceRepository,
    private val loginUseCase: LoginUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val initialStateActionUseCase: InitialStateActionUseCase
) : ViewModel() {

    private val _nav = MutableStateFlow<UserResponseStatus>(ERROR)
    val nav = _nav.asStateFlow()

    fun startFakeLogin(emailUser: String, password: String){
        viewModelScope.launch {
            when (val s = loginUseCase(LoginCredentials(emailUser, password, null))) {
                is Success -> {
                    val status = s.data
                    when (status.status) {
                        DONE -> {
                            _nav.value = DONE
                            setPreferenceLogged()
                            Timber.d("go to Home")
                        }
                        ERROR -> {
                            _nav.value = ERROR
                            Timber.d("Bad Credentials")
                        }
                    }
                }
                is Error -> Timber.d("something wrong")
                Loading -> Timber.d("Loading")
            }
        }
    }

    private fun setPreferenceLogged(){
        viewModelScope.launch {
            initialStateActionUseCase(MAIN)
        }
    }

    fun startFakeSignIn(emailUser: String, password: String, nameUser: String){
        viewModelScope.launch {
            when (val s = signUpUseCase(LoginCredentials(emailUser,password, nameUser))){
                is Success -> {
                    val status = s.data
                    when(status.status){
                        DONE -> {
                            when(val update = updateUserUseCase(Update(status.value as FirebaseUser, nameUser))
                            ){
                                is Success -> update.data
                                is Error -> update.exception
                                Loading -> throw IllegalStateException()
                            }
                            Timber.d("creation of User correct")
                        }
                        ERROR -> Timber.d("bad Credentials for create new user")
                    }
                }
                is Error -> Timber.d("something wrong")
                Loading -> Timber.d("Loading")
            }
        }
    }

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
    fun updateExistUser(
        exist: Boolean,
        nameUser: String = "No name",
        emailUser: String = "No email"
    ) {
        Timber.d("SUCCES UPDATING USER: ${exist}")
        viewModelScope.launch {
            userPreferencesRepository.updateUserLastSession(exist, nameUser, emailUser)
        }
    }

}

sealed class GoToMainAction {
    object NavigationToMainActivity : GoToMainAction()
}