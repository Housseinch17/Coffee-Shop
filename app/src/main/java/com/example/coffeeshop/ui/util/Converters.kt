package com.example.coffeeshop.ui.util

import androidx.room.TypeConverter
import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.data.model.offers.Offers
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart
import com.example.coffeeshop.data.model.shoppingCart.OfferCart
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromCategoryItems(value: CategoryItems?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCategoryItems(value: String): CategoryItems {
        return gson.fromJson(value, CategoryItems::class.java)
    }

    @TypeConverter
    fun fromOffers(value: Offers?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toOffers(value: String): Offers {
        return gson.fromJson(value, Offers::class.java)
    }

    @TypeConverter
    fun fromCategoryItemsList(value: MutableList<CategoryItemsCart>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCategoryItemsList(value: String): MutableList<CategoryItemsCart> {
        val listType = object : TypeToken<MutableList<CategoryItemsCart>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromOfferCartList(value: MutableList<OfferCart>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toOfferCartList(value: String): MutableList<OfferCart> {
        val listType = object : TypeToken<MutableList<OfferCart>>() {}.type
        return gson.fromJson(value, listType)
    }
}