package com.example.coffeeshop.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(ratingIndex: Int) {
    val list = DataSource.ratingBarList
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(list) { index, imageDrawable ->
            val isTargeted = index + 1 == ratingIndex
            RatingIcon(
                isTargeted = isTargeted,
                imageDrawable = imageDrawable,
                contentDescription = null
            )
        }
    }
}

//image used instead of vector cause i cant convert all drawables for free
@Composable
fun RatingIcon(isTargeted: Boolean, imageDrawable: Int, contentDescription: String?) {
    val alpha = if (isTargeted) 1F else {
        0.5F
    }
    Image(
        painter = painterResource(imageDrawable), contentDescription = contentDescription,
        modifier = Modifier
            .clip(CircleShape)
            .size(
                if (isTargeted) {
                    26.dp
                } else {
                    18.dp
                }
            ),
        alpha = alpha
    )
}