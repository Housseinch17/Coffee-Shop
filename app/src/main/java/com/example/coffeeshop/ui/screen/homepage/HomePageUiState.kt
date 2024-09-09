package com.example.coffeeshop.ui.screen.homepage

import androidx.compose.runtime.Immutable
import com.example.coffeeshop.data.model.items.CategoryItems

@Immutable
data class HomePageUiState(
    val response: FirebaseResponse = FirebaseResponse.Loading,
    val searchText: String = "",
    val categoryMap: HashMap<String,List<CategoryItems>> = HashMap(),
    val currentCategory: CurrentCategory = CurrentCategory("", emptyList())
)


@Immutable
data class CurrentCategory(val key: String,val categoryList: List<CategoryItems>)