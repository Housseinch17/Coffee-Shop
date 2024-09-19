package com.example.coffeeshop.ui.screen.offerItemPage

import androidx.compose.runtime.Immutable
import com.example.coffeeshop.data.model.shoppingCart.OfferCart

@Immutable
data class OfferItemUiState(
    val offerCart: OfferCart = OfferCart()
)
