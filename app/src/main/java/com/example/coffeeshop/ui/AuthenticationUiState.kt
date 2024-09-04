package com.example.coffeeshop.ui

import com.example.coffeeshop.AuthenticationStatus
import com.example.coffeeshop.ui.navigation.CurrentDestination

data class AuthenticationUiState(
    val authenticationStatus: AuthenticationStatus = AuthenticationStatus.UnAuthenticated,
    val startDestination: CurrentDestination = CurrentDestination.LogInPage
)
