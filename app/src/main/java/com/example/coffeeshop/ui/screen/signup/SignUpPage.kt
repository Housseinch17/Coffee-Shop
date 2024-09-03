package com.example.coffeeshop.ui.screen.signup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.coffeeshop.ui.sharedcomponent.SharedScreen

@Composable
fun SignUpScreen(
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
    enabled: Boolean,
    onCreateAccount: (email: String, password: String) -> Unit,
    onExistingAccount: () -> Unit
) {
    SharedScreen(
        modifier,
        textPage = textPage,
        emailValue = emailValue,
        onEmailChange = onEmailChange,
        imageVector = imageVector,
        onIconClick = onIconClick,
        showPassword = showPassword,
        passwordValue = passwordValue,
        onPasswordChange = onPasswordChange,
        button = buttonText,
        textButton = accountTextButton,
        enabled = enabled,
        onButtonClick = { email, password ->
            onCreateAccount(email, password)
        },
        onTextButtonClick = onExistingAccount
    )
}
