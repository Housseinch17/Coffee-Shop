package com.example.coffeeshop.ui.screen

data class AuthenticationUiState(
    val signOut: SignOutResponse = SignOutResponse.InitialState,
    val username: String? = "",
    val signOutShowDialog: Boolean = false,
    
)
