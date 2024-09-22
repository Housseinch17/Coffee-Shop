package com.example.coffeeshop.data.datasource.localdatabase

import com.example.coffeeshop.data.model.ShoppingCart
import com.example.coffeeshop.data.roomdb.ShoppingCartDAO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class LocalDataBaseDataSourceImpl @Inject constructor(
    private val shoppingCartDAO: ShoppingCartDAO,
    @Named("DispatchersIO") private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : LocalDataBaseDataSource {
    override suspend fun saveShoppingCartItems(shoppingCart: ShoppingCart) =
        withContext(coroutineDispatcher) {
            shoppingCartDAO.saveShoppingCartItems(shoppingCart)
        }

    override suspend fun getAllShoppingCartItems(): List<ShoppingCart> = withContext(coroutineDispatcher) {
       val shoppingCartList = try {
            shoppingCartDAO.getAllShoppingCartItems()
        } catch (e: Exception) {
            emptyList()
        }
            return@withContext shoppingCartList
        }
}