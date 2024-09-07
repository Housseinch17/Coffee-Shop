package com.example.coffeeshop.data.model.items

import androidx.compose.runtime.Stable
import com.example.coffeeshop.data.model.items.itemslist.coffee.Coffee
import kotlinx.serialization.Serializable

@Serializable
@Stable //stable (list is mutable object)
data class Items(
    val coffee: List<Coffee>
)