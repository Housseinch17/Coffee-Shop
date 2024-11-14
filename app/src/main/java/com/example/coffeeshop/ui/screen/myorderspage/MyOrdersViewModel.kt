package com.example.coffeeshop.ui.screen.myorderspage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.domain.usecase.localDataBaseUseCase.GetAllShoppingCartItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
        Log.d("ViewModelInitialization", "MyOrders created")
        viewModelScope.launch {
            getAllOrders()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization", "MyOrders destroyed")
    }

    fun setExpanded(index: Int) {
        viewModelScope.launch {
            viewModelScope.launch {
                val currentOrdersList = _myOrdersUiState.value.myOrdersList.toList()
                val updatedOrdersList = currentOrdersList.mapIndexed { i, order ->
                    if (i == index) {
                        order.copy(isExpanded = !order.isExpanded)
                    } else {
                        order
                    }
                }
                _myOrdersUiState.update { newState ->
                    newState.copy(myOrdersList = updatedOrdersList)
                }
            }
        }
    }


    private suspend fun getAllOrders() {
        _myOrdersUiState.update { newState ->
            newState.copy(orderState = OrderStatus.IsLoading)
        }
        val getAllOrders = getAllShoppingCartItemsUseCase.getAllShoppingCartItems()
        _myOrdersUiState.update { newState ->
            newState.copy(
                myOrdersList = getAllOrders.map { shoppingCart ->
                    MyOrders(shoppingCart = shoppingCart, isExpanded = false)
                },
                orderState = OrderStatus.AlreadyLoaded
            )
        }
        Log.d("MyTag","getAllOrders() finished")
    }

    fun loadNewOrders() {
        viewModelScope.launch {
            _myOrdersUiState.update { newState ->
                newState.copy(isRefreshing = true)
            }
            Log.d("MyTag","refreshing true")
            resetAllOrders()
            delay(1000)
            getAllOrders()
            _myOrdersUiState.update { newState ->
                newState.copy(isRefreshing = false)
            }
            Log.d("MyTag","loadNewOrders() finished")
        }
    }

    //here isRefreshing is set true because in loadNewOrders() its showing No orders yet in MyOrdersPage
    //because of the condition which set list empty and refreshing false
    private fun resetAllOrders() {
        viewModelScope.launch {
            _myOrdersUiState.value = MyOrdersUiState().copy(isRefreshing = true)
        }
        Log.d("MyTag","resetAllOrders() finished")
    }
}

sealed interface OrderStatus {
    data object IsLoading : OrderStatus
    data object AlreadyLoaded : OrderStatus

}