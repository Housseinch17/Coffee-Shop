package com.example.coffeeshop.ui.screen.allOffersDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.offers.Offers
import com.example.coffeeshop.ui.screen.homepage.OffersItem
import com.example.coffeeshop.ui.theme.TitleTypography
import com.example.coffeeshop.ui.util.AppCard

@Composable
fun AllOffersPage(
    modifier: Modifier, isLoading: Boolean, offersList: List<Offers>,
    onItemClick: (Offers) -> Unit
) {
    AppCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.Center)
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.all_offers),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = TitleTypography.copy(color = Color.White)
                )
                Spacer(modifier = Modifier.height(20.dp))
                LazyColumn (
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(20.dp)
                ) {
                    items(offersList) { offers ->
                        Spacer(modifier = Modifier.height(20.dp))
                        OffersItem(modifier = Modifier
                            .fillMaxWidth(),
                            offers = offers,
                            onOffersClick = {
                                onItemClick(offers)
                            })
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}