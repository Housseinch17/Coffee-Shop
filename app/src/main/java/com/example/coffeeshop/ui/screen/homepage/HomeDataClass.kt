package com.example.coffeeshop.ui.screen.homepage

import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.data.model.offers.Offers

data class HomeTopPageState(
    val searchText: String,
    val username: String?,
    val onClear: () -> Unit,
    val onSearch: (String) -> Unit,
)

data class HomeCenterPageState(
    val categoriesKey: List<String>,
    val currentCategory: CurrentCategory,
    val onCategoryClick: (String) -> Unit,
    val onFirstSeeAllClick: (List<CategoryItems>) -> Unit,
    val onItemClick: (CategoryItems) -> Unit,
)

data class HomeBottomPageState(
    val offersList: List<Offers>,
    val onSecondSeeAllClick: (List<Offers>) -> Unit,
    val onOffersClick: (Offers) -> Unit,
)
