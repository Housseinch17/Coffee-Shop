package com.example.coffeeshop.ui.screen.homepage

import androidx.compose.runtime.Immutable
import com.example.coffeeshop.data.model.items.CategoryItems
import com.example.coffeeshop.data.model.offers.Offers

@Immutable
data class HomePageUiState(
    val isLoading: Boolean = true,
    val searchText: String = "",
    val categoryMap: HashMap<String,List<CategoryItems>> = HashMap(),
    val categoriesKey: List<String> = emptyList(),
    val currentCategory: CurrentCategory = CurrentCategory("", emptyList()),
    val filteredCategoryList: CurrentCategory = CurrentCategory("", emptyList()),
    val offersList: List<Offers> = emptyList(),
    val filteredOffersList: List<Offers> = emptyList(),
)


@Immutable
data class CurrentCategory(val key: String,val categoryList: List<CategoryItems>)