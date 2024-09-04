package com.example.coffeeshop.ui.navigation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.coffeeshop.AuthenticationStatus
import com.example.coffeeshop.AuthenticationViewModel
import com.example.coffeeshop.ui.screen.HomePage
import com.example.coffeeshop.ui.screen.login.AuthState
import com.example.coffeeshop.ui.screen.login.LogInScreen
import com.example.coffeeshop.ui.screen.login.LogInViewModel
import com.example.coffeeshop.ui.screen.signup.AccountStatus
import com.example.coffeeshop.ui.screen.signup.SignUpScreen
import com.example.coffeeshop.ui.screen.signup.SignUpViewModel
import kotlinx.serialization.Serializable


@SuppressLint("RestrictedApi")
@Composable
fun Navigation(
    modifier: Modifier,
    navController: NavHostController,
) {
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val authenticationUiState by authenticationViewModel.authenticationUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current


    LaunchedEffect(authenticationUiState.authenticationStatus) {
        when(authenticationUiState.authenticationStatus){
            AuthenticationStatus.UnAuthenticated ->{
                authenticationViewModel.updateDestination(CurrentDestination.LogInPage)
            }
            AuthenticationStatus.Authenticated -> {
                authenticationViewModel.updateDestination(CurrentDestination.HomePage)
            }
        }
    }

    NavHost(
        navController = navController, startDestination = authenticationUiState.startDestination,
        modifier = modifier
    ) {
        composable<CurrentDestination.LogInPage> {
            Log.d("BackStack",navController.currentBackStack.value.toString())
            val logInViewModel = hiltViewModel<LogInViewModel>()
            val logInUiState by logInViewModel.logInUiState.collectAsStateWithLifecycle()

            Log.d("BackStackEntry", navController.currentBackStack.value.toString())

            LaunchedEffect(logInViewModel.sharedFlow) {
                logInViewModel.sharedFlow.collect { error ->
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
            }

            LaunchedEffect(logInUiState.authState) {
                when(logInUiState.authState){
                    AuthState.LoggedIn -> navController.navigate(CurrentDestination.HomePage){
                        //remove LogInPage from backstackentry
                        popUpTo(CurrentDestination.LogInPage){
                            inclusive = true
                        }
                    }
                    else -> {}
                }
            }

            LogInScreen(
                modifier = modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                textPage = "Login Page",
                emailValue = logInUiState.emailValue,
                onEmailChange = { newEmail ->
                    logInViewModel.setEmail(newEmail)
                },
                imageVector = logInViewModel.getIconVisibility(),
                onIconClick = logInViewModel::setShowPassword,
                showPassword = logInUiState.showPassword,
                passwordValue = logInUiState.passwordValue,
                onPasswordChange = { newPassword ->
                    logInViewModel.setPassword(newPassword)
                },
                "LogIn",
                "Don't have an account, SignUp!",
                enabled = logInUiState.authState != AuthState.Loading,
                onLogInClick = { email, password ->
                    logInViewModel.logIn(email, password)
                },
                onSignUpClick = {
                    navController.navigate(CurrentDestination.SignUpPage)
                }
            )
        }

        composable<CurrentDestination.SignUpPage> {
            Log.d("BackStack",navController.currentBackStack.value.toString())
            val signUpViewModel = hiltViewModel<SignUpViewModel>()
            val signUpUiState by signUpViewModel.signupUiState.collectAsStateWithLifecycle()

            Log.d("BackStackEntry", navController.currentBackStack.value.toString())

            LaunchedEffect(signUpViewModel.signUpSharedFlow) {
                signUpViewModel.signUpSharedFlow.collect { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }

            LaunchedEffect(signUpUiState.accountStatus) {
                when (signUpUiState.accountStatus) {
                    is AccountStatus.IsCreated -> navController.navigate(CurrentDestination.LogInPage){
                        //inclusive = true to remove SignUpPage from backstackentry
                        popUpTo(CurrentDestination.LogInPage){
                            inclusive = true
                        }
                    }
                    else -> {}
                }
            }

            SignUpScreen(
                modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                textPage = "SignUp Page",
                emailValue = signUpUiState.email,
                onEmailChange = { newEmail ->
                    signUpViewModel.setEmail(newEmail)
                },
                imageVector = signUpViewModel.getIconVisibility(),
                onIconClick = signUpViewModel::setShowPassword,
                showPassword = signUpUiState.showPassword,
                passwordValue = signUpUiState.password,
                onPasswordChange = { newPassword ->
                    signUpViewModel.setPassword(newPassword)
                },
                buttonText = "Create Account",
                accountTextButton = "Already have an account? Login!",
                enabled = signUpUiState.accountStatus != AccountStatus.Loading,
                onCreateAccount = { email, password ->
                    signUpViewModel.signUp(email, password)
                },
                onExistingAccount = {
                    navController.navigateUp()
                }
            )
        }
        composable<CurrentDestination.HomePage> {
            Log.d("BackStack",navController.currentBackStack.value.toString())
            rememberCoroutineScope()
            Log.d("BackStackEntry", navController.currentBackStack.value.toString())
            HomePage(modifier = Modifier.fillMaxSize(), username = "userName.toString") {
                authenticationViewModel.signOut()
                navController.navigate(CurrentDestination.LogInPage){
                    popUpTo(0){
                        inclusive = true
                    }
                }
            }
        }
    }
}

@Serializable
sealed interface CurrentDestination{
    @Serializable
    data object LogInPage: CurrentDestination

    @Serializable
    data object SignUpPage: CurrentDestination

    @Serializable
    data object HomePage: CurrentDestination

}

