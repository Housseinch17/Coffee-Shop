package com.example.coffeeshop.ui.screen.categoryItemPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.ui.util.DataSource.formatTotal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryItemViewModel @Inject constructor() : ViewModel() {
    private val _categoryItemUiState: MutableStateFlow<CategoryItemUiState> =
        MutableStateFlow(CategoryItemUiState())
    val categoryItemUiState: StateFlow<CategoryItemUiState> = _categoryItemUiState.asStateFlow()

    init {
        Log.d("ViewModelInitialization","Category created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization","category destroyed")
    }

    fun setCategoryItems(categoryItems: CategoryItems) {
        viewModelScope.launch {
            _categoryItemUiState.update { newState ->
                newState.copy(
                    categoryItemsCart = newState.categoryItemsCart.copy(
                        categoryItems = categoryItems
                    )
                )
            }
        }
    }

    fun setCountAndTotalFirstValues(count: Int = 1, price: Double) {
        val firstTotal: Double = (count * price)
        viewModelScope.launch {
            _categoryItemUiState.update { newState ->
                newState.copy(
                    categoryItemsCart = newState.categoryItemsCart.copy(
                        count = count,
                        totalPrice = formatTotal(firstTotal)
                    )
                )
            }
        }
    }


    fun decreaseCount(count: Int) {
        viewModelScope.launch {
            if (count > 1) {
                _categoryItemUiState.update { newState ->
                    val newCount = count - 1
                    val price = newState.categoryItemsCart.categoryItems.price
                    val newTotal = newCount * price
                    newState.copy(
                        categoryItemsCart = newState.categoryItemsCart.copy(
                            count = newCount,
                            totalPrice = formatTotal(newTotal)
                        ),
                    )
                }
            }
        }
    }

    fun increaseCount(count: Int) {
        viewModelScope.launch {
            if (count < 50) {
                _categoryItemUiState.update { newState ->
                    val newCount = count + 1
                    val price = newState.categoryItemsCart.categoryItems.price
                    val newTotal = newCount * price
                    newState.copy(
                        categoryItemsCart = newState.categoryItemsCart.copy(
                            count = newCount,
                            totalPrice = formatTotal(newTotal)
                        )
                    )
                }
            }
        }
    }
}