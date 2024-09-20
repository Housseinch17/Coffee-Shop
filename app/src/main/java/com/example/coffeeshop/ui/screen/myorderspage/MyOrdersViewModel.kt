package com.example.coffeeshop.ui.screen.myorderspage

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(): ViewModel(){

    init {
        Log.d("ViewModelInitialization","MyOrders")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization","myorders destroyed")
    }
}