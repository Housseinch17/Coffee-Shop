package com.example.coffeeshop.ui.screen.login

import androidx.compose.runtime.Immutable

@Immutable
data class LogInUiState(
    val emailValue: String = "",
    val passwordValue: String = "",
    val showPassword: Boolean = false,
    val authState: AuthState = AuthState.NotLoggedIn,
    val signUpEnabled: Boolean = false,
    val userName: String? = null,
    val isLoading: Boolean = true,
)
