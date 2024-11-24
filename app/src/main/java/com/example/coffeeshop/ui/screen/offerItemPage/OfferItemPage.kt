package com.example.coffeeshop.ui.screen.offerItemPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.shoppingCart.OfferCart
import com.example.coffeeshop.ui.theme.BodyTypography
import com.example.coffeeshop.ui.theme.DescriptionTypography
import com.example.coffeeshop.ui.util.AddToCartButton
import com.example.coffeeshop.ui.util.OrderCountWithImage
import com.example.coffeeshop.ui.util.RatingBar
import com.example.coffeeshop.ui.util.TitleAndPrice

@Composable
fun OfferItemPage(
    modifier: Modifier,
    offerCart: OfferCart,
    onCountRemove: () -> Unit, onCountAdd: () -> Unit,
    addToCard: (OfferCart) -> Unit,
) {
    val scrollState = rememberScrollState()
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
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = offerCart.offers.offerDescription,
                style = BodyTypography.copy(color = Color.White)
            )
            OrderCountWithImage(
                count = offerCart.count, onCountRemove = onCountRemove, onCountAdd = onCountAdd,
                imageUrl = offerCart.offers.picUrl
            )
            Spacer(Modifier.height(10.dp))
            TitleAndPrice(title = offerCart.offers.title, price = offerCart.totalPrice)
            Spacer(modifier = Modifier.height(10.dp))
            RatingBar(offerCart.offers.rating, smallSize = 24.dp, largeSize = 34.dp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = offerCart.offers.description,
                style = DescriptionTypography
            )
            Spacer(modifier.weight(1f))
            AddToCartButton {
                addToCard(offerCart)
            }
        }
    }
}