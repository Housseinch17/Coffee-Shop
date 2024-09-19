package com.example.coffeeshop.data.model.shoppingCart

import androidx.compose.runtime.Immutable
import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class CategoryItemsCart(
    val categoryItems: CategoryItems = CategoryItems(),
    val count: Int = 0,
    val totalPrice: Double = 0.0,
)
