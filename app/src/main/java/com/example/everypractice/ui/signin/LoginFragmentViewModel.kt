package com.example.everypractice.ui.signin

import androidx.lifecycle.*
import com.example.everypractice.data.domain.login.*
import com.example.everypractice.data.domain.onboarding.*
import com.example.everypractice.data.models.login.*
import com.example.everypractice.data.models.login.UserResponseStatus.DONE
import com.example.everypractice.data.models.login.UserResponseStatus.ERROR
import com.example.everypractice.ui.StartView.MAIN
import com.example.everypractice.utils.Result.*
import com.google.firebase.auth.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*
import javax.inject.*

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val loginUseCaseDeprecated: LoginUseCaseDeprecated,
    private val loginUseCase: LoginUseCase,
    private val loginUseCaseSTABLE: LoginUseCaseSTABLE,
    private val initialStateActionUseCase: InitialStateActionUseCase
) : ViewModel() {

    private val _nav = MutableStateFlow<UserResponseStatus>(ERROR)
    val nav = _nav.asStateFlow()

    fun startFakeLogin(emailUser: String, password: String) {
        viewModelScope.launch {
            when (val s = loginUseCaseDeprecated(LoginCredentials(emailUser, password, null))) {
                is Success -> {
                    when (s.data.status) {
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
                Loading -> {}
            }
        }
    }

    suspend fun login(
        emailUser: String,
        password: String
    ): StateFlow<com.example.everypractice.utils.Result<UserStatus>> {
        val result = viewModelScope.async {
            val start = loginUseCase(LoginCredentials(emailUser, password, null))
            /*.map {
            when(it){
                is Error -> it.exception
                is Success -> it.data
                Loading -> {}
            }
        }*/
            return@async start
        }
        return result.await().stateIn(viewModelScope, SharingStarted.Lazily, Loading)
    }

    suspend fun startLoginSTABLE(
        emailUser: String,
        password: String
    ): StateFlow<com.example.everypractice.utils.Result<UserStatus>> {
        /*val result = viewModelScope.async {
            //TODO MANDART DE AQUI EL PREFERENCE
            return@async loginUseCase2(LoginCredentials(emailUser, password, null))
        }*/
        //TODO, COMO HACER PARA QUE EMITA DESDE ANTES DE RECOGER LA RESPUESTA
        return withContext(Dispatchers.IO) {
            Timber.d("After Obtain result in VM")
            val result = loginUseCaseSTABLE(LoginCredentials(emailUser, password, null))
            result.collectLatest {
                if (it is Success) {
                    setPreferenceLogged()
                }
            }
            return@withContext result
        }.stateIn(viewModelScope, SharingStarted.Lazily, Loading)
/*
        return result.await().stateIn(viewModelScope, SharingStarted.Lazily, Loading)*/
    }

    /////////////////


    suspend fun startLogin(
        emailUser: String,
        password: String
    ): StateFlow<Any?> {
        val result = viewModelScope.async {
            val start = loginUseCase(LoginCredentials(emailUser, password, null))
                .map {
                    when (it) {
                        is Error -> {
                            Timber.d("Receiving some new LOGIN FATAL ERROR : ${it.exception.message}")
                            it.exception
                        }
                        is Success -> {
                            when (it.data.status) {
                                DONE -> {
                                    setPreferenceLogged()
                                    Timber.d("go to Home because ${it.data.status.name}")
                                    it.data.status

                                }
                                ERROR -> {
                                    try {
                                        Timber.d("Error with credentials: ${(it.data.value as Exception).message}")
                                        (it.data.value as FirebaseAuthException).errorCode
                                        it.data.status
                                    } catch (e: Exception) {
                                        Timber.d("Receive exception : ${it.data.value}")
                                        it.data.value
                                    }


                                }
                            }
                        }
                        Loading -> {}
                    }
                }
            return@async start
        }
        return result.await().stateIn(viewModelScope, SharingStarted.Lazily, Loading)
    }


    private fun setPreferenceLogged() {
        viewModelScope.launch {
            initialStateActionUseCase(MAIN)
        }
    }

}