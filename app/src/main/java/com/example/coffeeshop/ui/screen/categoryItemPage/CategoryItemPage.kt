package com.example.coffeeshop.ui.screen.categoryItemPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.ui.theme.BodyTypography
import com.example.coffeeshop.ui.theme.DescriptionTypography
import com.example.coffeeshop.ui.theme.Orange
import com.example.coffeeshop.ui.theme.TitleTypography
import com.example.coffeeshop.ui.util.CoffeeImage
import com.example.coffeeshop.ui.util.RatingBar


@Composable
fun CategoryItemPage(
    modifier: Modifier,
    categoryItems: CategoryItems,
    count: Int,
    totalPrice: Int, onCountRemove: () -> Unit, onCountAdd: () -> Unit,
    addToCard: (count: Int, categoryItems: CategoryItems) -> Unit,
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
                count = count, onCountRemove = onCountRemove, onCountAdd = onCountAdd,
                imageUrl = categoryItems.picUrl
            )
            Spacer(Modifier.height(10.dp))
            TitleAndPrice(title = categoryItems.title, price = totalPrice)
            Spacer(Modifier.height(10.dp))
            RatingBar(categoryItems.rating.toInt(), smallSize = 24.dp, largeSize = 34.dp)
            Spacer(Modifier.height(10.dp))
            Text(
                text = categoryItems.description,
                style = DescriptionTypography
            )
            Spacer(modifier.weight(1f))
            AddToCartButton {
                addToCard(count, categoryItems)
            }
        }
    }
}

@Composable
fun OrderCountWithImage(
    count: Int,
    onCountRemove: () -> Unit,
    onCountAdd: () -> Unit,
    imageUrl: String
) {
    ConstraintLayout {
        val (image, orderCount) = createRefs()
        DetailImage(
            modifier = Modifier
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }, imageUrl
        )
        OrderCount(
            modifier = Modifier.constrainAs(orderCount) {
                top.linkTo(image.bottom)
                bottom.linkTo(image.bottom)
                start.linkTo(image.start)
                end.linkTo(image.end)
            },
            count = count, onCountRemove = onCountRemove, onCountAdd = onCountAdd
        )
    }
}

@Composable
fun OrderCount(modifier: Modifier, count: Int, onCountRemove: () -> Unit, onCountAdd: () -> Unit) {
    Row(
        modifier = modifier
            .border(1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        IconButton(onClick = onCountRemove, modifier = Modifier.size(44.dp)) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = stringResource(R.string.remove),
                tint = Color.Black
            )
        }
        Text(
            text = "$count",
            style = BodyTypography.copy(color = Orange, fontSize = 22.sp, lineHeight = 24.sp)
        )
        IconButton(onClick = onCountAdd, modifier = Modifier.size(44.dp)) {
            Icon(
                imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.add),
                tint = Color.Black
            )
        }
    }
}

@Composable
fun DetailImage(modifier: Modifier, imageUrl: String) {
    CoffeeImage(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp)),
        imageUrl = imageUrl,
    )
}

@Composable
fun TitleAndPrice(title: String, price: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TitleTypography.copy(color = Color.White, fontSize = 24.sp)
        )
        Text(
            text = "$$price",
            style = BodyTypography.copy(color = Orange, fontSize = 24.sp)
        )
    }
}

@Composable
fun AddToCartButton(addToCard: () -> Unit) {
    Button(
        onClick = addToCard,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Orange)
    ) {
        Text(
            text = stringResource(R.string.add_to_cart),
            style = TitleTypography.copy(color = Color.White, lineHeight = 26.sp)
        )
    }
}