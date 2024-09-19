package com.example.coffeeshop.ui.screen.categoryItemPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart
import com.example.coffeeshop.ui.theme.DescriptionTypography
import com.example.coffeeshop.ui.util.AddToCartButton
import com.example.coffeeshop.ui.util.OrderCountWithImage
import com.example.coffeeshop.ui.util.RatingBar
import com.example.coffeeshop.ui.util.TitleAndPrice


@Composable
fun CategoryItemPage(
    modifier: Modifier,
    categoryItemsCart: CategoryItemsCart,onCountRemove: () -> Unit, onCountAdd: () -> Unit,
    addToCard: (categoryItemsCart: CategoryItemsCart) -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        Image(
            painterResource(id = R.drawable.coffee_bean),
            contentDescription = stringResource(R.string.background_image),
            modifier = modifier,
            contentScale = ContentScale.FillBounds,
        )
        Column(
            modifier = modifier
                .padding(horizontal = 20.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OrderCountWithImage(
                count = categoryItemsCart.count, onCountRemove = onCountRemove, onCountAdd = onCountAdd,
                imageUrl = categoryItemsCart.categoryItems.picUrl
            )
            Spacer(Modifier.height(10.dp))
            TitleAndPrice(title = categoryItemsCart.categoryItems.title, price = categoryItemsCart.totalPrice)
            Spacer(Modifier.height(10.dp))
            RatingBar(categoryItemsCart.categoryItems.rating.toInt(), smallSize = 24.dp, largeSize = 34.dp)
            Spacer(Modifier.height(10.dp))
            Text(
                text = categoryItemsCart.categoryItems.description,
                style = DescriptionTypography
            )
            Spacer(modifier.weight(1f))
            AddToCartButton {
                addToCard(categoryItemsCart)
            }
        }
    }
}

