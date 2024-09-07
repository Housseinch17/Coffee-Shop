package com.example.coffeeshop.data.model.category

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable


@Serializable
@Immutable
data class Category(
    val beverages: String = "",
    val coffee: String = "",
    val dessert: String = "",
    val sandwiches: String = "",
    val snacks: String = ""
)