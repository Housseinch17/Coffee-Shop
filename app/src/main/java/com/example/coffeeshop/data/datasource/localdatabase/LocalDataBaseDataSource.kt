package com.example.coffeeshop.data.datasource.localdatabase

import com.example.coffeeshop.data.model.ShoppingCart

interface LocalDataBaseDataSource {
    suspend fun saveShoppingCartItems(shoppingCart: ShoppingCart)
    suspend fun getAllShoppingCartItems(): List<ShoppingCart>
}