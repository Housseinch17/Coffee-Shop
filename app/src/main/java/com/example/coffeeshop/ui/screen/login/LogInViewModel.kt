package com.example.coffeeshop.ui.screen.login

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.AuthState
import com.example.coffeeshop.domain.usecase.firebaseUsecase.GetUsernameUseCase
import com.example.coffeeshop.domain.usecase.firebaseUsecase.LogInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val getUsernameUseCase: GetUsernameUseCase,
) : ViewModel() {
    private val _logInUiState: MutableStateFlow<LogInUiState> = MutableStateFlow(LogInUiState())
    val logInUiState: StateFlow<LogInUiState> = _logInUiState.asStateFlow()

    private val _sharedFlow: MutableSharedFlow<String> = MutableSharedFlow()
    val sharedFlow: SharedFlow<String> = _sharedFlow.asSharedFlow()

    override fun onCleared() {
        super.onCleared()
        Log.d("MyTag", "LogIn destroyed")
    }


    private fun emitSharedFlow(error: String){
        viewModelScope.launch {
            _sharedFlow.emit(error)
        }
    }

    fun getUsername(){
        viewModelScope.launch {
            _logInUiState.update { newState->
                newState.copy(userName = getUsernameUseCase.getUsername())
            }
        }
    }

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            _logInUiState.update { newState ->
                newState.copy(authState = AuthState.Loading)
            }
            if (email.isEmpty() || password.isEmpty()) {
                emitSharedFlow("Email and Password can't be empty")
                _logInUiState.update { newState->
                    newState.copy(authState = AuthState.UnAuthenticated)
                }
            }
            else{
                val response = logInUseCase.logIn(email,password)
                if(response is AuthState.Error){
                    emitSharedFlow(response.message)
                }
                _logInUiState.update { newState->
                    newState.copy(authState = response)
                }
            }
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