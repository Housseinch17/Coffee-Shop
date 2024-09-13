package com.example.coffeeshop.ui.screen

data class AuthenticationUiState(
    val signOut: SignOutResponse = SignOutResponse.Loading,
    val username: String? = "",
    
)
