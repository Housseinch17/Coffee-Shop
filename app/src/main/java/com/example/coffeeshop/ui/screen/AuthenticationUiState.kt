package com.example.coffeeshop.ui.screen

import com.example.coffeeshop.ui.screen.settingspage.PasswordChangement

data class AuthenticationUiState(
    val signOut: SignOutResponse = SignOutResponse.InitialState,
    val username: String? = "",
    val signOutShowDialog: Boolean = false,
    val resetShowDialog: Boolean = false,
    val resetPassword: PasswordChangement = PasswordChangement.InitialState,
    val resetEmailValue: String = "",
    )
