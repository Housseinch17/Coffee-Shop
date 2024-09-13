package com.example.coffeeshop.data.model.offers

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Offers(
    val title: String = "",
    val price: Int = 0,
    val discount: Int = 0,
    val picUrl: String = "",
)