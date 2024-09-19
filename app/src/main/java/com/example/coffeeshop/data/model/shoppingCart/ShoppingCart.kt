package com.example.coffeeshop.data.model.shoppingCart

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class ShoppingCart(
    val categoryItemsList: List<CategoryItemsCart> = emptyList(),
    val offerList: List<OfferCart> = emptyList(),
)


