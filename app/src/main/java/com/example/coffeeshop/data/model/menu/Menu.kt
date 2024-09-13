package com.example.coffeeshop.data.model.menu

import androidx.compose.runtime.Stable
import com.example.coffeeshop.data.model.category.Category
import com.example.coffeeshop.data.model.offers.Offers
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class Menu(
    val category: Category = Category(),
    val offers: List<Offers> = emptyList()
)