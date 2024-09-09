package com.example.coffeeshop.data.model.category

import androidx.compose.runtime.Stable
import com.example.coffeeshop.data.model.items.CategoryItems
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class Category(
    val beverages: List<CategoryItems> = emptyList(),
    val coffee: List<CategoryItems> = emptyList(),
    val dessert: List<CategoryItems> = emptyList(),
    val sandwiches: List<CategoryItems> = emptyList(),
    val snacks: List<CategoryItems> = emptyList()
){
    fun toHashMap(): HashMap<String, List<CategoryItems>> {
        val map = HashMap<String, List<CategoryItems>>()
        map["beverages"] = beverages
        map["coffee"] = coffee
        map["dessert"] = dessert
        map["sandwiches"] = sandwiches
        map["snacks"] = snacks
        return map
    }
}