package com.example.coffeeshop.data.model

import com.example.coffeeshop.data.model.category.Category
import com.example.coffeeshop.data.model.items.Items
import com.example.coffeeshop.data.model.offers.Offer
import kotlinx.serialization.Serializable

@Serializable
data class FirebaseModel(
    val category: Category,
    val items: Items,
    val offers: List<Offer>
)