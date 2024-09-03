package com.example.coffeeshop.ui.screen.login

import androidx.compose.runtime.Immutable
import com.example.coffeeshop.AuthState

@Immutable
data class LogInUiState(
    val emailValue: String = "",
    val passwordValue: String = "",
    val showPassword: Boolean = false,
    val authState: AuthState = AuthState.UnAuthenticated,
    val userName: String? = null
)
