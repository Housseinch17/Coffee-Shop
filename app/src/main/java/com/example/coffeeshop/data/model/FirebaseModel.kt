package com.example.coffeeshop.data.model

import androidx.compose.runtime.Stable
import com.example.coffeeshop.data.model.category.Category
import com.example.coffeeshop.data.model.items.Items
import com.example.coffeeshop.data.model.offers.Offer
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class FirebaseModel(
    val category: Category,
    val items: Items,
    val offers: List<Offer>
)