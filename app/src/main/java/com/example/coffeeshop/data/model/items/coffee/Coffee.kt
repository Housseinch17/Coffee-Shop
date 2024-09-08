package com.example.coffeeshop.data.model.items.coffee

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Coffee(
    val description: String = "",
    val extra: String = "",
    val picUrl: String = "",
    val price: Int = 0,
    val rating: Double = 0.0,
    val title: String = ""
)