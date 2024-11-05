package com.example.coffeeshop.ui.screen.settingspage

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsUiState(
    val newPasswordValue: String = "",
    val confirmNewPasswordValue: String = "",
    val showPassword: Boolean = false,
    val confirmShowPassword: Boolean = false,
    val showText: Boolean = false,
    val confirmShowText: Boolean = false,
    val passwordChangement: PasswordChangement = PasswordChangement.InitialState,
)
