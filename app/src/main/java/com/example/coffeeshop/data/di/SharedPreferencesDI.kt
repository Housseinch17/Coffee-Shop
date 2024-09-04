package com.example.coffeeshop.data.di

import android.content.Context
import android.content.SharedPreferences
import com.example.coffeeshop.data.datasource.sharedpreference.SharedPreferencesManager
import com.example.coffeeshop.data.datasource.sharedpreference.SharedPreferencesManagerImpl
import com.example.coffeeshop.data.datasource.sharedpreference.SharedPreferencesRepositoryImpl
import com.example.coffeeshop.domain.repository.SharedPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesDI {


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext  context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(sharedPreferences: SharedPreferences): SharedPreferencesManager {
        return SharedPreferencesManagerImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesRepository(sharedPreferencesManagerImpl: SharedPreferencesManagerImpl): SharedPreferencesRepository{
        return SharedPreferencesRepositoryImpl(sharedPreferencesManagerImpl)
    }
}