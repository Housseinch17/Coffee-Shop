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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.data.model.offers.Offers
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart
import com.example.coffeeshop.data.model.shoppingCart.OfferCart
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
import com.example.coffeeshop.ui.screen.myorderspage.MyOrdersViewModel
import com.example.coffeeshop.ui.screen.offerItemPage.OfferItemPage
import com.example.coffeeshop.ui.screen.offerItemPage.OfferItemViewModel
import com.example.coffeeshop.ui.screen.profilepage.ProfilePage
import com.example.coffeeshop.ui.screen.profilepage.ProfileViewModel
import com.example.coffeeshop.ui.screen.settingspage.PasswordChangement
import com.example.coffeeshop.ui.screen.settingspage.SettingsPage
import com.example.coffeeshop.ui.screen.settingspage.SettingsViewModel
import com.example.coffeeshop.ui.screen.shoppingcartpage.ShoppingCartPage
import com.example.coffeeshop.ui.screen.shoppingcartpage.ShoppingCartViewModel
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

    Log.d("MyTag", currentUsername.toString())

    //avoid initializing viewmodel everytime i navigate to shopping cart
    //instead of using local data to save the current orders of shopping cart
    val shoppingCartViewModel = hiltViewModel<ShoppingCartViewModel>()

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
            Log.d("BackStack", navController.currentBackStack.value.toString())
            val logInViewModel = hiltViewModel<LogInViewModel>()
            val logInUiState by logInViewModel.logInUiState.collectAsStateWithLifecycle()

            val scope = rememberCoroutineScope()

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


            LaunchedEffect(signUpViewModel.signUpSharedFlow) {
                signUpViewModel.signUpSharedFlow.collect { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }

            LaunchedEffect(signUpUiState.accountStatus) {
                Log.d("MyTag", "launch")
                when (signUpUiState.accountStatus) {
                    is AccountStatus.IsCreated -> {
                        Log.d("MyTag", "IsCreated")
                        navController.navigate(CurrentDestination.LogInPage)
                    }

                    else -> {
                        Log.d(
                            "MyTag", signUpUiState.accountStatus.toString()
                        )
                    }
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
                createAccountEnabled = signUpUiState.accountStatus != AccountStatus.isLoading,
                alreadyExistingEnabled = signUpUiState.alreadyHaveAccountButton && (signUpUiState.accountStatus != AccountStatus.isLoading),
                onCreateAccount = { email, password ->
                    signUpViewModel.signUp(email, password)
                },
                onExistingAccount = {
                    navController.navigateUp()
                })
        }
        composable<CurrentDestination.HomePage> {
            Log.d("BackStack", navController.currentBackStack.value.toString())
            val homePageViewModel = hiltViewModel<HomePageViewModel>()
            val homePageUiState by homePageViewModel.homePageUiState.collectAsStateWithLifecycle()

            LaunchedEffect(homePageViewModel.responseError) {
                homePageViewModel.responseError.collect { error ->
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
            }
            HomePage(modifier = Modifier.fillMaxSize(),
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
                        CurrentDestination.CategoryItemPage(categoryItems = categoryItems)
                    ) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                offersList = homePageUiState.filteredOffersList,
                onSecondSeeAllClick = { offersList ->

                },
                onOffersClick = { offers ->
                    navController.navigate(
                        CurrentDestination.OfferItemPage(offers = offers)
                    ) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                })
        }

        composable<CurrentDestination.CategoryItemPage>(
            typeMap = mapOf(
                typeOf<CategoryItems>() to CustomNavType.categoryItems,
            )
        ) { entry ->
            Log.d("BackStack", navController.currentBackStack.value.toString())
            val args = entry.toRoute<CurrentDestination.CategoryItemPage>()
            val categoryItems = args.categoryItems

            val categoryItemViewModel = hiltViewModel<CategoryItemViewModel>()
            val categoryItemUiState by categoryItemViewModel.categoryItemUiState.collectAsStateWithLifecycle()


            LaunchedEffect(Unit) {
                categoryItemViewModel.setCategoryItems(categoryItems)
                categoryItemViewModel.setCountAndTotalFirstValues(price = categoryItems.price)
            }

            CategoryItemPage(modifier = Modifier.fillMaxSize(),
                categoryItemsCart = categoryItemUiState.categoryItemsCart,
                onCountRemove = {
                    categoryItemViewModel.decreaseCount(categoryItemUiState.categoryItemsCart.count)
                },
                onCountAdd = {
                    categoryItemViewModel.increaseCount(categoryItemUiState.categoryItemsCart.count)
                },
                addToCard = { categoryItemCart ->
                    navController.navigate(
                        CurrentDestination.ShoppingCartPage(
                            categoryItemsCart = categoryItemCart, offerCart = OfferCart()
                        )
                    ) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                })
        }

        //Offer Item Page might be similar to CategoryItemPage but it was added for later use
        //Offers data class structure might change which lead to error having single page for
        //both CategoryItems and OffersItem
        composable<CurrentDestination.OfferItemPage>(
            typeMap = mapOf(
                typeOf<Offers>() to CustomNavType.offers
            )
        ) { entry ->
            Log.d("BackStack", navController.currentBackStack.value.toString())
            val args = entry.toRoute<CurrentDestination.OfferItemPage>()
            val offer = args.offers

            val offerItemViewModel = hiltViewModel<OfferItemViewModel>()
            val offerItemUiState by offerItemViewModel.offerItemUiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                offerItemViewModel.setOffer(offer)
                offerItemViewModel.setCountAndTotalFirstValues(price = offer.price)
            }

            OfferItemPage(modifier = Modifier.fillMaxSize(),
                offerCart = offerItemUiState.offerCart,
                onCountRemove = {
                    offerItemViewModel.decreaseCount(offerItemUiState.offerCart.count)
                },
                onCountAdd = {
                    offerItemViewModel.increaseCount(offerItemUiState.offerCart.count)
                },
                addToCard = { offerCart ->
                    navController.navigate(
                        CurrentDestination.ShoppingCartPage(
                            categoryItemsCart = CategoryItemsCart(), offerCart = offerCart
                        )
                    ) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                })
        }


        composable<CurrentDestination.ShoppingCartPage>(
            typeMap = mapOf(
                typeOf<OfferCart>() to CustomNavType.offerCart,
                typeOf<CategoryItemsCart>() to CustomNavType.categoryItemsCart
            )
        ) { entry ->
            Log.d("BackStack", navController.currentBackStack.value.toString())
            val args = entry.toRoute<CurrentDestination.ShoppingCartPage>()
            val categoryItemsCart = args.categoryItemsCart
            val offerCart = args.offerCart

            val shoppingCartUiState by shoppingCartViewModel.shoppingCartUiState.collectAsStateWithLifecycle()

            LaunchedEffect(categoryItemsCart, offerCart) {
                shoppingCartViewModel.updateShoppingCartLists(
                    categoryItemsCart = categoryItemsCart, offerCart = offerCart
                )
            }


            ShoppingCartPage(modifier = Modifier.fillMaxSize(),
                shoppingCart = shoppingCartUiState.shoppingCart,
                onCategoryCountDecrease = { index, categoryItemCart ->
                    shoppingCartViewModel.onCategoryCountDecrease(index, categoryItemCart)
                },
                onCategoryCountIncrease = { index, categoryItemCart ->
                    shoppingCartViewModel.onCategoryCountIncrease(index, categoryItemCart)
                },
                onOfferCountDecrease = { index, offerDecrease ->
                    shoppingCartViewModel.onOfferCountDecrease(index, offerDecrease)
                },

                onOfferCountIncrease = { index, offerIncrease ->
                    shoppingCartViewModel.onOfferCountIncrease(index, offerIncrease)
                },
                onCheckOut = {
                    val currentShoppingCart = shoppingCartUiState.shoppingCart
                    shoppingCartViewModel.saveShoppingCartItems(shoppingCart = currentShoppingCart)
                })
        }


        composable<CurrentDestination.SettingsPage> {
            Log.d("BackStack", navController.currentBackStack.value.toString())
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()

            LaunchedEffect(settingsViewModel.emitValue) {
                settingsViewModel.emitValue.collectLatest { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            SettingsPage(
                modifier = Modifier.fillMaxSize(),
                newPasswordValue = settingsUiState.newPasswordValue,
                confirmPasswordValue = settingsUiState.confirmNewPasswordValue,
                newPasswordValueChange = { newPassword ->
                    settingsViewModel.newPasswordValueChange(newPassword)
                },
                confirmPasswordValueChange = { confirmPassword ->
                    settingsViewModel.confirmNewPasswordValueChange(confirmPassword)
                },
                imageVector = settingsViewModel.getIconVisibility(
                    settingsUiState.showPassword
                ),
                confirmImageVector = settingsViewModel.getIconVisibility(settingsUiState.confirmShowPassword),
                onIconClick = settingsViewModel::setShowPassword,
                confirmOnIconClick = settingsViewModel::setConfirmShowPassword,
                showPassword = settingsUiState.showPassword,
                confirmShowPassword = settingsUiState.confirmShowPassword,
                showText = settingsUiState.showText,
                confirmShowText = settingsUiState.confirmShowText,
                onPasswordChange = { newPassword, confirmPassword ->
                    settingsViewModel.changePassword(
                        email = currentUsername!!,
                        newPassword = newPassword,
                        confirmNewPassword = confirmPassword
                    )
                },
                onResetPassword = settingsViewModel::resetShowDialog,
                resetShowDialog = settingsUiState.resetShowDialog,
                resetPassword = settingsViewModel::resetPassword,
                resetDismiss = settingsViewModel::resetHideDialog,
                resetIsLoading = settingsUiState.resetPassword == PasswordChangement.isLoading,
                onSignOut = authenticationViewModel::resetShowDialog,
                signOutShowDialog = authenticationUiState.signOutShowDialog,
                signOutConfirm = authenticationViewModel::signOut,
                signOutDismiss = authenticationViewModel::resetHideDialog,
                signOutIsLoading = authenticationUiState.signOut == SignOutResponse.isLoading,
            )
        }

        composable<CurrentDestination.ProfilePage> {
            Log.d("BackStack", navController.currentBackStack.value.toString())
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            ProfilePage(modifier = Modifier.fillMaxSize())
        }

        composable<CurrentDestination.MyOrders> {
            Log.d("BackStack", navController.currentBackStack.value.toString())
            val myOrdersViewModel = hiltViewModel<MyOrdersViewModel>()
            val myOrdersUiState by myOrdersViewModel.myOrdersUiState.collectAsStateWithLifecycle()

            MyOrdersPage(
                modifier = Modifier.fillMaxSize(),
                myOrders = myOrdersUiState.myOrdersUiState
            )
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
    data class CategoryItemPage(val categoryItems: CategoryItems = CategoryItems()) :
        CurrentDestination {
        companion object {
            const val ROUTE = "CategoryItemPage"
        }
    }

    @Serializable
    data class OfferItemPage(val offers: Offers = Offers()) : CurrentDestination {
        companion object {
            const val ROUTE = "OfferItemPage"
        }
    }

    @Serializable
    data object ProfilePage : CurrentDestination {
        const val ROUTE = "ProfilePage"
    }

    @Serializable
    data class ShoppingCartPage(
        val categoryItemsCart: CategoryItemsCart = CategoryItemsCart(),
        val offerCart: OfferCart = OfferCart(),
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

