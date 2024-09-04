package com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase

import com.example.coffeeshop.data.datasource.sharedpreference.SharedPreferencesRepositoryImpl
import javax.inject.Inject

class SaveSharedPrefUsernameUseCse @Inject constructor(
    private val sharedPreferencesRepositoryImpl: SharedPreferencesRepositoryImpl
) {
    suspend fun saveUsername(username: String?) = sharedPreferencesRepositoryImpl.saveUsername(username)
}