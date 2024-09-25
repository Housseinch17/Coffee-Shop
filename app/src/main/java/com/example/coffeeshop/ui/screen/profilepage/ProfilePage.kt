package com.example.coffeeshop.ui.screen.profilepage

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.coffeeshop.ui.theme.TitleTypography
import com.example.coffeeshop.ui.util.AppCard

@Composable
fun ProfilePage(modifier: Modifier) {
    AppCard(modifier) {
        Box(modifier = modifier) {
            Text(
                "Coming Soon!", style = TitleTypography.copy(color = Color.White),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}