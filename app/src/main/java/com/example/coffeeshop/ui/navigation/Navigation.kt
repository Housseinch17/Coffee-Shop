package com.example.coffeeshop.ui.navigation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.coffeeshop.ui.screen.AuthenticationViewModel
import com.example.coffeeshop.ui.screen.homepage.HomePage
import com.example.coffeeshop.ui.screen.homepage.HomePageViewModel
import com.example.coffeeshop.ui.screen.login.AuthState
import com.example.coffeeshop.ui.screen.login.LogInScreen
import com.example.coffeeshop.ui.screen.login.LogInViewModel
import com.example.coffeeshop.ui.screen.signup.AccountStatus
import com.example.coffeeshop.ui.screen.signup.SignUpScreen
import com.example.coffeeshop.ui.screen.signup.SignUpViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@SuppressLint("RestrictedApi")
@Composable
fun Navigation(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: CurrentDestination,
) {
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val context = LocalContext.current

    NavHost(
        navController = navController, startDestination = startDestination,
        modifier = modifier
    ) {
        composable<CurrentDestination.LogInPage> {
            Log.d("BackStack", navController.currentBackStack.value.toString())
            val logInViewModel = hiltViewModel<LogInViewModel>()
            val logInUiState by logInViewModel.logInUiState.collectAsStateWithLifecycle()

            Log.d("BackStackEntry", navController.currentBackStack.value.toString())

            LaunchedEffect(logInViewModel.sharedFlow) {
                logInViewModel.sharedFlow.collect { error ->
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
            }

            LaunchedEffect(logInUiState.authState) {
                when (logInUiState.authState) {
                    AuthState.LoggedIn -> navController.navigate(CurrentDestination.HomePage) {
                        //remove LogInPage from currentBackStack
                        popUpTo(CurrentDestination.LogInPage) {
                            inclusive = true
                        }
                    }
                    else -> {}
                }
            }
            //set delay for signup button to avoid spamming it
            val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
            val scope = rememberCoroutineScope()
            DisposableEffect(key1 = lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> {
                            Log.d("currentScreen", "Resume")
                            scope.launch {
                                logInViewModel.setSignUpButton(false)
                                delay(500L)
                                logInViewModel.setSignUpButton(true)
                            }
                        }
                        Lifecycle.Event.ON_START -> Log.d("currentScreen","Start")
                        Lifecycle.Event.ON_CREATE -> Log.d("currentScreen","Create")
                        Lifecycle.Event.ON_PAUSE  -> Log.d("currentScreen","Pause")
                        Lifecycle.Event.ON_STOP -> Log.d("currentScreen","Stop")
                        Lifecycle.Event.ON_DESTROY -> Log.d("currentScreen","Destroy")
                        else -> Log.d("currentScreen","Else")
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
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
                logInEnabled = logInUiState.authState != AuthState.Loading,
                signUpEnabled = logInUiState.signUpEnabled,
                onLogInClick = { email, password ->
                    logInViewModel.logIn(email, password)
                },
                onSignUpClick = {
                    navController.navigate(CurrentDestination.SignUpPage)
                }
            )
        }

        composable<CurrentDestination.SignUpPage> {
            Log.d("BackStack", navController.currentBackStack.value.toString())
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
                    is AccountStatus.IsCreated -> navController.navigate(CurrentDestination.LogInPage) {
                        //inclusive = true to remove SignUpPage from currentBackStack
                        popUpTo(CurrentDestination.LogInPage) {
                            inclusive = true
                        }
                    }

                    else -> {}
                }
            }

            //set delay for signup button to avoid spamming it
            val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
            val scope = rememberCoroutineScope()
            DisposableEffect(key1 = lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_RESUME ->
                            scope.launch {
                                signUpViewModel.setAlreadyHaveAccountButton(false)
                                delay(500L)
                                signUpViewModel.setAlreadyHaveAccountButton(true)
                            }
                        else -> {}
                    }
                }

                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
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
                createAccountEnabled = signUpUiState.accountStatus != AccountStatus.Loading,
                alreadyExistingEnabled = signUpUiState.alreadyHaveAccountButton,
                onCreateAccount = { email, password ->
                    signUpViewModel.signUp(email, password)
                },
                onExistingAccount = {
                    navController.navigateUp()
                }
            )
        }
        composable<CurrentDestination.HomePage> {
            val homePageViewModel = hiltViewModel<HomePageViewModel>()
            val homePageUiState by homePageViewModel.homePageUiState.collectAsStateWithLifecycle()

            Log.d("BackStack", navController.currentBackStack.value.toString())
            HomePage(modifier = Modifier.fillMaxSize(), onClick = {
                Log.d("CheckResponse",homePageUiState.response.toString())
            }) {
                //sign out and navigate to login page and clear all backstack entry
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
sealed interface CurrentDestination {
    @Serializable
    data object LogInPage : CurrentDestination{
        const val ROUTE = "LogInPage"
    }

    @Serializable
    data object SignUpPage : CurrentDestination{
        const val ROUTE = "SignUpPage"
    }

    @Serializable
    data object HomePage : CurrentDestination

    @Serializable
    data object Loading : CurrentDestination
}

