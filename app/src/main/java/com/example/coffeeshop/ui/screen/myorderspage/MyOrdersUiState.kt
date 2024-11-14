package com.example.coffeeshop.ui.screen.myorderspage

import androidx.compose.runtime.Stable
import com.example.coffeeshop.data.model.ShoppingCart

@Stable
data class MyOrdersUiState(
    val myOrdersList: List<MyOrders> = emptyList(),
    val orderState: OrderStatus = OrderStatus.IsLoading,
    val isRefreshing: Boolean = false,
)

@Stable
data class MyOrders(val shoppingCart: ShoppingCart = ShoppingCart(), var isExpanded: Boolean = false)

