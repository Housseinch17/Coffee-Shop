package com.example.coffeeshop.ui.screen.categoryItemPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryItemViewModel @Inject constructor() : ViewModel() {
    private val _itemsDetailsUiState: MutableStateFlow<CategoryItemUiState> =
        MutableStateFlow(CategoryItemUiState())
    val categoryItemUiState: StateFlow<CategoryItemUiState> = _itemsDetailsUiState.asStateFlow()

    fun setCountValues(count: Int = 1, price: Int) {
        val firstTotal = count * price
        viewModelScope.launch {
            _itemsDetailsUiState.update { newState ->
                newState.copy(
                    count = count,
                    price = price,
                    total = firstTotal
                )
            }
        }
    }


    fun decreaseCount(count: Int) {
        viewModelScope.launch {
            if (count > 1) {
                _itemsDetailsUiState.update { newState ->
                    val newCount = count - 1
                    val price = newState.price
                    val newTotal = newCount * price
                    newState.copy(
                        count = newCount,
                        total = newTotal
                    )
                }
            }
        }
    }

    fun increaseCount(count: Int) {
        viewModelScope.launch {
            _itemsDetailsUiState.update { newState ->
                val newCount = count + 1
                val price = newState.price
                val newTotal = newCount * price
                newState.copy(
                    count = newCount,
                    total = newTotal
                )
            }
        }
    }
}