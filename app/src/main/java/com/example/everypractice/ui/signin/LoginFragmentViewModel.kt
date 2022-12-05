package com.example.everypractice.ui.signin

import androidx.lifecycle.*
import com.example.everypractice.data.domain.login.*
import com.example.everypractice.data.domain.onboarding.*
import com.example.everypractice.data.models.login.*
import com.example.everypractice.ui.StartView.MAIN
import com.example.everypractice.utils.*
import com.example.everypractice.utils.Result.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val initialStateActionUseCase: InitialStateActionUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    fun startLoginFromServer(
        emailUser: String,
        password: String
    ): StateFlow<Result<Unit>> {
        return flow {
            try {
                val response = withContext(Dispatchers.IO) {
                    val result = loginUseCase.receiveLoginResponseFromServer(
                        LoginCredentials(
                            emailUser,
                            password
                        )
                    )
                    //Actions to do if it is success
                    result.collectLatest { userStatusResult ->
                        if (userStatusResult is Success) {
                            setPreferenceLogged()
                        }
                    }
                    return@withContext result.first()
                }
                emit(response)
            } catch (e: Exception) {
                emit(Error(e))
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, Loading)
    }

    private fun setPreferenceLogged() {
        viewModelScope.launch {
            initialStateActionUseCase(MAIN)
        }
    }

}