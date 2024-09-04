package com.example.coffeeshop.data.di

import com.example.coffeeshop.data.datasource.firebase.FirebaseRemoteDataSource
import com.example.coffeeshop.data.datasource.firebase.FirebaseRemoteDataSourceImpl
import com.example.coffeeshop.data.datasource.firebase.FirebaseRepositoryImpl
import com.example.coffeeshop.domain.repository.FirebaseRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideFirebaseRemoteDataSource(
        firebaseAuth: FirebaseAuth,
    ): FirebaseRemoteDataSource {
        return FirebaseRemoteDataSourceImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(firebaseRemoteDataSourceImpl: FirebaseRemoteDataSourceImpl): FirebaseRepository{
        return FirebaseRepositoryImpl(firebaseRemoteDataSourceImpl)
    }
}