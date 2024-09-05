package com.example.coffeeshop

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.domain.usecase.firebaseUsecase.SignOutUseCase
import com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase.GetSharedPrefUsernameUseCase
import com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase.SaveSharedPrefUsernameUseCse
import com.example.coffeeshop.ui.AuthenticationUiState
import com.example.coffeeshop.ui.navigation.CurrentDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val saveSharedPrefUsernameUseCse: SaveSharedPrefUsernameUseCse,
    private val getSharedPrefUsernameUseCase: GetSharedPrefUsernameUseCase,

    ) : ViewModel() {
    private val _authenticationUiState: MutableStateFlow<AuthenticationUiState> =
        MutableStateFlow(AuthenticationUiState())
    val authenticationUiState: StateFlow<AuthenticationUiState> = _authenticationUiState.asStateFlow()

    init {
        checkSharedPreferUsername()
    }

    fun updateDestination(currentDestination: CurrentDestination){
        _authenticationUiState.update { newState->
            newState.copy(startDestination = currentDestination)
        }
    }

    private suspend fun getCurrentUserName(): String?{
        return getSharedPrefUsernameUseCase.getUsername()
    }

    private fun checkSharedPreferUsername() {
        viewModelScope.launch {
            val currentUsername = getCurrentUserName()
            if (currentUsername == null) {
                _authenticationUiState.update { newState->
                    newState.copy(authenticationStatus = AuthenticationStatus.UnAuthenticated)
                }
            } else {
                Log.d("aff","EnteredHere")
                _authenticationUiState.update { newState->
                    newState.copy(authenticationStatus = AuthenticationStatus.Authenticated)
                }
                saveSharedPrefUsernameUseCse.saveUsername(currentUsername)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _authenticationUiState.update { newState->
                newState.copy(authenticationStatus = signOutUseCase.signOut())
            }
            saveSharedPrefUsernameUseCse.saveUsername(null)
        }
    }
}

sealed interface AuthenticationStatus {
    data object Authenticated : AuthenticationStatus
    data object UnAuthenticated : AuthenticationStatus
}