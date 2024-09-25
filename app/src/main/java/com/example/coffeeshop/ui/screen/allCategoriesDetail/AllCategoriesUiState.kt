package com.example.coffeeshop.ui.screen.allCategoriesDetail

import androidx.compose.runtime.Stable
import com.example.coffeeshop.data.model.categoryItems.CategoryItems

@Stable
data class AllCategoriesUiState(
    val allCategoriesList: List<CategoryItems> = emptyList(),
    val isLoading: Boolean = false,
)
