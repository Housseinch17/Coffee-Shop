package com.example.coffeeshop.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.coffeeshop.R
import com.example.coffeeshop.ui.sharedcomponent.AccountButton
import com.example.coffeeshop.ui.sharedcomponent.AccountTextButton
import com.example.coffeeshop.ui.sharedcomponent.EmailAndPassword
import com.example.coffeeshop.ui.sharedcomponent.ShimmerEffect
import com.example.coffeeshop.ui.sharedcomponent.TrailingIcon


@Composable
fun LogInScreen(
    modifier: Modifier,
    textPage: String,
    emailValue: String,
    onEmailChange: (String) -> Unit,
    imageVector: ImageVector,
    onIconClick: () -> Unit,
    showPassword: Boolean,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    buttonText: String,
    accountTextButton: String,
    logInEnabled: Boolean,
    signUpEnabled: Boolean,
    isLoading: Boolean,
    onLogInClick: (email: String, password: String) -> Unit,
    onSignUpClick: () -> Unit
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
                .matchParentSize()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                // Shimmer for textPage
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp), color = Color.White
                )
                Spacer(Modifier.height(16.dp))
                // Shimmer for email field
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    color = Color.White
                )
                Spacer(Modifier.height(16.dp))
                // Shimmer for password field
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), color = Color.White
                )
                Spacer(Modifier.height(16.dp))
                // Shimmer for button
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp), color = Color.White
                )
                Spacer(Modifier.height(24.dp))
                // Shimmer for text button
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp), color = Color.White
                )
            } else {
                Text(textPage, style = MaterialTheme.typography.titleLarge, color = Color.White)
                Spacer(Modifier.height(16.dp))
                EmailAndPassword(
                    Modifier.fillMaxWidth(),
                    emailValue = emailValue,
                    onEmailChange = onEmailChange,
                    showPassword = showPassword,
                    passwordValue = passwordValue,
                    onPasswordChange = onPasswordChange
                ) {
                    TrailingIcon(imageVector, onIconClick = onIconClick)
                }
                Spacer(Modifier.height(16.dp))
                AccountButton(Modifier, buttonText, buttonEnabled = logInEnabled) {
                    onLogInClick(emailValue, passwordValue)
                }
                Spacer(Modifier.height(24.dp))
                AccountTextButton(
                    Modifier,
                    text = accountTextButton,
                    textButtonEnabled = signUpEnabled,
                    onSignUpClick = onSignUpClick
                )
            }
        }
    }
}

