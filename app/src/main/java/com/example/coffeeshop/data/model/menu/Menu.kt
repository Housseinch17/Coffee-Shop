package com.example.coffeeshop.data.model.menu

import androidx.compose.runtime.Immutable
import com.example.coffeeshop.data.model.offers.Offer
import com.example.coffeeshop.data.model.category.Category
import com.example.coffeeshop.data.model.items.Items
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Menu(
    val category: Category = Category(),
    val items: Items = Items(),
    val offers: List<Offer> = emptyList()
)