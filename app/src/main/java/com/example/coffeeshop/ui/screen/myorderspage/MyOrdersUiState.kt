package com.example.coffeeshop.ui.screen.myorderspage

import androidx.compose.runtime.Stable
import com.example.coffeeshop.data.model.ShoppingCart

@Stable
data class MyOrdersUiState(
    val myOrdersUiState: List<ShoppingCart> = emptyList()
)
