package com.example.coffeeshop.ui.screen.myorderspage

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.coffeeshop.data.model.ShoppingCart

@Composable
fun MyOrdersPage(modifier: Modifier,
                 myOrders: List<ShoppingCart>){
    Log.d("MyTag",myOrders.toString())
}