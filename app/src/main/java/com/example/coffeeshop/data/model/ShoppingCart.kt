package com.example.coffeeshop.data.model

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart
import com.example.coffeeshop.data.model.shoppingCart.OfferCart
import kotlinx.serialization.Serializable

@Entity(tableName = "shoppingCart_items")
@Serializable
@Stable
data class ShoppingCart(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryItemsList: MutableList<CategoryItemsCart> = mutableListOf(),
    val offersList: MutableList<OfferCart> = mutableListOf(),
    val totalPrice: Double = 0.0,
)
