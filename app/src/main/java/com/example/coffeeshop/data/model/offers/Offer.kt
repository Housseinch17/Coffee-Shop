package com.example.coffeeshop.data.model.offers

import kotlinx.serialization.Serializable

@Serializable
data class Offer(
    val picUrl: List<String>,
    val price: Int,
    val title: String
)