package com.example.coffeeshop.ui.util

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.data.model.offers.Offers
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart
import com.example.coffeeshop.data.model.shoppingCart.OfferCart
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {

    val offers = object : NavType<Offers>(
        isNullableAllowed = false,
    ) {
        override fun get(bundle: Bundle, key: String): Offers? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Offers {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Offers): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Offers) {
            bundle.putString(key,Json.encodeToString(value))
        }
    }

    val categoryItems = object : NavType<CategoryItems>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): CategoryItems? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): CategoryItems {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: CategoryItems): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: CategoryItems) {
            bundle.putString(key,Json.encodeToString(value))
        }
    }

    val allOffers = object : NavType<List<Offers>>(
        isNullableAllowed = false,
    ) {
        override fun get(bundle: Bundle, key: String): List<Offers>? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): List<Offers> {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: List<Offers>): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: List<Offers>) {
            bundle.putString(key,Json.encodeToString(value))
        }
    }

    val allCategories = object : NavType<List<CategoryItems>>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): List<CategoryItems>? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): List<CategoryItems> {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: List<CategoryItems>): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: List<CategoryItems>) {
            bundle.putString(key,Json.encodeToString(value))
        }
    }

    val offerCart = object : NavType<OfferCart>(
        isNullableAllowed = false,
    ) {
        override fun get(bundle: Bundle, key: String): OfferCart? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): OfferCart {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: OfferCart): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: OfferCart) {
            bundle.putString(key,Json.encodeToString(value))
        }
    }

    val categoryItemsCart = object : NavType<CategoryItemsCart>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): CategoryItemsCart? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): CategoryItemsCart {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: CategoryItemsCart): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: CategoryItemsCart) {
            bundle.putString(key,Json.encodeToString(value))
        }
    }

}