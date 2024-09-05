package com.example.coffeeshop.data.model.category

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val beverages: String,
    val coffee: String,
    val dessert: String,
    val sandwiches: String,
    val snacks: String
)