package com.example.coffeeshop.ui.screen.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.model.items.CategoryItems
import com.example.coffeeshop.domain.usecase.firebaseReadAndWriteUsecase.ReadCategoryDataFromFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val readCategoryDataFromFirebase: ReadCategoryDataFromFirebase,
) : ViewModel() {
    private val _homepageUiState: MutableStateFlow<HomePageUiState> = MutableStateFlow(HomePageUiState())
    val homePageUiState: StateFlow<HomePageUiState> = _homepageUiState.asStateFlow()

    init {
        readData()
    }

    private fun readData(){
        viewModelScope.launch {
            val response = readCategoryDataFromFirebase.readData()
            _homepageUiState.update { newState->
                newState.copy(response = response)
            }
        }
    }

    /*// Update the search query
    fun onSearchTextChange(newText: String) {
        viewModelScope.launch {
            _homepageUiState.update { newState->
                newState.copy(searchText = newText)
            }
            filterList(newText)
        }
    }

    // Filter the list based on the search text
    private fun filterList(query: String) {
        _persons.value = if (query.isEmpty()) {
            allPersons // Return the full list if the query is empty
        } else {
            allPersons.filter { person ->
                // Check if either the first name or the last name contains the query (case-insensitive)
                person.firstName.contains(query, ignoreCase = true) ||
                        person.lastName.contains(query, ignoreCase = true)
            }
        }
    }
    */
}

sealed interface FirebaseResponse {
    data class Success(val categoryMap: HashMap<String,List<CategoryItems>>) : FirebaseResponse
    data class Error(val message: String) : FirebaseResponse
    data object Loading : FirebaseResponse
}