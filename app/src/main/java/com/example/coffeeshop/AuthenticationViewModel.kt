package com.example.coffeeshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.domain.usecase.firebaseUsecase.CheckStatusUseCase
import com.example.coffeeshop.domain.usecase.firebaseUsecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val checkStatusUseCase: CheckStatusUseCase,
    private val signOutUseCase: SignOutUseCase,
): ViewModel() {
    private val _status: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    val status: StateFlow<AuthState> = _status.asStateFlow()

    init {
        checkStatus()
    }

    private fun checkStatus(){
        viewModelScope.launch {
            _status.value = checkStatusUseCase.checkStatus()
        }
    }
    fun signOut(){
        viewModelScope.launch {
            _status.value = signOutUseCase.signOut()
        }
    }
}

sealed interface AuthState{
    data object Authenticated: AuthState
    data object UnAuthenticated: AuthState
    data object Loading: AuthState
    data class Error(val message: String): AuthState
}