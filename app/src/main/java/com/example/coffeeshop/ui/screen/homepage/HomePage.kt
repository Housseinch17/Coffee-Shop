package com.example.coffeeshop.ui.screen.homepage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.data.model.offers.Offers
import com.example.coffeeshop.ui.theme.BodyTypography
import com.example.coffeeshop.ui.theme.DescriptionTypography
import com.example.coffeeshop.ui.theme.Gray
import com.example.coffeeshop.ui.theme.Orange
import com.example.coffeeshop.ui.theme.TitleTypography
import com.example.coffeeshop.ui.util.CoffeeImage
import com.example.coffeeshop.ui.util.DataSource
import com.example.coffeeshop.ui.util.RatingBar
import com.example.coffeeshop.ui.util.ShimmerEffect

@Composable
fun HomePage(
    modifier: Modifier,
    isLoading: Boolean,
    searchText: String,
    onClear: () -> Unit,
    onSearch: (String) -> Unit,
    username: String?,
    categoriesKey: List<String>,
    onCategoryClick: (String) -> Unit,
    currentCategory: CurrentCategory,
    onFirstSeeAllClick: (List<CategoryItems>) -> Unit,
    onItemClick: (CategoryItems) -> Unit,
    offersList: List<Offers>,
    onSecondSeeAllClick: (List<Offers>) -> Unit,
    onOffersClick: (Offers) -> Unit
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
            modifier = modifier
                .padding(horizontal = 20.dp)
                .verticalScroll(
                    rememberScrollState()
                ),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (isLoading) {
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    items(5) {
                        ShimmerEffect(
                            modifier = Modifier
                                .width(60.dp)
                                .height(40.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ShimmerEffect(
                        modifier = Modifier
                            .width(120.dp)
                            .height(40.dp)
                    )
                    ShimmerEffect(
                        modifier = Modifier
                            .width(80.dp)
                            .height(30.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                LazyRow(
                    contentPadding = PaddingValues(top = 20.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(3) {
                        ShimmerEffect(modifier = Modifier.size(140.dp))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ShimmerEffect(
                        modifier = Modifier
                            .width(120.dp)
                            .height(40.dp)
                    )
                    ShimmerEffect(
                        modifier = Modifier
                            .width(80.dp)
                            .height(30.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                LazyRow(
                    contentPadding = PaddingValues(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(3) {
                        ShimmerEffect(
                            modifier = Modifier
                                .width(200.dp)
                                .height(140.dp)
                                .clip(RoundedCornerShape(12.dp)),
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
            } else {
                SearchView(
                    modifier = Modifier.fillMaxWidth(),
                    searchText = searchText,
                    onClear = onClear,
                    onSearch = onSearch
                )
                Spacer(modifier = Modifier.height(16.dp))
                TopText(modifier = Modifier.fillMaxWidth(), username = username.toString())
                Spacer(modifier = Modifier.height(20.dp))
                TabRow(
                    currentSelected = currentCategory.key,
                    categoriesKey = categoriesKey,
                    onCategoryClick = onCategoryClick
                )
                Spacer(modifier = Modifier.height(20.dp))
                SeeAll(modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.popular),
                    onSeeAllClick = { onFirstSeeAllClick(currentCategory.categoryList) })
                Spacer(modifier = Modifier.height(20.dp))
                CurrentCategoryList(
                    currentCategoryList = currentCategory.categoryList, onItemClick = onItemClick
                )
                Spacer(modifier = Modifier.height(10.dp))
                SeeAll(modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.available_offers),
                    onSeeAllClick = { onSecondSeeAllClick(offersList) })
                //no need for spacer padding because we used contentPadding for OffersList
                OffersList(offersList = offersList) { offers ->
                    onOffersClick(offers.copy(price = DataSource.calculateTotal(
                        offers.price,
                        offers.discount
                    )))
                }
            }
        }
    }
}

@Composable
fun OffersList(offersList: List<Offers>, onOffersClick: (Offers) -> Unit) {
    LazyRow(
        modifier = Modifier.navigationBarsPadding(),
        contentPadding = PaddingValues(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(offersList) { offers ->
            OffersItem(offers) {
                onOffersClick(offers)
            }
        }
    }
}

@Composable
fun OffersItem(offers: Offers, onOffersClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable {
                onOffersClick()
            }
            .width(200.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            CoffeeImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp), imageUrl = offers.picUrl
            )
            Spacer(modifier = Modifier.height(8.dp))
            //text align used for the second line to show in center
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = offers.description,
                    minLines = 2,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    style = DescriptionTypography.copy(color = Color.Black)
                )
                Text(
                    text = (stringResource(R.string.discount) + ": " + offers.discount + "%"),
                    style = BodyTypography,
                    maxLines = 1
                )
                Text(
                    text = stringResource(R.string.total) + ": " + DataSource.calculateTotal(
                        offers.price,
                        offers.discount
                    ) + "$",
                    style = TitleTypography.copy(color = Orange),
                    maxLines = 1,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}

@Composable
fun TabRow(
    currentSelected: String, categoriesKey: List<String>, onCategoryClick: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(categoriesKey) { category ->
            val isSelected = currentSelected == category
            TabButton(
                category,
                isSelected = isSelected,
                onCategoryClick = { onCategoryClick(category) })
        }
    }
}

@Composable
fun SeeAll(modifier: Modifier, title: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge.copy(
                lineHeight = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
        TextButton(onClick = onSeeAllClick) {
            Text(
                stringResource(R.string.see_all),
                color = Orange,
                style = MaterialTheme.typography.titleMedium.copy(
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Composable
fun CurrentCategoryList(
    currentCategoryList: List<CategoryItems>, onItemClick: (CategoryItems) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(top = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(currentCategoryList) { categoryItem ->
            CategoryItem(categoryItem, onItemClick = { onItemClick(categoryItem) })
        }
    }
}

@Composable
fun CategoryItem(categoryItem: CategoryItems, onItemClick: () -> Unit) {
    ConstraintLayout {
        val (card, image) = createRefs()
        CategoryCardItem(
            modifier = Modifier
                .size(140.dp)
                .constrainAs(card) {
                    top.linkTo(parent.top)
                }, categoryItem = categoryItem,
            ratingIndex = categoryItem.rating.toInt(), onItemClick = onItemClick
        )
        CoffeeImage(
            modifier = Modifier
                .width(100.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .constrainAs(image) {
                    top.linkTo(card.top)
                    bottom.linkTo(card.top)
                    start.linkTo(card.start)
                    end.linkTo(card.end)
                },
            imageUrl = categoryItem.picUrl,
        )
    }
}

@Composable
fun CategoryCardItem(
    modifier: Modifier,
    categoryItem: CategoryItems,
    ratingIndex: Int,
    onItemClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable {
            onItemClick()
        },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(25.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            //here top = 50.dp because we coffee image size is 80.dp its taking 40 top 40 bottom
            //those 40 bottom are inside the card
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            RatingBar(ratingIndex = ratingIndex, smallSize = 18.dp, largeSize = 26.dp)
            Text(
                modifier = Modifier
                    .widthIn(max = 100.dp),
                text = categoryItem.title,
                maxLines = 1,
                style = TitleTypography,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.widthIn(max = 100.dp),
                text = categoryItem.extra,
                maxLines = 1,
                style = DescriptionTypography,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier
                    .widthIn(max = 100.dp),
                text = "$${categoryItem.price}",
                maxLines = 1,
                style = BodyTypography.copy(color = Orange),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TabButton(category: String, isSelected: Boolean, onCategoryClick: () -> Unit) {
    val color = if (isSelected) Orange else Gray
    TextButton(
        onClick = onCategoryClick,
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
        )
    ) {
        Text(text = category, color = Color.White, fontWeight = FontWeight.Normal, fontSize = 14.sp)
    }
}


@Composable
fun TopText(modifier: Modifier, username: String) {
    val textWithUsername = buildAnnotatedString {
        append(stringResource(R.string.good_morning))
        append("  ")
        withStyle(style = SpanStyle(Orange)) {
            append(username)
        }
        append("!")
    }

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            text = textWithUsername,
            maxLines = 1,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            text = stringResource(R.string.grab_your_items),
            maxLines = 1,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 14.sp, lineHeight = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchView(
    modifier: Modifier,
    searchText: String,
    onClear: () -> Unit,
    onSearch: (String) -> Unit
) {
    //keyboard controller to show or hide keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    //current focus manager if focused or not
    val focusManager = LocalFocusManager.current

    //check if keyboard is open or closed
    val isImeVisible = WindowInsets.isImeVisible

    //when keyboard closed clear focus (show unfocusedContainerColor)
    if (!isImeVisible) {
        focusManager.clearFocus()
    }


    TextField(
        modifier = modifier
            .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(25.dp)),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.search),
                tint = Color.White
            )
        },
        trailingIcon = {
            IconButton(onClick = onClear) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = stringResource(R.string.clear),
                    tint = Color.White
                )
            }
        },
        textStyle = TextStyle.Default.copy(color = Color.White, fontWeight = FontWeight.Bold),
        maxLines = 1,
        value = searchText,
        onValueChange = onSearch,
        shape = RoundedCornerShape(25.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Orange,
            unfocusedContainerColor = Color.Transparent
        ),
        placeholder = {
            Text(text = stringResource(R.string.search), color = Color.White)
        },
    )
}