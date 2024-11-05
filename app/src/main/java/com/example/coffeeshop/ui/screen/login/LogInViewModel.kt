package com.example.coffeeshop.ui.screen.login

import android.util.Log
import android.util.Patterns
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase.GetCurrentUserUseCase
import com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase.LogInUseCase
import com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase.ResetPasswordUseCase
import com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase.SaveSharedPrefUsernameUseCase
import com.example.coffeeshop.ui.screen.settingspage.PasswordChangement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val saveSharedPrefUsernameUseCase: SaveSharedPrefUsernameUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    private val _logInUiState: MutableStateFlow<LogInUiState> = MutableStateFlow(LogInUiState())
    val logInUiState: StateFlow<LogInUiState> = _logInUiState.asStateFlow()

    private val _sharedFlow: MutableSharedFlow<String> = MutableSharedFlow()
    val sharedFlow: SharedFlow<String> = _sharedFlow.asSharedFlow()


    init {
        showLoader()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization","login destroyed")
    }

    private fun showLoader(){
        viewModelScope.launch {
            _logInUiState.update { newState->
                newState.copy(isLoading = true)
            }
            delay(500L)
            _logInUiState.update { newState->
                newState.copy(isLoading = false)
            }
        }
    }

    private fun emitSharedFlow(message: String) {
        viewModelScope.launch {
            _sharedFlow.emit(message)
        }
    }


    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(authState = AuthState.Loading)
            }
            if (email.isEmpty() || password.isEmpty()) {
                emitSharedFlow("Email and Password can't be empty")
                _logInUiState.update { newState ->
                    newState.copy(authState = AuthState.NotLoggedIn)
                }
            } else {
                val response = logInUseCase.logIn(email, password)
                if (response is AuthState.Error) {
                    emitSharedFlow(response.message)
                    _logInUiState.update { newState->
                        newState.copy(authState = AuthState.NotLoggedIn)
                    }
                } else if (response is AuthState.LoggedIn) {
                    //save the user in sharedPreference if its logged in
                        getCurrentUserAndSaveIt()
                    _logInUiState.update { newState ->
                        newState.copy(authState = AuthState.LoggedIn)
                    }
                }
            }
        }
    }

    fun resetShowDialog() {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(resetShowDialog = true)
            }
        }
    }

    fun resetHideDialog() {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(resetShowDialog = false)
            }
        }
    }

    fun onResetEmailValue(email: String){
        viewModelScope.launch {
            _logInUiState.update { newState->
                newState.copy(resetEmailValue = email)
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _logInUiState.update { newState ->
                    newState.copy(resetPassword = PasswordChangement.IsLoading)
                }
                val resetPassword = resetPasswordUseCase.resetPassword(email)

                Log.d("MyTag", "resetPassword $resetPassword")
                _logInUiState.update { newState ->
                    newState.copy(
                        resetPassword = resetPassword,
                        resetShowDialog = false
                    )
                }
                when (resetPassword) {
                    is PasswordChangement.Error -> emitSharedFlow(resetPassword.errorMessage)
                    is PasswordChangement.Success -> {
                        Log.d("MyTag",_sharedFlow.toString())
                        emitSharedFlow(resetPassword.successMessage)
                        Log.d("MyTag",_sharedFlow.toString())
                        _logInUiState.update { newState->
                            newState.copy(resetEmailValue = "")
                        }
                    }
                    else -> {
                        emitSharedFlow("Check if email exists!")
                    }
                }
                Log.d("MyTag", _logInUiState.value.resetPassword.toString())
            } else {
                emitSharedFlow("Email not valid")
            }
        }
    }

    private suspend fun getCurrentUserAndSaveIt() {
            val currentUsername = getCurrentUserUseCase.getCurrentUser()
            saveSharedPrefUsernameUseCase.saveUsername(currentUsername)
    }

    fun setEmail(email: String) {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(emailValue = email)
            }
        }
    }

    fun setPassword(password: String) {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(passwordValue = password)
            }
        }
    }

    fun setShowPassword() {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(
                    showPassword = !newState.showPassword,
                )
            }
        }
    }

    fun getIconVisibility(): ImageVector {
        val showPassword = _logInUiState.value.showPassword
        return if (showPassword) {
            Icons.Filled.Visibility
        } else {
            Icons.Filled.VisibilityOff
        }
    }
}

sealed interface AuthState {
    data object LoggedIn : AuthState
    data object NotLoggedIn : AuthState
    data object Loading : AuthState
    data class Error(val message: String) : AuthState
}
