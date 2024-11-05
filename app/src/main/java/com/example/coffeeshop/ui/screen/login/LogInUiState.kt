package com.example.coffeeshop.ui.screen.login

import androidx.compose.runtime.Immutable
import com.example.coffeeshop.ui.screen.settingspage.PasswordChangement

@Immutable
data class LogInUiState(
    val emailValue: String = "",
    val passwordValue: String = "",
    val showPassword: Boolean = false,
    val authState: AuthState = AuthState.NotLoggedIn,
    val userName: String? = null,
    val isLoading: Boolean = true,
    val resetShowDialog: Boolean = false,
    val resetPassword: PasswordChangement = PasswordChangement.InitialState,
    val resetEmailValue: String = "",
    
)
