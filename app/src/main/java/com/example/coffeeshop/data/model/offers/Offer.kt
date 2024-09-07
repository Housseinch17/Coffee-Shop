package com.example.coffeeshop.data.model.offers

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class Offer(
    val picUrl: String,
    val price: Int,
    val title: String
)