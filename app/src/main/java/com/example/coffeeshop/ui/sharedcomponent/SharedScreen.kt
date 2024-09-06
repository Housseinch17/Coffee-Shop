package com.example.coffeeshop.ui.sharedcomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.coffeeshop.R

@Composable
fun SharedScreen(
    modifier: Modifier,
    textPage: String,
    emailValue: String,
    onEmailChange: (String) -> Unit,
    imageVector: ImageVector,
    onIconClick: () -> Unit,
    showPassword: Boolean,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    button: String,
    textButton: String,
    buttonEnabled: Boolean,
    textButtonEnabled: Boolean,
    onButtonClick: (email: String, password: String) -> Unit,
    onTextButtonClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(textPage, style = MaterialTheme.typography.titleMedium, color = Color.Black)
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
        AccountButton(Modifier, button,buttonEnabled = buttonEnabled) {
            onButtonClick(emailValue, passwordValue)
        }
        Spacer(Modifier.height(24.dp))
        AccountTextButton(Modifier, text = textButton, textButtonEnabled = textButtonEnabled, onSignUpClick = onTextButtonClick)
    }
}


@Composable
fun CoffeeImage(modifier: Modifier, imageUrl: String?) {
    if (imageUrl != null) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .error(R.drawable.connectionerror)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier)
    }
}


@Composable
fun TrailingIcon(imageVector: ImageVector, onIconClick: () -> Unit) {
    IconButton(onClick = onIconClick, modifier = Modifier) {
        Icon(imageVector = imageVector, contentDescription = stringResource(R.string.visibility))
    }
}

@Composable
fun AccountTextButton(
    modifier: Modifier,
    text: String,
    textButtonEnabled: Boolean,
    onSignUpClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onSignUpClick,
        enabled = textButtonEnabled
    ) {
        Text(text)
    }
}

@Composable
fun AccountButton(modifier: Modifier, text: String, buttonEnabled: Boolean,onLogInClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onLogInClick,
        enabled = buttonEnabled
    ) {
        Text(text = text)
    }
}

@Composable
fun EmailAndPassword(
    modifier: Modifier,
    emailValue: String,
    onEmailChange: (String) -> Unit,
    showPassword: Boolean,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    trailingIcon: @Composable () -> Unit,
) {
    Column(modifier) {
        EmailTextField(
            modifier = modifier, label = stringResource(R.string.email), value = emailValue,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { newEmail ->
                onEmailChange(newEmail)
            },
        )
        Spacer(Modifier.height(16.dp))
        PasswordTextField(
            modifier = modifier,
            label = stringResource(R.string.password), value = passwordValue,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = getPasswordVisualTransformation(showPassword),
            onValueChange = { newPassword ->
                onPasswordChange(newPassword)
            },
            trailingIcon = trailingIcon
        )
    }
}

@Composable
fun PasswordTextField(
    modifier: Modifier, label: String,
    value: String, keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation, onValueChange: (String) -> Unit,
    trailingIcon: @Composable () -> Unit,
) {
    OutlinedTextField(
        maxLines = 1,
        textStyle = TextStyle.Default.copy(fontSize = 12.sp),
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        singleLine = true,
        modifier = modifier,
        label = {
            Text(text = label)
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
}

@Composable
fun EmailTextField(
    modifier: Modifier, label: String,
    value: String, keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        maxLines = 1,
        textStyle = TextStyle.Default.copy(fontSize = 12.sp),
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        singleLine = true,
        modifier = modifier,
        label = {
            Text(text = label)
        },
        keyboardOptions = keyboardOptions,
    )
}


fun getPasswordVisualTransformation(showValue: Boolean): VisualTransformation {
    return if (showValue) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }
}