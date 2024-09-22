package com.example.coffeeshop.domain.repository

import com.example.coffeeshop.data.model.ShoppingCart

interface RoomDataBaseRepository {
    suspend fun saveShoppingCartItems(shoppingCart: ShoppingCart)
    suspend fun getAllShoppingCartItems(): List<ShoppingCart>
}