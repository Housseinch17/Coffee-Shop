package com.example.coffeeshop.ui.screen.offerItemPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.model.offers.Offers
import com.example.coffeeshop.ui.util.DataSource.formatTotal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferItemViewModel @Inject constructor(): ViewModel() {
    private val _offerItemUiState: MutableStateFlow<OfferItemUiState> = MutableStateFlow(OfferItemUiState())
    val offerItemUiState: StateFlow<OfferItemUiState> = _offerItemUiState.asStateFlow()


    init {
        Log.d("ViewModelInitialization","offer")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization","offer destroyed")
    }

    fun setOffer(offers: Offers) {
        viewModelScope.launch {
            _offerItemUiState.update { newState ->
                newState.copy(
                    offerCart = newState.offerCart.copy(
                        offers = offers
                    )
                )
            }
        }
    }

    fun setCountAndTotalFirstValues(count: Int = 1, price: Double) {
        val firstTotal = count * price
        viewModelScope.launch {
            _offerItemUiState.update { newState ->
                newState.copy(
                    offerCart = newState.offerCart.copy(
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
                _offerItemUiState.update { newState ->
                    val newCount = count - 1
                    val price = newState.offerCart.offers.price
                    val newTotal = newCount * price
                    newState.copy(
                        offerCart = newState.offerCart.copy(
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
                _offerItemUiState.update { newState ->
                    val newCount = count + 1
                    val price = newState.offerCart.offers.price
                    val newTotal = newCount * price
                    newState.copy(
                        offerCart = newState.offerCart.copy(
                            count = newCount,
                            totalPrice = newTotal
                        )
                    )
                }
            }
        }
    }
}