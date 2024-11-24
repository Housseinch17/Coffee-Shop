package com.example.coffeeshop.ui.navigation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.coffeeshop.ui.navigation.navGraphBuilder.coffeeShop
import com.example.coffeeshop.ui.navigation.navGraphBuilder.registerGraph
import com.example.coffeeshop.ui.screen.AuthenticationViewModel
import com.example.coffeeshop.ui.screen.SignOutResponse
import com.example.coffeeshop.ui.screen.settingspage.PasswordChangement
import kotlinx.coroutines.flow.collectLatest


@SuppressLint("RestrictedApi")
@Composable
fun Navigation(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: NavigationScreens,
) {
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val authenticationUiState by authenticationViewModel.authenticationUiState.collectAsStateWithLifecycle()

    val signOut = authenticationUiState.signOut

    val currentUsername = authenticationUiState.username

    LaunchedEffect(Unit) {
        Log.d("MyTag", currentUsername.toString())
    }

    val context = LocalContext.current

    LaunchedEffect(signOut) {
        when (signOut) {
            SignOutResponse.Success -> {
                authenticationViewModel.resetSignOutState()
                navController.navigate(NavigationScreens.LogInPage) {
                    //popUpTo(0) here 0 means we will remove all the old stacks in BackStackEntry
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(authenticationViewModel.showMessage) {
        authenticationViewModel.showMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    NavHost(
        navController = navController, startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(1000)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(1000)
            )
        },
    ) {

        registerGraph(
            navHostController = navController,
            authenticationViewModel = authenticationViewModel,
            onResetEmailValue = authenticationUiState.resetEmailValue,
            resetShowDialog = authenticationUiState.resetShowDialog,
            email = authenticationUiState.resetEmailValue,
            resetIsLoading = authenticationUiState.resetPassword == PasswordChangement.IsLoading,
        )

        coffeeShop(
            navHostController = navController,
            authenticationViewModel = authenticationViewModel,
            username = currentUsername ?: "",
            resetShowDialog = authenticationUiState.resetShowDialog,
            resetIsLoading = authenticationUiState.resetPassword == PasswordChangement.IsLoading,
            signOutShowDialog = authenticationUiState.signOutShowDialog,
            signOutIsLoading = authenticationUiState.signOut == SignOutResponse.isLoading,
        )

    }
}


