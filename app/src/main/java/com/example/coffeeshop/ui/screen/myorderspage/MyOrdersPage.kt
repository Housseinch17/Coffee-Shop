package com.example.coffeeshop.ui.screen.myorderspage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart
import com.example.coffeeshop.data.model.shoppingCart.OfferCart
import com.example.coffeeshop.ui.theme.BodyTypography
import com.example.coffeeshop.ui.theme.BrightBlue
import com.example.coffeeshop.ui.theme.Orange
import com.example.coffeeshop.ui.theme.TitleTypography
import com.example.coffeeshop.ui.util.CoffeeImage

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyOrdersPage(
    modifier: Modifier,
    myOrdersList: List<MyOrders>,
    onExpand: (index: Int) -> Unit,
    isLoading: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh,
    )
    Box(
        modifier = modifier.pullRefresh(pullRefreshState)
    ) {
        Image(
            painter = painterResource(R.drawable.coffee_bean),
            contentDescription = stringResource(R.string.background_image),
            modifier = modifier,
            contentScale = ContentScale.FillBounds
        )

        if (myOrdersList.isEmpty() && !isRefreshing) {
            Box(modifier = modifier) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.no_orders_yet), style = TitleTypography.copy(
                        fontSize = 30.sp,
                        color = BrightBlue,
                    )
                )
            }
        }else if(myOrdersList.isEmpty() && isRefreshing){
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                CircularProgressIndicator(modifier = Modifier.size(200.dp))
            }
        }
        else {
            if (isLoading) {
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(200.dp))
                }
            } else {
                MyOrderComponent(
                    modifier = modifier, myOrdersList = myOrdersList,
                    onExpand = onExpand
                )
            }
        }
        // Adding the PullRefreshIndicator
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun MyOrderComponent(
    modifier: Modifier,
    myOrdersList: List<MyOrders>,
    onExpand: (index: Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(20.dp)
    ) {
        myOrderItemList(
            modifier = Modifier
                .fillMaxWidth(),
            myOrdersList = myOrdersList,
            onExpand = onExpand
        )
    }
}


fun LazyListScope.myOrderItemList(
    modifier: Modifier,
    myOrdersList: List<MyOrders>,
    onExpand: (index: Int) -> Unit
) {
    itemsIndexed(myOrdersList) { index, myOrders ->
        Card(
            modifier = modifier.clickable {
                onExpand(index)
            },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Text(
                text = stringResource(R.string.order) + ": ${myOrders.shoppingCart.id}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = TitleTypography.copy(color = Color.White, fontSize = 24.sp)
            )
            MyOrderItem(
                modifier = Modifier.fillMaxWidth(),
                myOrders = myOrders,
            )
        }
    }
}

@Composable
fun MyOrderItem(
    modifier: Modifier,
    myOrders: MyOrders,
) {
    Column(
        modifier = modifier,
    ) {
        TotalAndExpand(
            totalPrice = myOrders.shoppingCart.totalPrice,
            isExpanded = myOrders.isExpanded,
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (myOrders.isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                if (myOrders.shoppingCart.categoryItemsList.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.category_orders) + ":",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = TitleTypography
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    myOrders.shoppingCart.categoryItemsList.forEach { categoryItem ->
                        CategoryItem(categoryItem)
                        Spacer(modifier = Modifier.height(4.dp))
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            color = Orange
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                if (myOrders.shoppingCart.offersList.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.offer_orders) + ":",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = TitleTypography
                    )
                    myOrders.shoppingCart.offersList.forEach { offerItem ->
                        OfferItem(offerItem)
                        Spacer(modifier = Modifier.height(4.dp))
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            color = Orange
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun OfferItem(
    offerCart: OfferCart,
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
                    text = offerCart.offers.title,
                    style = BodyTypography.copy(Orange, fontSize = 14.sp)
                )
                Text(
                    text = stringResource(R.string.price) + ": " + offerCart.offers.price,
                    style = BodyTypography.copy(Color.White, fontSize = 14.sp)
                )
            }
        }
    }
}


@Composable
fun CategoryItem(
    categoryItemsCart: CategoryItemsCart,
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
        }
    }
}


@Composable
fun TotalAndExpand(totalPrice: Double, isExpanded: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.total) + ": $totalPrice$",
            modifier = Modifier.weight(1f), style = TitleTypography.copy(color = Orange)
        )
        if (isExpanded)
            Icon(imageVector = Icons.Filled.ArrowUpward, contentDescription = null)
        else
            Icon(imageVector = Icons.Filled.ArrowDownward, contentDescription = null)
    }
}


