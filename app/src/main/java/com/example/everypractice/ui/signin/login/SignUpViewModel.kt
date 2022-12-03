package com.example.everypractice.ui.signin.login

import androidx.lifecycle.*
import com.example.everypractice.data.domain.onboarding.*
import com.example.everypractice.data.domain.signup.*
import com.example.everypractice.data.models.login.*
import com.example.everypractice.ui.StartView.MAIN
import com.example.everypractice.utils.*
import com.example.everypractice.utils.Result.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*
import javax.inject.*

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val initialStateActionUseCase: InitialStateActionUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {

    suspend fun startSignIn(
        emailUser: String,
        password: String,
        nameUser: String
    ): StateFlow<Result<Boolean>> {

        return withContext(Dispatchers.IO) {
            val result = signUpUseCase(LoginCredentials(emailUser, password, nameUser)).map {
                when (it) {
                    is Error -> { Error(it.exception) }
                    Loading -> { Loading }
                    is Success -> {
                        when (val update = updateUserUseCase(nameUser)) {
                            is Success -> {
                                Timber.d("go to Home because ${it.data.status.name}")
                                setPreferenceLogged()
                                Success(update.data)
                            }
                            is Error -> {
                                Timber.d("Receive exception updating User : ${update.exception.message}")
                                Error(update.exception)
                            }
                            Loading -> {}
                        }
                    }
                }
            } as Flow<Result<Boolean>>
            return@withContext result
        }.stateIn(viewModelScope, SharingStarted.Lazily, Loading)
    }

    private fun setPreferenceLogged() {
        viewModelScope.launch {
            initialStateActionUseCase(MAIN)
        }
    }

}