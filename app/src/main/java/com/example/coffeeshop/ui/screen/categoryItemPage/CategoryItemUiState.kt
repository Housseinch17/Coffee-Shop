package com.example.coffeeshop.ui.screen.categoryItemPage

import androidx.compose.runtime.Immutable
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart

@Immutable
data class CategoryItemUiState(
    val categoryItemsCart: CategoryItemsCart = CategoryItemsCart()
)
