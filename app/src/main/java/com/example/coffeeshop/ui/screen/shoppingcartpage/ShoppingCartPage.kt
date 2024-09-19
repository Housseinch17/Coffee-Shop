package com.example.coffeeshop.ui.screen.shoppingcartpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart
import com.example.coffeeshop.data.model.shoppingCart.OfferCart
import com.example.coffeeshop.ui.theme.BodyTypography
import com.example.coffeeshop.ui.theme.BrightBlue
import com.example.coffeeshop.ui.theme.DescriptionTypography
import com.example.coffeeshop.ui.theme.Orange
import com.example.coffeeshop.ui.theme.TitleTypography
import com.example.coffeeshop.ui.util.CoffeeImage

@Composable
fun ShoppingCartPage(
    modifier: Modifier,
    categoryItemsCartList: List<CategoryItemsCart>,
    offerCartList: List<OfferCart>,
    totalPrice: Double,
    onCategoryCountDecrease: (index: Int, categoryItemsCart: CategoryItemsCart) -> Unit,
    onCategoryCountIncrease: (index: Int, categoryItemsCart: CategoryItemsCart) -> Unit,
    onOfferCountDecrease: (index: Int, offerCart: OfferCart) -> Unit,
    onOfferCountIncrease: (index: Int, offerCart: OfferCart) -> Unit,
    onCheckOut: () -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        Image(
            painterResource(id = R.drawable.coffee_bean),
            contentDescription = stringResource(R.string.background_image),
            modifier = modifier,
            contentScale = ContentScale.FillBounds,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 10.dp,
                    bottom = 20.dp
                ),
        ) {
            if (categoryItemsCartList.isNotEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.category_orders),
                    style = DescriptionTypography.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                //column was used instead of lazy column to avoid using static height
                //to achieve vertical scrolling without crashing
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    categoryItemsCartList.forEachIndexed { index, categoryItem ->
                        CategoryItemComponent(
                            categoryItemsCart = categoryItem,
                            onCategoryCountDecrease = {
                                onCategoryCountDecrease(index, categoryItem)
                            },
                            onCategoryCountIncrease = {
                                onCategoryCountIncrease(index, categoryItem)
                            }
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        HorizontalDivider(modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (offerCartList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier,
                    text = stringResource(R.string.offer_orders),
                    style = DescriptionTypography.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                //column was used instead of lazy column to avoid using static height
                //to achieve vertical scrolling without crashing
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    offerCartList.forEachIndexed { index, offerCart ->
                        OfferCartComponent(
                            offerCart = offerCart,
                            onOfferCountIncrease = {
                                onOfferCountIncrease(index, offerCart)
                            },
                            onOfferCountDecrease = {
                                onOfferCountDecrease(index, offerCart)
                            }
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        HorizontalDivider(modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(
                            rememberScrollState()
                        ),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    text = stringResource(R.string.total_price) + ": $totalPrice $",
                    style = TitleTypography.copy(color = BrightBlue, lineHeight = 24.sp)
                )
                Button(
                    onClick = onCheckOut,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange,
                    )
                ) {
                    Text(
                        text = stringResource(R.string.checkout),
                        style = TitleTypography.copy(color = Color.White)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun CategoryItemComponent(
    categoryItemsCart: CategoryItemsCart,
    onCategoryCountDecrease: () -> Unit,
    onCategoryCountIncrease: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp)),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoffeeImage(
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp)),
            imageUrl = categoryItemsCart.categoryItems.picUrl
        )
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = categoryItemsCart.categoryItems.title,
                    style = BodyTypography.copy(Orange, fontSize = 14.sp)
                )
                Text(
                    text = stringResource(R.string.price) + ": " + categoryItemsCart.categoryItems.price,
                    style = BodyTypography.copy(Color.White, fontSize = 14.sp)
                )
            }
            AddAndRemoveCount(
                modifier = Modifier,
                count = categoryItemsCart.count,
                onCountRemove = onCategoryCountDecrease,
                onCountAdd = onCategoryCountIncrease
            )
        }
    }
}

@Composable
fun OfferCartComponent(
    offerCart: OfferCart,
    onOfferCountDecrease: () -> Unit,
    onOfferCountIncrease: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp)),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        CoffeeImage(
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp)),
            imageUrl = offerCart.offers.picUrl
        )
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = offerCart.offers.title,
                    style = BodyTypography.copy(Orange, fontSize = 14.sp)
                )
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = "price: " + offerCart.offers.price,
                    style = BodyTypography.copy(Color.White, fontSize = 14.sp)
                )
            }
            AddAndRemoveCount(
                modifier = Modifier,
                count = offerCart.count,
                onCountRemove = onOfferCountDecrease,
                onCountAdd = onOfferCountIncrease
            )
        }
    }
}

@Composable
fun AddAndRemoveCount(
    modifier: Modifier,
    count: Int,
    onCountRemove: () -> Unit,
    onCountAdd: () -> Unit
) {
    Row(
        modifier = modifier
            .border(1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        IconButton(
            onClick = onCountRemove, modifier = Modifier
                .width(40.dp)
                .height(24.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Filled.Remove,
                contentDescription = stringResource(R.string.remove),
                tint = Color.Black
            )
        }
        Text(
            modifier = Modifier,
            text = "$count",
            style = BodyTypography.copy(color = Orange, fontSize = 24.sp, lineHeight = 20.sp)
        )

        IconButton(
            onClick = onCountAdd, modifier = Modifier
                .width(40.dp)
                .height(24.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.add),
                tint = Color.Black
            )
        }
    }
}