package com.example.coffeeshop.ui.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachEmail
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.coffeeshop.R
import com.example.coffeeshop.ui.theme.Orange

@Composable
fun ShimmerEffect(
    modifier: Modifier,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
    color: Color = Color.White,
) {
    val shimmerColors = listOf(
        color.copy(alpha = 0.3f),  // Light grey
        color.copy(alpha = 0.5f),  // Slightly more opaque
        color.copy(alpha = 0.7f),  // Darker grey
        color.copy(alpha = 0.5f),
        color.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
    )

    Box(modifier = modifier) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush)
        )
    }
}

@Composable
fun CoffeeImage(modifier: Modifier, imageUrl: String?) {
    if (imageUrl != null) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .placeholder(R.drawable.loading)
                .error(R.drawable.connectionerror)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}


@Composable
fun TrailingIcon(imageVector: ImageVector, onIconClick: () -> Unit) {
    IconButton(onClick = onIconClick, modifier = Modifier) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(R.string.visibility),
            tint = Color.White
        )
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
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        onClick = onSignUpClick,
        enabled = textButtonEnabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.White
        )
    ) {
        if (textButtonEnabled) {
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        } else {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(30.dp), // Adjust size as needed
                color = Color.White, // Set the color for the progress bar
                strokeWidth = 2.dp
            )
        }
    }
}

@Composable
fun AccountButton(
    modifier: Modifier,
    text: String,
    buttonEnabled: Boolean,
    onLogInClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onLogInClick,
        enabled = buttonEnabled,
        colors = ButtonDefaults.buttonColors(containerColor = Orange)
    ) {
        if (buttonEnabled) {
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        } else {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(30.dp), // Adjust size as needed
                color = Color.White, // Set the color for the progress bar
                strokeWidth = 2.dp
            )
        }
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
        textStyle = TextStyle.Default.copy(
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Orange,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        singleLine = true,
        modifier = modifier,
        label = {
            Text(text = label, color = Color.White)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock, contentDescription = null,
                tint = Color.White
            )
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,

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
        textStyle = TextStyle.Default.copy(
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Orange,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        singleLine = true,
        modifier = modifier,
        label = {
            Text(text = label, color = Color.White)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.AttachEmail, contentDescription = null,
                tint = Color.White
            )
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


@Composable
fun ShowDialog(showDialog: Boolean, error: String, onDismissButton: () -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            dismissButton = {
                Button(onClick = onDismissButton) {
                    Text("Dismiss", color = Orange)
                }
            },
            modifier = Modifier
                .size(200.dp)
                .background(Color.White)
                .clip(RoundedCornerShape(25.dp)),
            title = {
                Text(
                    "No internet connection",
                    style = MaterialTheme.typography.titleLarge.copy(color = Orange)
                )
            },
            text = {
                Text(
                    text = error,
                    style = MaterialTheme.typography.titleMedium.copy(Color.Black)
                )
            }
        )
    }
}

