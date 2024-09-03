package com.example.coffeeshop.ui.navigation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.coffeeshop.AuthState
import com.example.coffeeshop.ui.screen.HomePage
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
    val context = LocalContext.current


    NavHost(
        navController = navController, startDestination = LogInPage,
        modifier = modifier
    ) {
        composable<LogInPage> {
            val logInViewModel = hiltViewModel<LogInViewModel>()
            val logInUiState by logInViewModel.logInUiState.collectAsStateWithLifecycle()

            Log.d("MyTag", navController.currentBackStack.value.toString())

            LaunchedEffect(logInViewModel.sharedFlow) {
                logInViewModel.sharedFlow.collect { error ->
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
            }

            LaunchedEffect(logInUiState.authState) {
                when(logInUiState.authState){
                    AuthState.Authenticated -> navController.navigate(HomePage){
                        //remove LogInPage from backstackentry
                        popUpTo(LogInPage){
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
                    navController.navigate(SignUpPage)
                }
            )
        }

        composable<SignUpPage> {
            val signUpViewModel = hiltViewModel<SignUpViewModel>()
            val signUpUiState by signUpViewModel.signupUiState.collectAsStateWithLifecycle()

            Log.d("MyTag", navController.currentBackStack.value.toString())

            LaunchedEffect(signUpViewModel.signUpSharedFlow) {
                signUpViewModel.signUpSharedFlow.collect { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }

            LaunchedEffect(signUpUiState.accountStatus) {
                when (signUpUiState.accountStatus) {
                    is AccountStatus.IsCreated -> navController.navigate(LogInPage){
                        //inclusive = true to remove SignUpPage from backstackentry
                        popUpTo(LogInPage){
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
        composable<HomePage> {
            Log.d("MyTag", navController.currentBackStack.value.toString())
            HomePage(modifier = Modifier.fillMaxSize(), username = "userName.toString") {
            }
        }
    }
}


@Serializable
object LogInPage

@Serializable
object SignUpPage

@Serializable
object HomePage

