@file:Suppress("NAME_SHADOWING")

package com.example.coffeeshop.ui.navigation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.ui.screen.AuthenticationViewModel
import com.example.coffeeshop.ui.screen.SignOutResponse
import com.example.coffeeshop.ui.screen.categoryItemPage.CategoryItemPage
import com.example.coffeeshop.ui.screen.categoryItemPage.CategoryItemViewModel
import com.example.coffeeshop.ui.screen.homepage.HomePage
import com.example.coffeeshop.ui.screen.homepage.HomePageViewModel
import com.example.coffeeshop.ui.screen.login.AuthState
import com.example.coffeeshop.ui.screen.login.LogInScreen
import com.example.coffeeshop.ui.screen.login.LogInViewModel
import com.example.coffeeshop.ui.screen.myorderspage.MyOrdersPage
import com.example.coffeeshop.ui.screen.offerItemPage.OfferItemPage
import com.example.coffeeshop.ui.screen.profilepage.ProfilePage
import com.example.coffeeshop.ui.screen.settingspage.SettingsPage
import com.example.coffeeshop.ui.screen.shoppingcartpage.ShoppingCartPage
import com.example.coffeeshop.ui.screen.signup.AccountStatus
import com.example.coffeeshop.ui.screen.signup.SignUpScreen
import com.example.coffeeshop.ui.screen.signup.SignUpViewModel
import com.example.coffeeshop.ui.util.CustomNavType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf


@SuppressLint("RestrictedApi")
@Composable
fun Navigation(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: CurrentDestination,
) {
    val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
    val authenticationUiState by authenticationViewModel.authenticationUiState.collectAsStateWithLifecycle()
    val signOut = authenticationUiState.signOut

    val currentUsername = authenticationUiState.username


    val context = LocalContext.current

    LaunchedEffect(signOut) {
        when (signOut) {
            SignOutResponse.Success -> {
                authenticationViewModel.resetSignOutState()
                navController.navigate(CurrentDestination.LogInPage) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }

            else -> {}
        }
    }

    LaunchedEffect(authenticationViewModel.showError) {
        authenticationViewModel.showError.collectLatest { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
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
        composable<CurrentDestination.LogInPage> {
            val logInViewModel = hiltViewModel<LogInViewModel>()
            val logInUiState by logInViewModel.logInUiState.collectAsStateWithLifecycle()

            val scope = rememberCoroutineScope()

            Log.d("BackStackEntry", navController.currentBackStack.value.toString())

            LaunchedEffect(logInViewModel.sharedFlow) {
                logInViewModel.sharedFlow.collect { error ->
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
            }
            LaunchedEffect(logInUiState.authState) {
                when (logInUiState.authState) {
                    AuthState.LoggedIn -> {
                        //update currentUsername before navigating
                        authenticationViewModel.updateCurrentUserName()
                        navController.navigate(CurrentDestination.HomePage) {
                            //remove LogInPage from currentBackStack
                            popUpTo(CurrentDestination.LogInPage) {
                                inclusive = true
                            }
                        }
                    }

                    else -> {}
                }
            }
            LogInScreen(modifier = Modifier.fillMaxSize(),
                textPage = stringResource(R.string.welcome_back),
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
                logInEnabled = logInUiState.authState == AuthState.NotLoggedIn,
                signUpEnabled = logInUiState.authState == AuthState.NotLoggedIn,
                isLoading = logInUiState.isLoading,
                onLogInClick = { email, password ->
                    scope.launch {
                        logInViewModel.logIn(email, password)
                    }
                },
                onSignUpClick = {
                    navController.navigate(CurrentDestination.SignUpPage)
                })
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
                    is AccountStatus.IsCreated -> navController.navigate(CurrentDestination.LogInPage)
                    else -> {}
                }
            }

            //set delay for signup button to avoid spamming it
            val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
            DisposableEffect(key1 = lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> signUpViewModel.setAlreadyHaveAccountButton()
                        else -> {}
                    }
                }

                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
            SignUpScreen(Modifier.fillMaxSize(),
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
                createAccountEnabled = signUpUiState.accountStatus == AccountStatus.NotCreated,
                alreadyExistingEnabled = signUpUiState.alreadyHaveAccountButton && (signUpUiState.accountStatus == AccountStatus.NotCreated),
                onCreateAccount = { email, password ->
                    signUpViewModel.signUp(email, password)
                },
                onExistingAccount = {
                    navController.navigateUp()
                })
        }
        composable<CurrentDestination.HomePage> {
            val homePageViewModel = hiltViewModel<HomePageViewModel>()
            val homePageUiState by homePageViewModel.homePageUiState.collectAsStateWithLifecycle()

            LaunchedEffect(homePageViewModel.responseError) {
                homePageViewModel.responseError.collect { error ->
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
            }

            Log.d("BackStack", navController.currentBackStack.value.toString())
            HomePage(modifier = Modifier
                .fillMaxSize(),
                isLoading = homePageUiState.isLoading,
                searchText = homePageUiState.searchText,
                onClear = homePageViewModel::onClearSearchText,
                onSearch = { newText ->
                    homePageViewModel.onSearchTextChange(newText)
                },
                username = currentUsername,
                categoriesKey = homePageUiState.categoriesKey,
                onCategoryClick = { newKey ->
                    homePageViewModel.setCurrentCategory(newKey)
                },
                currentCategory = homePageUiState.filteredCategoryList,
                onFirstSeeAllClick = { categoryItemsList ->
                },
                onItemClick = { categoryItems ->
                    navController.navigate(
                        CurrentDestination.CategoryItemPage(
                            categoryItems = categoryItems,
                        )
                    )
                },
                offersList = homePageUiState.filteredOffersList,
                onSecondSeeAllClick = { offersList ->
                },
                onOffersClick = { offers ->
                    navController.navigate(
                        CurrentDestination.OfferItemPage
                    )
                })
        }

        composable<CurrentDestination.CategoryItemPage>(
            typeMap = mapOf(
                typeOf<CategoryItems>() to CustomNavType.categoryItems,
            )
        ) { entry ->
            val args = entry.toRoute<CurrentDestination.CategoryItemPage>()
            val categoryItems = args.categoryItems

            val categoryItemViewModel = hiltViewModel<CategoryItemViewModel>()
            val categoryItemUiState by categoryItemViewModel.categoryItemUiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                categoryItemViewModel.setCountValues(price = categoryItems.price.toInt())
            }


            CategoryItemPage(
                modifier = Modifier.fillMaxSize(),
                categoryItems = categoryItems,
                count = categoryItemUiState.count,
                totalPrice = categoryItemUiState.total,
                onCountRemove = {
                    categoryItemViewModel.decreaseCount(categoryItemUiState.count)
                },
                onCountAdd = {
                    categoryItemViewModel.increaseCount(categoryItemUiState.count)
                },
                addToCard = { count, categoryItems ->
                    navController.navigate(
                        CurrentDestination.ShoppingCartPage(
                            count = count,
                            categoryItems = categoryItems,
                        )
                    ) {
                        popUpTo(CurrentDestination.CategoryItemPage) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<CurrentDestination.OfferItemPage> {
            OfferItemPage(modifier = Modifier.fillMaxSize())
        }

        composable<CurrentDestination.SettingsPage> {
            SettingsPage(modifier = Modifier.fillMaxSize())
        }

        composable<CurrentDestination.ProfilePage> {
            ProfilePage(modifier = Modifier.fillMaxSize())
        }

        composable<CurrentDestination.ShoppingCartPage>(
            typeMap = mapOf(
                typeOf<CategoryItems>() to CustomNavType.categoryItems
            )
        ) {
            ShoppingCartPage(modifier = Modifier.fillMaxSize())
        }

        composable<CurrentDestination.MyOrders> {
            MyOrdersPage(modifier = Modifier.fillMaxSize())
        }
    }
}

@Serializable
sealed interface CurrentDestination {
    @Serializable
    data object LogInPage : CurrentDestination {
        const val ROUTE = "LogInPage"
    }

    @Serializable
    data object SignUpPage : CurrentDestination {
        const val ROUTE = "SignUpPage"
    }

    @Serializable
    data object HomePage : CurrentDestination {
        const val ROUTE = "HomePage"
    }

    @Serializable
    data class CategoryItemPage(val categoryItems: CategoryItems) :
        CurrentDestination {
        companion object {
            const val ROUTE = "Category Item Details"
        }
    }

    @Serializable
    data object OfferItemPage : CurrentDestination

    @Serializable
    data object ProfilePage : CurrentDestination {
        const val ROUTE = "ProfilePage"
    }

    @Serializable
    data class ShoppingCartPage(
        val count: Int,
        val categoryItems: CategoryItems,
    ) : CurrentDestination {
        companion object {
            const val ROUTE = "ShoppingCartPage"
        }
    }

    @Serializable
    data object SettingsPage : CurrentDestination {
        const val ROUTE = "SettingsPage"
    }

    @Serializable
    data object MyOrders : CurrentDestination {
        const val ROUTE = "MyOrders"
    }

    @Serializable
    data object Loading : CurrentDestination
}

