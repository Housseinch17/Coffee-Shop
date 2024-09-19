package com.example.coffeeshop.data.model.shoppingCart

import androidx.compose.runtime.Immutable
import com.example.coffeeshop.data.model.offers.Offers
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class OfferCart(
    val offers: Offers = Offers(),
    val count: Int = 0,
    val totalPrice: Double =0.0
)
