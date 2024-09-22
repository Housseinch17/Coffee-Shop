package com.example.coffeeshop.data.di

import android.app.Application
import androidx.room.Room
import com.example.coffeeshop.data.roomdb.ShoppingCartDAO
import com.example.coffeeshop.data.roomdb.ShoppingCartDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideShoppingCartDataBase(app: Application): ShoppingCartDataBase{
        return Room.databaseBuilder(app,ShoppingCartDataBase::class.java,"ShoppingCartApp")
            .build()
    }

    @Singleton
    @Provides
    fun provideShoppingCartDAO(shoppingCartDataBase: ShoppingCartDataBase): ShoppingCartDAO{
        return shoppingCartDataBase.shoppingCartDao()
    }

}
