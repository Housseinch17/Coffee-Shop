package com.example.coffeeshop.ui.screen.homepage

import androidx.compose.runtime.Immutable

@Immutable
data class HomePageUiState(
    val response: FirebaseResponse = FirebaseResponse.Loading
)
