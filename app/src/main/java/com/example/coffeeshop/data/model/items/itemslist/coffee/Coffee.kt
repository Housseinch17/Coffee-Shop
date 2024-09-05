package com.example.coffeeshop.data.model.items.itemslist.coffee

import kotlinx.serialization.Serializable

@Serializable
data class Coffee(
    val description: String,
    val extra: String,
    val picUrl: String,
    val price: Int,
    val rating: Double,
    val title: String
)