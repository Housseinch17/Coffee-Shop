package com.example.coffeeshop.ui.screen.mainpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase.GetSharedPrefUsernameUseCase
import com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase.SaveSharedPrefUsernameUseCase
import com.example.coffeeshop.ui.navigation.CurrentDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveSharedPrefUsernameUseCase: SaveSharedPrefUsernameUseCase,
    private val getSharedPrefUsernameUseCase: GetSharedPrefUsernameUseCase,
) : ViewModel() {
    private val _status: MutableStateFlow<CurrentDestination> =
        MutableStateFlow(CurrentDestination.Loading)
    val status: StateFlow<CurrentDestination> = _status.asStateFlow()

    init {
        checkSharedPreferUsername()
    }

    private suspend fun getCurrentUserName(): String? {
        return getSharedPrefUsernameUseCase.getUsername()
    }

    private fun checkSharedPreferUsername() {
        viewModelScope.launch {
            val currentUsername = getCurrentUserName()
            if (currentUsername == null) {
                _status.value = CurrentDestination.LogInPage
            } else {
                _status.value = CurrentDestination.HomePage
                saveSharedPrefUsernameUseCase.saveUsername(currentUsername)
            }
        }
    }
}