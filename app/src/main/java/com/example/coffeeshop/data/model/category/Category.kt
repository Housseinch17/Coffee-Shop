package com.example.coffeeshop.data.model.category

import androidx.compose.runtime.Stable
import com.example.coffeeshop.data.model.items.beverages.Beverage
import com.example.coffeeshop.data.model.items.coffee.Coffee
import com.example.coffeeshop.data.model.items.dessert.Dessert
import com.example.coffeeshop.data.model.items.sandwiches.Sandwiche
import com.example.coffeeshop.data.model.items.snacks.Snack
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class Category(
    val beverages: List<Beverage> = emptyList(),
    val coffee: List<Coffee> = emptyList(),
    val dessert: List<Dessert> = emptyList(),
    val sandwiches: List<Sandwiche> = emptyList(),
    val snacks: List<Snack> = emptyList()
)