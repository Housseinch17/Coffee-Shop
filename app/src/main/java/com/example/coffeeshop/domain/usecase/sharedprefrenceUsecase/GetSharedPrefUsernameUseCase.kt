package com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase

import com.example.coffeeshop.data.datasource.sharedpreference.SharedPreferencesRepositoryImpl
import javax.inject.Inject

class GetSharedPrefUsernameUseCase @Inject constructor(
    private val sharedPreferencesRepositoryImpl: SharedPreferencesRepositoryImpl
) {
    suspend fun getUsername(): String? = sharedPreferencesRepositoryImpl.getUsername()
}