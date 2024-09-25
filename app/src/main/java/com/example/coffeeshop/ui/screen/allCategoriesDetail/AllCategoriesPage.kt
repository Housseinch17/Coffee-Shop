package com.example.coffeeshop.ui.screen.allCategoriesDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.ui.screen.homepage.CategoryItem
import com.example.coffeeshop.ui.theme.TitleTypography
import com.example.coffeeshop.ui.util.AppCard

@Composable
fun AllCategoriesPage(
    modifier: Modifier, isLoading: Boolean, categoriesList: List<CategoryItems>,
    onItemClick: (CategoryItems) -> Unit
) {
    val imageTopPadding: Dp = 40.dp
    AppCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.Center)
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.all_categories),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = TitleTypography.copy(color = Color.White)
                )
                Spacer(modifier = Modifier.height(20.dp))
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(imageTopPadding + 20.dp),
                    contentPadding = PaddingValues(top = imageTopPadding, bottom = 20.dp)
                ) {
                    items(categoriesList) { categoryItem ->
                        Spacer(modifier = Modifier.height(imageTopPadding))
                        CategoryItem(modifier = Modifier
                            .fillMaxWidth(),
                            categoryItem = categoryItem,
                            onItemClick = {
                                onItemClick(categoryItem)
                            })
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}