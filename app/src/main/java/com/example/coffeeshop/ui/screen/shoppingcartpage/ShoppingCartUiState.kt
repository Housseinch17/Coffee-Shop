package com.example.coffeeshop.ui.screen.shoppingcartpage

import androidx.compose.runtime.Stable
import com.example.coffeeshop.data.model.ShoppingCart

@Stable
data class ShoppingCartUiState(
    val shoppingCart: ShoppingCart = ShoppingCart()
)

