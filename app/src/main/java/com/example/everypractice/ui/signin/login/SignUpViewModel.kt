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

    fun startCreatingUser(
        emailUser: String,
        password: String,
        nameUser: String
    ): StateFlow<Result<Unit>> {
        return flow {
            try {
                val response = withContext(Dispatchers.IO){
                    val result = signUpUseCase.receiveSignUpResponseFromServer(
                        LoginCredentials(emailUser,password, nameUser)
                    )
                    if (result.first() is Success){
                        //Timber.d("Result in VM is success")
                        val update = updateUserUseCase.receiveSignUpResponseFromServer(nameUser)
                        update.collectLatest {
                            if (it is Success){
                                setPreferenceLogged()
                            }
                        }
                        //Timber.d("After of use func in VM to update user")
                        return@withContext update.first()
                    } else {
                        //Timber.d("Result in VM was error")
                        return@withContext result.first()
                    }
                }
                emit(response)
            } catch (e: Exception){
                Timber.d("Error in func creating User: ${e.message}")
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