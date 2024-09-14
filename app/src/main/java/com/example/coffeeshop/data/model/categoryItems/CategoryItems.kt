package com.example.coffeeshop.data.model.categoryItems

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class CategoryItems(
    val title: String = "",
    val description: String = "",
    val extra: String = "",
    val price: Double = 0.0,
    val rating: Double = 0.0,
    val picUrl: String = "",
)