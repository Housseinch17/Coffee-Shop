package com.example.coffeeshop.domain.usecase.localDataBaseUseCase

import com.example.coffeeshop.data.datasource.localdatabase.RoomDataBaseRepositoryImpl
import com.example.coffeeshop.data.model.ShoppingCart
import javax.inject.Inject

class SaveShoppingCartItemsUseCase @Inject constructor(
    private val roomDataBaseRepositoryImpl: RoomDataBaseRepositoryImpl
) {
    suspend fun saveShoppingCartItems(shoppingCart: ShoppingCart) {
        roomDataBaseRepositoryImpl.saveShoppingCartItems(shoppingCart)
    }
}