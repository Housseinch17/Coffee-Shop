package com.example.coffeeshop.ui.screen.allOffersDetail

import androidx.compose.runtime.Stable
import com.example.coffeeshop.data.model.offers.Offers

@Stable
data class AllOffersUiState(
    val allOffersList: List<Offers> = emptyList(),
    val isLoading: Boolean = false,
)
