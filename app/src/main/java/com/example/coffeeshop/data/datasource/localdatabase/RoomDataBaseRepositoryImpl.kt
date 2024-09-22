package com.example.coffeeshop.data.datasource.localdatabase

import com.example.coffeeshop.data.model.ShoppingCart
import com.example.coffeeshop.domain.repository.RoomDataBaseRepository
import javax.inject.Inject

class RoomDataBaseRepositoryImpl @Inject constructor(
    private val localDataBaseDataSourceImpl: LocalDataBaseDataSourceImpl
) : RoomDataBaseRepository {
    override suspend fun saveShoppingCartItems(shoppingCart: ShoppingCart) {
        return localDataBaseDataSourceImpl.saveShoppingCartItems(shoppingCart)
    }

    override suspend fun getAllShoppingCartItems(): List<ShoppingCart> {
        return localDataBaseDataSourceImpl.getAllShoppingCartItems()
    }
}