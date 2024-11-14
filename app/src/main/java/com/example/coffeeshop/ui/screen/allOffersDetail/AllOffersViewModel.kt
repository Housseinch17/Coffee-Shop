package com.example.coffeeshop.ui.screen.allOffersDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.model.offers.Offers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllOffersViewModel @Inject constructor(): ViewModel() {
    private val _allOffersUiState: MutableStateFlow<AllOffersUiState> = MutableStateFlow(
        AllOffersUiState()
    )
    val allOffersUiState: StateFlow<AllOffersUiState> = _allOffersUiState.asStateFlow()

    init {
        Log.d("ViewModelInitialization", "AllOffers created")
        showLoader()
    }

    private fun showLoader(){
        _allOffersUiState.update { newState->
            newState.copy(isLoading = true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization", "AllOffers destroyed")
    }


    fun setData(offersList: List<Offers>) {
        viewModelScope.launch {
            _allOffersUiState.update { newState ->
                newState.copy(
                    allOffersList = offersList,
                    isLoading = false
                )
            }
        }
    }
}