package com.example.coffeeshop.ui.screen.shoppingcartpage

import androidx.compose.runtime.Stable
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart
import com.example.coffeeshop.data.model.shoppingCart.OfferCart

@Stable
data class ShoppingCartUiState(
    val categoryItemsList: MutableList<CategoryItemsCart> = mutableListOf(),
    val offersList: MutableList<OfferCart> = mutableListOf(),
    val totalPrice: Double = 0.0,
)
