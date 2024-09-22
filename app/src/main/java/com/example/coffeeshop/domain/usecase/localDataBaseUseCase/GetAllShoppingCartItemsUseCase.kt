package com.example.coffeeshop.domain.usecase.localDataBaseUseCase

import com.example.coffeeshop.data.datasource.localdatabase.RoomDataBaseRepositoryImpl
import com.example.coffeeshop.data.model.ShoppingCart
import javax.inject.Inject

class GetAllShoppingCartItemsUseCase @Inject constructor(
    private val roomDataBaseRepositoryImpl: RoomDataBaseRepositoryImpl
) {
    suspend fun getAllShoppingCartItems(): List<ShoppingCart>{
        return roomDataBaseRepositoryImpl.getAllShoppingCartItems()
    }
}