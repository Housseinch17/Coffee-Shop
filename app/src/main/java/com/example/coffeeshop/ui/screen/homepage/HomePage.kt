package com.example.coffeeshop.ui.screen.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.coffeeshop.R

@Composable
fun HomePage(modifier: Modifier,onClick: () -> Unit,onSignOut: () -> Unit) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(id = R.drawable.coffee_bean), contentDescription = "BackGround Image",
            modifier = modifier,
            contentScale = ContentScale.FillBounds,
        )
        Column {
            TextButton(onClick = onClick) {
                Text("Click for response")
            }
            Color.White
            TextButton(onClick = onSignOut) {
                Text("Signout Button")
            }
            Color.White
        }
    }
}