package com.example.coffeeshop.ui.screen.allCategoriesDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllCategoriesViewModel @Inject constructor() : ViewModel() {
    private val _allCategoriesUiState: MutableStateFlow<AllCategoriesUiState> = MutableStateFlow(
        AllCategoriesUiState()
    )
    val allCategoriesUiState: StateFlow<AllCategoriesUiState> = _allCategoriesUiState.asStateFlow()

    init {
        Log.d("ViewModelInitialization", "AllCategories created")
        showLoader()
    }

    private fun showLoader(){
        _allCategoriesUiState.update { newState->
            newState.copy(isLoading = true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization", "AllCategories destroyed")
    }


    fun setData(categoriesList: List<CategoryItems>) {
        viewModelScope.launch {
            _allCategoriesUiState.update { newState ->
                newState.copy(
                    allCategoriesList = categoriesList,
                    isLoading = false
                )
            }
        }
    }
}