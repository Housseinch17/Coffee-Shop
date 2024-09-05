package com.example.coffeeshop.data.model.items

import com.example.coffeeshop.data.model.items.itemslist.coffee.Coffee
import kotlinx.serialization.Serializable

@Serializable
data class Items(
    val coffee: List<Coffee>
)