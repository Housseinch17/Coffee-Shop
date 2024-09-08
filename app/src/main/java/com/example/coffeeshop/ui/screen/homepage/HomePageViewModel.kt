package com.example.coffeeshop.ui.screen.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.model.menu.Menu
import com.example.coffeeshop.domain.usecase.firebaseReadAndWriteUsecase.ReadDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val readDataUseCase: ReadDataUseCase,
) : ViewModel() {
    private val _homepageUiState: MutableStateFlow<HomePageUiState> = MutableStateFlow(HomePageUiState())
    val homePageUiState: StateFlow<HomePageUiState> = _homepageUiState.asStateFlow()

    init {
        readData()
    }

    private fun readData(){
        viewModelScope.launch {
            val response = readDataUseCase.readData()
            _homepageUiState.update { newState->
                newState.copy(response = response)
            }
        }
    }

}

sealed interface FirebaseResponse {
    data class Success(val menu: Menu) : FirebaseResponse
    data class Error(val message: String) : FirebaseResponse
    data object Loading : FirebaseResponse
}