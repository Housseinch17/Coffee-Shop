package com.example.coffeeshop.ui.screen.myorderspage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.domain.usecase.localDataBaseUseCase.GetAllShoppingCartItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val getAllShoppingCartItemsUseCase: GetAllShoppingCartItemsUseCase
): ViewModel(){
    private val _myOrdersUiState: MutableStateFlow<MyOrdersUiState> = MutableStateFlow(MyOrdersUiState())
    val myOrdersUiState: StateFlow<MyOrdersUiState> = _myOrdersUiState.asStateFlow()


    init {
        Log.d("ViewModelInitialization","MyOrders")
        getAllOrders()
    }

    private fun getAllOrders(){
        viewModelScope.launch {
          val getAllOrders = getAllShoppingCartItemsUseCase.getAllShoppingCartItems()
        _myOrdersUiState.update { newState->
            newState.copy(
                myOrdersUiState = getAllOrders
            )
        }
    }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization","myorders destroyed")
    }
}