package com.example.coffeeshop.ui.screen.settingspage

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase.ChangePasswordUseCase
import com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase.GetCurrentUserUseCase
import com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
class SettingsViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _settingsUiState: MutableStateFlow<SettingsUiState> =
        MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    private val _emitValue: MutableSharedFlow<String> = MutableSharedFlow()
    val emitValue: SharedFlow<String> = _emitValue.asSharedFlow()

    init {
        Log.d("ViewModelInitialization", "settings")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization", "settings destroyed")
    }

    fun resetShowDialog() {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(resetShowDialog = true)
            }
        }
    }

    fun resetHideDialog() {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(resetShowDialog = false)
            }
        }
    }

    fun newPasswordValueChange(newPassword: String) {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(newPasswordValue = newPassword)
            }
        }
    }

    fun confirmNewPasswordValueChange(confirmNewPassword: String) {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(confirmNewPasswordValue = confirmNewPassword)
            }
        }
    }

    fun setShowPassword() {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(
                    showPassword = !newState.showPassword,
                )
            }
        }
    }

    fun setConfirmShowPassword() {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(
                    confirmShowPassword = !newState.confirmShowPassword,
                )
            }
        }
    }

    private fun emitValue(newText: String) {
        viewModelScope.launch {
            _emitValue.emit(newText)
        }
    }

    fun changePassword(email: String, newPassword: String, confirmNewPassword: String) {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(
                    showText = false,
                    confirmShowText = false,
                    passwordChangement = PasswordChangement.InitialState
                )
            }
            if (newPassword.length < 6) {
                _settingsUiState.update { newState ->
                    newState.copy(showText = true)
                }
            } else if (confirmNewPassword.length < 6) {
                _settingsUiState.update { newState ->
                    newState.copy(confirmShowText = true)
                }
            } else if (newPassword != confirmNewPassword) {
                emitValue("New password and confirm password should match")
                _settingsUiState.update { newState ->
                    newState.copy(passwordChangement = PasswordChangement.Error("Error"))
                }
            } else {
                val passwordChangement =
                    changePasswordUseCase.changePassword(email = email, newPassword = newPassword)
                _settingsUiState.update { newState ->
                    newState.copy(
                        newPasswordValue = "",
                        confirmNewPasswordValue = "",
                        showText = false,
                        confirmShowText = false,
                        passwordChangement = passwordChangement,
                    )
                }
                when (passwordChangement) {
                    is PasswordChangement.Error -> emitValue(passwordChangement.errorMessage)
                    is PasswordChangement.Success -> emitValue(passwordChangement.successMessage)
                    else -> {}
                }
            }
        }
    }

    fun resetPassword() {
        viewModelScope.launch {
            _settingsUiState.update { newState ->
                newState.copy(resetPassword = PasswordChangement.IsLoading)
            }
            val resetPassword = resetPasswordUseCase.resetPassword(getCurrentUserUseCase.getCurrentUser()!!)

            Log.d("MyTag", "resetPassword $resetPassword")
            _settingsUiState.update { newState ->
                newState.copy(
                    resetPassword = resetPassword,
                    resetShowDialog = false
                )
            }
            when (resetPassword) {
                is PasswordChangement.Error -> emitValue(resetPassword.errorMessage)
                is PasswordChangement.Success -> emitValue(resetPassword.successMessage)
                else -> {}
            }
            Log.d("MyTag", _settingsUiState.value.resetPassword.toString())
        }
    }

    fun getIconVisibility(showPassword: Boolean): ImageVector {
        return if (showPassword) {
            Icons.Filled.Visibility
        } else {
            Icons.Filled.VisibilityOff
        }
    }
}


sealed interface PasswordChangement {
    data class Success(val successMessage: String) : PasswordChangement
    data class Error(val errorMessage: String) : PasswordChangement
    data object InitialState : PasswordChangement
    data object IsLoading : PasswordChangement
}