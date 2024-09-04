package com.example.coffeeshop.ui.screen.signup

import androidx.compose.runtime.Immutable

@Immutable
data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val accountStatus: AccountStatus = AccountStatus.NotCreated
)
