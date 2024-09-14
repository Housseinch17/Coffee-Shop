package com.example.coffeeshop.ui.screen.categoryItemPage

import androidx.compose.runtime.Stable

@Stable
data class CategoryItemUiState(
    val count: Int = 0,
    val price: Int = 0,
    val total: Int = 0
)
