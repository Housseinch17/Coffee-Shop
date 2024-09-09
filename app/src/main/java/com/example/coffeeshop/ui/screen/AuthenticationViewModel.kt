package com.example.coffeeshop.ui.screen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.MyApp
import com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase.SignOutUseCase
import com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase.GetSharedPrefUsernameUseCase
import com.example.coffeeshop.domain.usecase.sharedprefrenceUsecase.SaveSharedPrefUsernameUseCase
import com.example.coffeeshop.ui.util.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val application: Application, // Use Application directly
    private val signOutUseCase: SignOutUseCase,
    private val saveSharedPrefUsernameUseCase: SaveSharedPrefUsernameUseCase,
    private val getSharedPrefUsernameUseCase: GetSharedPrefUsernameUseCase,
) : AndroidViewModel(application = application) {
    private val _signOut: MutableStateFlow<SignOut> = MutableStateFlow(SignOut.Loading)
    val signOut: StateFlow<SignOut> = _signOut.asStateFlow()

    private val _showError = MutableSharedFlow<String>()
    val showError = _showError.asSharedFlow()


    private fun emitError(error: String) {
        viewModelScope.launch {
            _showError.emit(error)
        }
    }

    private suspend fun getCurrentUserName(): String? {
        return getSharedPrefUsernameUseCase.getUsername()
    }

    fun signOut() {
        viewModelScope.launch {
            val hasInternet = getApplication<Application>().isInternetAvailable()
            Log.d("hasInternet", "$hasInternet")
            if (hasInternet) {
                signOutUseCase.signOut()
                _signOut.value = SignOut.Success
                saveSharedPrefUsernameUseCase.saveUsername(null)
            } else {
                _signOut.value = SignOut.Error
                emitError("No internet connection")
            }
        }
    }
}

sealed interface SignOut {
    data object Loading : SignOut
    data object Success : SignOut
    data object Error : SignOut

}