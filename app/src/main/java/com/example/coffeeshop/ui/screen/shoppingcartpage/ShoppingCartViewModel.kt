package com.example.coffeeshop.ui.screen.shoppingcartpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.model.ShoppingCart
import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.data.model.offers.Offers
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart
import com.example.coffeeshop.data.model.shoppingCart.OfferCart
import com.example.coffeeshop.domain.usecase.localDataBaseUseCase.SaveShoppingCartItemsUseCase
import com.example.coffeeshop.ui.util.DataSource.formatTotal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val saveShoppingCartItemsUseCase: SaveShoppingCartItemsUseCase
) : ViewModel() {
    private val _shoppingCartUiState: MutableStateFlow<ShoppingCartUiState> =
        MutableStateFlow(ShoppingCartUiState())
    val shoppingCartUiState: StateFlow<ShoppingCartUiState> = _shoppingCartUiState.asStateFlow()

    private val _shoppingCartSharedFlow: MutableSharedFlow<String> = MutableSharedFlow()
    val shoppingCartSharedFlow: SharedFlow<String> = _shoppingCartSharedFlow.asSharedFlow()

    init {
        Log.d("ViewModelInitialization", "Shopping created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization", "shopping destroyed")
    }

    private fun emitMessage(message: String = "Your order successfully completed!"){
        viewModelScope.launch {
            _shoppingCartSharedFlow.emit(message)
        }
    }

    //check if the category item sent is already in the shopping cart list
    private fun isCategoryItemsInList(
        categoryItemsList: List<CategoryItemsCart>,
        categoryItems: CategoryItems
    ): Boolean {
        return categoryItemsList.any { it.categoryItems == categoryItems }
    }

    //check if the offer item sent is already in the shopping cart list
    private fun isOfferCartInList(
        offerCartList: List<OfferCart>,
        offers: Offers
    ): Boolean {
        return offerCartList.any { it.offers == offers }
    }

    suspend fun saveShoppingCartItems(shoppingCart: ShoppingCart) {
        saveShoppingCartItemsUseCase.saveShoppingCartItems(shoppingCart)
        Log.d("MyTag", "successfully saved!")
        resetState()
        Log.d("MyTag", "reset")
        emitMessage()
    }

    private fun resetState() {
        viewModelScope.launch {
            _shoppingCartUiState.update { newState ->
                newState.copy(
                    shoppingCart = ShoppingCart()
                )
            }
        }
    }

    fun updateShoppingCartLists(categoryItemsCart: CategoryItemsCart, offerCart: OfferCart) {
        viewModelScope.launch {
            val shoppingCartUiState = _shoppingCartUiState.value.shoppingCart

            //check if category item is empty
            val addCategoryItemsCart: Boolean = categoryItemsCart != CategoryItemsCart()
            //check if offer item is empty
            val addOfferCart: Boolean = offerCart != OfferCart()

            // Modify the categoryItemsCartList
            val categoryItemsList = shoppingCartUiState.categoryItemsList.toMutableList()
            //check categoryItemsCart if exists in the list
            val categoryItemsCartExist = isCategoryItemsInList(categoryItemsList, categoryItemsCart.categoryItems)

            //Modify the existing offerCartList
            val offersList = shoppingCartUiState.offersList.toMutableList()
            //check offerCart if exists exist in the list
            val offerCartExists = isOfferCartInList(offersList, offerCart.offers)

            // Add the new category item cart if not already exist and if not empty
            if (addCategoryItemsCart && !categoryItemsCartExist) {
                categoryItemsList.add(categoryItemsCart)
            }
            // Add the new offer item cart if not already exist and if not empty
            if (addOfferCart && !offerCartExists) {
                offersList.add(offerCart)
            }

            val totalPrice: Double = if (!categoryItemsCartExist && addCategoryItemsCart) {
                shoppingCartUiState.totalPrice + categoryItemsCart.totalPrice
            } else if (!offerCartExists && addOfferCart) {
                shoppingCartUiState.totalPrice + offerCart.totalPrice
            } else {
                _shoppingCartUiState.value.shoppingCart.totalPrice
            }

            _shoppingCartUiState.update { newState ->
                newState.copy(
                    shoppingCart = newState.shoppingCart.copy(
                        categoryItemsList = categoryItemsList,
                        offersList = offersList,
                        totalPrice = formatTotal(totalPrice)
                    ),
                )
            }
        }
    }

    fun onCategoryCountDecrease(index: Int, categoryItemsCart: CategoryItemsCart) {
        viewModelScope.launch {
            if (categoryItemsCart.count > 1) {
                val categoryItemsCartList =
                    _shoppingCartUiState.value.shoppingCart.categoryItemsList
                val newCount = categoryItemsCart.count - 1

                val categoryItemsPrice = categoryItemsCart.categoryItems.price

                val currentCategoryItems = categoryItemsCart.copy(count = newCount)
                val updatedCategoryItemList = categoryItemsCartList.toMutableList()
                updatedCategoryItemList[index] = currentCategoryItems
                _shoppingCartUiState.update { newState ->
                    newState.copy(
                        shoppingCart = newState.shoppingCart.copy(
                            categoryItemsList = updatedCategoryItemList,
                            totalPrice = formatTotal(newState.shoppingCart.totalPrice - categoryItemsPrice)
                        )
                    )
                }
            }
        }
    }

    fun onOfferCountDecrease(index: Int, offerCart: OfferCart) {
        viewModelScope.launch {
            if (offerCart.count > 1) {
                val offerCartList = _shoppingCartUiState.value.shoppingCart.offersList
                val newCount = offerCart.count - 1

                val offersPrice = offerCart.offers.price

                val currentOfferCart = offerCart.copy(count = newCount)
                val updatedOfferCartList = offerCartList.toMutableList()
                updatedOfferCartList[index] = currentOfferCart
                _shoppingCartUiState.update { newState ->
                    newState.copy(
                        shoppingCart = newState.shoppingCart.copy(
                            offersList = updatedOfferCartList,
                            totalPrice = formatTotal(newState.shoppingCart.totalPrice - offersPrice)
                        )
                    )
                }
            }
        }
    }

    fun onOfferCountIncrease(index: Int, offerCart: OfferCart) {
        viewModelScope.launch {
            if (offerCart.count < 50) {
                val offerCartList = _shoppingCartUiState.value.shoppingCart.offersList
                val newCount = offerCart.count + 1

                val offerPrice = offerCart.offers.price

                val currentOffer = offerCart.copy(count = newCount)
                val updatedOfferCartList = offerCartList.toMutableList()
                updatedOfferCartList[index] = currentOffer
                _shoppingCartUiState.update { newState ->
                    newState.copy(
                        shoppingCart = newState.shoppingCart.copy(
                            offersList = updatedOfferCartList,
                            totalPrice = formatTotal(newState.shoppingCart.totalPrice + offerPrice)
                        ),
                    )
                }
            }
        }
    }

    fun onCategoryCountIncrease(index: Int, categoryItemsCart: CategoryItemsCart) {
        viewModelScope.launch {
            if (categoryItemsCart.count < 50) {
                val categoryItemsCartList =
                    _shoppingCartUiState.value.shoppingCart.categoryItemsList
                val newCount = categoryItemsCart.count + 1

                val categoryItemsPrice = categoryItemsCart.categoryItems.price

                val currentCategoryItems = categoryItemsCart.copy(count = newCount)
                val updatedCategoryItemList = categoryItemsCartList.toMutableList()
                updatedCategoryItemList[index] = currentCategoryItems
                _shoppingCartUiState.update { newState ->
                    newState.copy(
                        shoppingCart = newState.shoppingCart.copy(
                            categoryItemsList = updatedCategoryItemList,
                            totalPrice = formatTotal(newState.shoppingCart.totalPrice + categoryItemsPrice)
                        ),
                    )
                }
            }
        }
    }

}