package com.example.coffeeshop.data.model.items.itemslist.coffee

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Coffee(
    val description: String,
    val extra: String,
    val picUrl: String,
    val price: Int,
    val rating: Double,
    val title: String
)