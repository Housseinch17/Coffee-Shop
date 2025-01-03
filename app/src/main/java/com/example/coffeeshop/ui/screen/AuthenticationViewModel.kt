package com.example.coffeeshop.ui.screen

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase.GetCurrentUserUseCase
import com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase.ResetPasswordUseCase
import com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase.SignOutUseCase
import com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase.GetSharedPrefUsernameUseCase
import com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase.SaveSharedPrefUsernameUseCase
import com.example.coffeeshop.ui.screen.settingspage.PasswordChangement
import com.example.coffeeshop.ui.util.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    application: Application,
    private val signOutUseCase: SignOutUseCase,
    private val saveSharedPrefUsernameUseCase: SaveSharedPrefUsernameUseCase,
    private val getSharedPrefUsernameUseCase: GetSharedPrefUsernameUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : AndroidViewModel(application = application) {
    private val _authenticationUiState: MutableStateFlow<AuthenticationUiState> =
        MutableStateFlow(AuthenticationUiState())
    val authenticationUiState: StateFlow<AuthenticationUiState> =
        _authenticationUiState.asStateFlow()

    private val _showMessage = MutableSharedFlow<String>()
    val showMessage = _showMessage.asSharedFlow()


    init {
        viewModelScope.launch {
            updateCurrentUserName()
        }
        Log.d("ViewModelInitialization", "Authentication created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization", "authentication destroyed")
    }

    private fun emitMessage(message: String = "No internet connection") {
        viewModelScope.launch {
            _showMessage.emit(message)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun updateCurrentUserName() {
        val currentUsername = getSharedPrefUsernameUseCase.getUsername()
        _authenticationUiState.update { newState ->
            newState.copy(username = currentUsername)
        }
    }

    //reset state while signing out
    fun resetSignOutState() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(signOut = SignOutResponse.InitialState)
            }
        }
    }

    fun resetShowDialog() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(signOutShowDialog = true)
            }
        }
    }

    fun resetHideDialog() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(signOutShowDialog = false)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(signOut = SignOutResponse.IsLoading)
            }
            val hasInternet = getApplication<Application>().isInternetAvailable()
            Log.d("hasInternet", "$hasInternet")
            if (hasInternet) {
                signOutUseCase.signOut()
                _authenticationUiState.update { newState ->
                    newState.copy(
                        signOut = SignOutResponse.Success,
                        signOutShowDialog = false
                    )
                }
                saveSharedPrefUsernameUseCase.saveUsername(null)
            } else {
                _authenticationUiState.update { newState ->
                    newState.copy(
                        signOut = SignOutResponse.Error,
                        signOutShowDialog = false
                    )
                }
                //show error to the user
                emitMessage()
            }
        }
    }

    fun resetResetShowDialog() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(resetShowDialog = true)
            }
        }
    }

    fun resetResetHideDialog() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(resetShowDialog = false)
            }
        }
    }

    fun onResetEmailValue(email: String){
        viewModelScope.launch {
            _authenticationUiState.update { newState->
                newState.copy(resetEmailValue = email)
            }
        }
    }

    fun resetPassword(email: String = "",resetPage: ResetPage) {
        viewModelScope.launch {
            if ((resetPage is ResetPage.LogInPage && Patterns.EMAIL_ADDRESS.matcher(email).matches()) || resetPage is ResetPage.SettingsPage) {
                _authenticationUiState.update { newState ->
                    newState.copy(resetPassword = PasswordChangement.IsLoading)
                }

                val resetPassword: PasswordChangement = when(resetPage){
                    ResetPage.LogInPage -> resetPasswordUseCase.resetPassword(email)
                    ResetPage.SettingsPage -> resetPasswordUseCase.resetPassword(getCurrentUserUseCase.getCurrentUser()!!)
                }


                Log.d("MyTag", "resetPassword $resetPassword")
                _authenticationUiState.update { newState ->
                    newState.copy(
                        resetPassword = resetPassword,
                        resetShowDialog = false
                    )
                }
                when (resetPassword) {
                    is PasswordChangement.Error -> emitMessage(resetPassword.errorMessage)
                    is PasswordChangement.Success -> {
                        Log.d("MyTag",_showMessage.toString())
                        emitMessage(resetPassword.successMessage)
                        Log.d("MyTag",_showMessage.toString())
                        _authenticationUiState.update { newState->
                            newState.copy(resetEmailValue = "")
                        }
                    }
                    else -> {
                        emitMessage("Check if email exists!")
                    }
                }
                Log.d("MyTag", _authenticationUiState.value.resetPassword.toString())
            } else {
                emitMessage("Email not valid")
            }
        }
    }
}

sealed interface SignOutResponse {
    data object IsLoading: SignOutResponse
    data object InitialState : SignOutResponse
    data object Success : SignOutResponse
    data object Error : SignOutResponse
}

sealed interface ResetPage{
    data object LogInPage: ResetPage
    data object SettingsPage: ResetPage
}