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
) : ViewModel() {
    private val _myOrdersUiState: MutableStateFlow<MyOrdersUiState> =
        MutableStateFlow(MyOrdersUiState())
    val myOrdersUiState: StateFlow<MyOrdersUiState> = _myOrdersUiState.asStateFlow()


    init {
        Log.d("ViewModelInitialization", "MyOrders")
        getAllOrders()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization", "myorders destroyed")
    }

    fun setExpanded(index: Int) {
        viewModelScope.launch {
            viewModelScope.launch {
                val currentOrdersList = _myOrdersUiState.value.myOrdersUiState.toList()
                val updatedOrdersList = currentOrdersList.mapIndexed { i, order ->
                    if (i == index) {
                        order.copy(isExpanded = !order.isExpanded)
                    } else {
                        order
                    }
                }
                _myOrdersUiState.update { newState ->
                    newState.copy(myOrdersUiState = updatedOrdersList)
                }
            }
        }
    }


private fun getAllOrders() {
    viewModelScope.launch {
        _myOrdersUiState.update { newState->
            newState.copy(orderState = OrderStatus.IsLoading)
        }
        val getAllOrders = getAllShoppingCartItemsUseCase.getAllShoppingCartItems()
        _myOrdersUiState.update { newState ->
            newState.copy(
                myOrdersUiState = getAllOrders.map { shoppingCart ->
                    MyOrders(shoppingCart = shoppingCart, isExpanded = false)
                },
                orderState = OrderStatus.AlreadyLoaded
            )
        }
    }
}

fun loadNewOrders() {
    viewModelScope.launch {
        _myOrdersUiState.update { newState->
            newState.copy(isRefreshing = true)
        }
        resetAllOrders()
        getAllOrders()
        _myOrdersUiState.update { newState->
            newState.copy(isRefreshing = false)
        }
    }
}

private fun resetAllOrders() {
    viewModelScope.launch {
        _myOrdersUiState.value = MyOrdersUiState()
    }
}
}

sealed interface OrderStatus{
    data object IsLoading: OrderStatus
    data object AlreadyLoaded: OrderStatus

}