package com.example.coffeeshop.data.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coffeeshop.data.model.ShoppingCart

@Dao
interface ShoppingCartDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveShoppingCartItems(shoppingCart: ShoppingCart)

    @Query("SELECT * FROM shoppingCart_items")
    suspend fun getAllShoppingCartItems(): List<ShoppingCart>
}