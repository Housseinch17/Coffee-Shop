package com.example.coffeeshop.ui.screen.homepage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun HomePage(modifier: Modifier,onClick: () -> Unit,onSignOut: () -> Unit) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
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