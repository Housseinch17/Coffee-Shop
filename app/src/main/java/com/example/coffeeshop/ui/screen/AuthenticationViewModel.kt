package com.example.coffeeshop.ui.screen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase.SignOutUseCase
import com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase.GetSharedPrefUsernameUseCase
import com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase.SaveSharedPrefUsernameUseCase
import com.example.coffeeshop.ui.util.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    application: Application, // Use Application directly
    private val signOutUseCase: SignOutUseCase,
    private val saveSharedPrefUsernameUseCase: SaveSharedPrefUsernameUseCase,
    private val getSharedPrefUsernameUseCase: GetSharedPrefUsernameUseCase,
) : AndroidViewModel(application = application) {
    private val _authenticationUiState: MutableStateFlow<AuthenticationUiState> =
        MutableStateFlow(AuthenticationUiState())
    val authenticationUiState: StateFlow<AuthenticationUiState> =
        _authenticationUiState.asStateFlow()

    init {
        getCurrentUserName()
    }

    private val _showError = MutableSharedFlow<String>()
    val showError = _showError.asSharedFlow()


    private fun emitError(error: String = "No internet connection") {
        viewModelScope.launch {
            _showError.emit(error)
        }
    }

    fun getCurrentUserName() {
        viewModelScope.launch {
            val currentUsername = getSharedPrefUsernameUseCase.getUsername()
            _authenticationUiState.update { newState ->
                newState.copy(username = currentUsername)
            }
        }
    }

    //reset state while signing out
    fun resetSignOutState() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(signOut = SignOutResponse.Loading)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _authenticationUiState.update { newState ->
                newState.copy(signOut = SignOutResponse.Loading)
            }
            val hasInternet = getApplication<Application>().isInternetAvailable()
            Log.d("hasInternet", "$hasInternet")
            if (hasInternet) {
                signOutUseCase.signOut()
                _authenticationUiState.update { newState ->
                    newState.copy(signOut = SignOutResponse.Success)
                }
                saveSharedPrefUsernameUseCase.saveUsername(null)
            } else {
                _authenticationUiState.update { newState ->
                    newState.copy(signOut = SignOutResponse.Error)
                }
                //show error to the user
                emitError()
            }
        }
    }
}

sealed interface SignOutResponse {
    data object Loading : SignOutResponse
    data object Success : SignOutResponse
    data object Error : SignOutResponse

}