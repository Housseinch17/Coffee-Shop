package com.example.coffeeshop.ui.screen.login

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase.GetCurrentUserUseCase
import com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase.LogInUseCase
import com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase.SaveSharedPrefUsernameUseCase
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
        Log.d("MyTag", "LogIn Cleared")
    }

    private fun showLoader(){
        viewModelScope.launch {
            _logInUiState.update { newState->
                newState.copy(isLoading = true)
            }
            delay(1000L)
            _logInUiState.update { newState->
                newState.copy(isLoading = false)
            }
        }
    }

    fun setSignUpButton() {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(signUpEnabled = false)
            }
            delay(200L)
            _logInUiState.update { newState ->
                newState.copy(signUpEnabled = true)
            }
        }
    }

    private fun emitSharedFlow(error: String) {
        viewModelScope.launch {
            _sharedFlow.emit(error)
        }
    }


    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(authState = AuthState.Loading)
            }
            if (email.isEmpty() || password.isEmpty()) {
                emitSharedFlow("Email and Password can't be empty")
                delay(500L)
                _logInUiState.update { newState ->
                    newState.copy(authState = AuthState.NotLoggedIn)
                }
            } else {
                val response = logInUseCase.logIn(email, password)
                if (response is AuthState.Error) {
                    emitSharedFlow(response.message)
                } else if (response is AuthState.LoggedIn) {
                    //save the user in sharedPreference if its logged in
                    getCurrentUserAndSaveIt()
                }
                _logInUiState.update { newState ->
                    newState.copy(authState = response)
                }
            }
        }
    }

    private fun getCurrentUserAndSaveIt() {
        viewModelScope.launch {
            val currentUsername = getCurrentUserUseCase.getCurrentUser()
            saveSharedPrefUsernameUseCase.saveUsername(currentUsername)
        }
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
