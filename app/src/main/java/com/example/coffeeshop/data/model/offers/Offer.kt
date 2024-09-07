package com.example.coffeeshop.data.model.offers

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Offer(
    val picUrl: String = "",
    val price: Int = 0,
    val title: String = ""
)