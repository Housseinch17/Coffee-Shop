package com.example.coffeeshop.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.coffeeshop.data.model.ShoppingCart
import com.example.coffeeshop.ui.util.Converters

@Database(
    entities = [ShoppingCart::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ShoppingCartDataBase: RoomDatabase() {
    abstract fun shoppingCartDao(): ShoppingCartDAO
}