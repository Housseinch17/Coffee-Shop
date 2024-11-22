package com.example.coffeeshop.ui.screen.homepage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.data.model.offers.Offers
import com.example.coffeeshop.domain.usecase.firebaseReadAndWriteUsecase.ReadCategoryDataFromFirebase
import com.example.coffeeshop.domain.usecase.firebaseReadAndWriteUsecase.ReadOffersDataFromFirebase
import com.example.coffeeshop.ui.navigation.CurrentDestination
import com.example.coffeeshop.ui.util.navigateSingleTopTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
class HomePageViewModel @Inject constructor(
    private val readCategoryDataFromFirebase: ReadCategoryDataFromFirebase,
    private val readOffersDataFromFirebase: ReadOffersDataFromFirebase,
) : ViewModel() {
    private val _homepageUiState: MutableStateFlow<HomePageUiState> =
        MutableStateFlow(HomePageUiState())
    val homePageUiState: StateFlow<HomePageUiState> = _homepageUiState.asStateFlow()

    private val _responseError: MutableSharedFlow<String> = MutableSharedFlow()
    val responseError: SharedFlow<String> = _responseError.asSharedFlow()

    init {
        Log.d("ViewModelInitialization", "Home created")
        viewModelScope.launch {
            readData()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization", "home destroyed")
    }

    suspend fun homeEvents(homeEvents: HomeEvents) {
        when (homeEvents) {
            is HomeEvents.OnSeeAllClick -> {
                onSecondSeeAllClick(
                    navHostController = homeEvents.navHostController,
                    route = homeEvents.route
                )
            }
        }
    }

    private suspend fun onSecondSeeAllClick(
        navHostController: NavHostController,
        route: CurrentDestination,
    ) {
        setSeeAllClicked(true)
        delay(500)
        navHostController.navigateSingleTopTo(navHostController = navHostController, route = route)
        setSeeAllClicked(false)
    }

    private suspend fun readData() {
        readCategoryData()
        readOffersData()
        viewModelScope.launch {
            if (_homepageUiState.value.isLoading) {
                _homepageUiState.update { newState ->
                    newState.copy(isLoading = false)
                }
                Log.d("MyTag", "isLoading set false finished")
            }
        }
        Log.d("MyTag", "readData() finished")
    }

    fun refreshData() {
        val homePageUiState = _homepageUiState.value
        viewModelScope.launch {
            if (homePageUiState.currentCategory == CurrentCategory() && homePageUiState.offersList.isEmpty()
            ) {
                _homepageUiState.update { newState ->
                    newState.copy(
                        isRefreshing = true
                    )
                }
                readData()
                _homepageUiState.update { newState ->
                    newState.copy(
                        isRefreshing = false
                    )
                }
            } else {
                emitError("Your data up to date!")
            }
        }
    }

    fun onClearSearchText() {
        filterList("")
        viewModelScope.launch {
            _homepageUiState.update { newState ->
                newState.copy(searchText = "")
            }
        }
    }

    private fun emitError(error: String) {
        viewModelScope.launch {
            _responseError.emit(error)
        }
    }

    fun setCurrentCategory(currentKey: String) {
        viewModelScope.launch {
            filterList("")
            _homepageUiState.update { newState ->
                newState.copy(
                    searchText = "",
                    currentCategory = newState.currentCategory.copy(
                        key = currentKey,
                        categoryList = newState.categoryMap[currentKey] ?: emptyList()
                    ),
                    filteredCategoryList = newState.currentCategory.copy(
                        key = currentKey,
                        categoryList = newState.categoryMap[currentKey] ?: emptyList()
                    ),
                )

            }
        }
    }

    //get the category keys from the received data
    private fun getCategoriesKey(response: FirebaseCategoryResponse.Success) {
        viewModelScope.launch {
            _homepageUiState.update { newState ->
                newState.copy(
                    categoriesKey = response.categoryMap.map {
                        it.key
                    }
                )
            }
        }
    }


    //isLoading is set in readOffersData() because we are reading it after
    //readCategoryData() loader should show when both finishes
    //we can refactor the function to use coroutine that waits all children to finis to complete
    //so we can use isLoading inside the parent scope and not in readOffersData()
    //since viewmodelScope will suspend it and doesn't wait children to finish to complete
    private suspend fun readOffersData() {
        val response = readOffersDataFromFirebase.readOffersData()
        if (response is FirebaseOffersResponse.Success) {
            viewModelScope.launch {
                _homepageUiState.update { newState ->
                    newState.copy(
                        offersList = response.offers,
                        filteredOffersList = response.offers,
                    )
                }
            }
        } else if (response is FirebaseOffersResponse.Error) {
            Log.d("MyTag", "readOffersData() ${response.message}")
            emitError(response.message)
        }
        Log.d("MyTag", "readOffersData() finished")
    }

    fun setSeeAllClicked(isClicked: Boolean) {
        viewModelScope.launch {
            _homepageUiState.update { newState ->
                newState.copy(seeAllClicked = isClicked)
            }
        }
    }


    private suspend fun readCategoryData() {
        val response = readCategoryDataFromFirebase.readCategoryData()
        if (response is FirebaseCategoryResponse.Success) {
            viewModelScope.launch {
                _homepageUiState.update { newState ->
                    newState.copy(categoryMap = response.categoryMap)
                }
            }
            //set category map we use it in setCurrentCategory
            //category map contains all keys with their lists
            //update categoriesKey
            getCategoriesKey(response)

            //set currentCategory as first key
            setCurrentCategory(response.categoryMap.keys.first())

        } else if (response is FirebaseCategoryResponse.Error) {
            emitError(response.message)
        }
        Log.d("MyTag", "readCategoryData() finished")
    }


    // Update the search query
    fun onSearchTextChange(newText: String) {
        viewModelScope.launch {
            _homepageUiState.update { newState ->
                newState.copy(searchText = newText)
            }
            filterList(newText)
        }
    }

    // Filter the list based on the search text
    private fun filterList(query: String) {
        viewModelScope.launch {
            _homepageUiState.update { newState ->
                //filter categories
                val filteredCategoryItems = if (query.isEmpty()) {
                    newState.currentCategory.categoryList
                } else {
                    newState.currentCategory.categoryList.filter { category ->
                        category.title.contains(query, ignoreCase = true)
                    }
                }
                //filter offers
                val filteredOffersItems = if (query.isEmpty()) {
                    newState.offersList
                } else {
                    newState.offersList.filter { offers ->
                        offers.title.contains(query, ignoreCase = true)
                    }
                }
                newState.copy(
                    filteredCategoryList = newState.filteredCategoryList.copy(
                        categoryList = filteredCategoryItems
                    ),
                    filteredOffersList = filteredOffersItems
                )
            }
        }
    }
}

sealed interface FirebaseCategoryResponse {
    data class Success(val categoryMap: HashMap<String, List<CategoryItems>>) :
        FirebaseCategoryResponse

    data class Error(val message: String) : FirebaseCategoryResponse
}

sealed interface FirebaseOffersResponse {
    data class Success(val offers: List<Offers>) :
        FirebaseOffersResponse

    data class Error(val message: String) : FirebaseOffersResponse
}