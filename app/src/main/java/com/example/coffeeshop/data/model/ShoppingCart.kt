package com.example.coffeeshop.data.model

import androidx.compose.runtime.Stable
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart
import com.example.coffeeshop.data.model.shoppingCart.OfferCart
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class ShoppingCart(
    val categoryItemsList: MutableList<CategoryItemsCart> = mutableListOf(),
    val offersList: MutableList<OfferCart> = mutableListOf(),
    val totalPrice: Double = 0.0,
)
