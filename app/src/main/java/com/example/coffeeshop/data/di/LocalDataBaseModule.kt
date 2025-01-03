package com.example.coffeeshop.data.di

import com.example.coffeeshop.data.datasource.localdatabase.LocalDataBaseDataSource
import com.example.coffeeshop.data.datasource.localdatabase.LocalDataBaseDataSourceImpl
import com.example.coffeeshop.data.datasource.localdatabase.RoomDataBaseRepositoryImpl
import com.example.coffeeshop.data.roomdb.ShoppingCartDAO
import com.example.coffeeshop.domain.repository.RoomDataBaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataBaseModule {

    @Singleton
    @Provides
    fun provideLocalDataBaseRepository(
        shoppingCartDAO: ShoppingCartDAO,
        @Named("DispatchersIO") coroutineDispatcher: CoroutineDispatcher
    ): LocalDataBaseDataSource {
        return LocalDataBaseDataSourceImpl(shoppingCartDAO, coroutineDispatcher)
    }

    @Singleton
    @Provides
    fun provideRoomDataBaseRepository(localDataBaseDataSourceImpl: LocalDataBaseDataSourceImpl): RoomDataBaseRepository {
        return RoomDataBaseRepositoryImpl(localDataBaseDataSourceImpl)
    }
}