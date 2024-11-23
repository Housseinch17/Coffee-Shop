package com.example.coffeeshop.ui.navigation.navGraphBuilder

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.data.model.offers.Offers
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart
import com.example.coffeeshop.data.model.shoppingCart.OfferCart
import com.example.coffeeshop.ui.navigation.NavigationScreens
import com.example.coffeeshop.ui.screen.AuthenticationViewModel
import com.example.coffeeshop.ui.screen.ResetPage
import com.example.coffeeshop.ui.screen.allCategoriesDetail.AllCategoriesPage
import com.example.coffeeshop.ui.screen.allCategoriesDetail.AllCategoriesViewModel
import com.example.coffeeshop.ui.screen.allOffersDetail.AllOffersPage
import com.example.coffeeshop.ui.screen.allOffersDetail.AllOffersViewModel
import com.example.coffeeshop.ui.screen.categoryItemPage.CategoryItemPage
import com.example.coffeeshop.ui.screen.categoryItemPage.CategoryItemViewModel
import com.example.coffeeshop.ui.screen.homepage.HomeBottomPageState
import com.example.coffeeshop.ui.screen.homepage.HomeCenterPageState
import com.example.coffeeshop.ui.screen.homepage.HomeEvents
import com.example.coffeeshop.ui.screen.homepage.HomePage
import com.example.coffeeshop.ui.screen.homepage.HomePageViewModel
import com.example.coffeeshop.ui.screen.homepage.HomeTopPageState
import com.example.coffeeshop.ui.screen.myorderspage.MyOrdersPage
import com.example.coffeeshop.ui.screen.myorderspage.MyOrdersViewModel
import com.example.coffeeshop.ui.screen.myorderspage.OrderStatus
import com.example.coffeeshop.ui.screen.offerItemPage.OfferItemPage
import com.example.coffeeshop.ui.screen.offerItemPage.OfferItemViewModel
import com.example.coffeeshop.ui.screen.profilepage.ProfilePage
import com.example.coffeeshop.ui.screen.profilepage.ProfileViewModel
import com.example.coffeeshop.ui.screen.settingspage.SettingsPage
import com.example.coffeeshop.ui.screen.settingspage.SettingsViewModel
import com.example.coffeeshop.ui.screen.shoppingcartpage.ShoppingCartPage
import com.example.coffeeshop.ui.screen.shoppingcartpage.ShoppingCartViewModel
import com.example.coffeeshop.ui.util.CustomNavType
import com.example.coffeeshop.ui.util.navigateSingleTopTo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

fun NavGraphBuilder.coffeeShop(
    navHostController: NavHostController,
    authenticationViewModel: AuthenticationViewModel,
    username: String,
    resetShowDialog: Boolean,
    resetIsLoading: Boolean,
    signOutShowDialog: Boolean,
    signOutIsLoading: Boolean
) {
    navigation<NavigationScreens.CoffeeShop>(
        startDestination = NavigationScreens.HomePage
    ) {
        composable<NavigationScreens.HomePage> {
            Log.d("BackStack","${navHostController.currentBackStackEntry}")
            val context = LocalContext.current
            val parentBackStackEntry: NavBackStackEntry =
                navHostController.getBackStackEntry(NavigationScreens.CoffeeShop)
            val homePageViewModel = hiltViewModel<HomePageViewModel>(parentBackStackEntry)
            val homePageUiState by homePageViewModel.homePageUiState.collectAsStateWithLifecycle()

            val scope = rememberCoroutineScope()

            LaunchedEffect(homePageViewModel.responseError) {
                homePageViewModel.responseError.collect { error ->
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
            }

            HomePage(
                modifier = Modifier.fillMaxSize(),
                isLoading = homePageUiState.isLoading,
                seeAllClicked = homePageUiState.seeAllClicked,
                topPageState = HomeTopPageState(
                    searchText = homePageUiState.searchText,
                    username = username,
                    onClear = homePageViewModel::onClearSearchText,
                    onSearch = { newText ->
                        homePageViewModel.onSearchTextChange(newText)
                    },
                ),
                centerPageState = HomeCenterPageState(
                    categoriesKey = homePageUiState.categoriesKey,
                    onCategoryClick = { newKey ->
                        homePageViewModel.setCurrentCategory(newKey)
                    },
                    currentCategory = homePageUiState.filteredCategoryList,
                    onFirstSeeAllClick = { categoryItemsList ->
                        scope.launch {
                            homePageViewModel.homeEvents(
                                HomeEvents.OnSeeAllClick(
                                    navHostController = navHostController,
                                    route = NavigationScreens.AllCategories(categoryItemsList)
                                )
                            )
                        }
                    },
                    onItemClick = { categoryItems ->
                        navHostController.navigateSingleTopTo(
                            NavigationScreens.CategoryItemPage(categoryItems = categoryItems),
                            navHostController = navHostController
                        )
                    }
                ),
                bottomPageState = HomeBottomPageState(
                    offersList = homePageUiState.filteredOffersList,
                    onSecondSeeAllClick = { offersList ->
                        scope.launch {
                            homePageViewModel.homeEvents(
                                HomeEvents.OnSeeAllClick(
                                    navHostController = navHostController,
                                    route = NavigationScreens.AllOffers(
                                        allOffers = offersList
                                    )
                                )
                            )
                        }
                    },
                    onOffersClick = { offers ->
                        navHostController.navigateSingleTopTo(
                            NavigationScreens.OfferItemPage(offers = offers),
                            navHostController = navHostController
                        )
                    }
                ),
                isRefreshing = homePageUiState.isRefreshing,
                onRefresh = homePageViewModel::refreshData
            )
        }

        composable<NavigationScreens.AllCategories>(
            typeMap = mapOf(
                typeOf<List<CategoryItems>>() to CustomNavType.allCategories
            )
        ) { entry ->
            Log.d("BackStack","${navHostController.currentBackStackEntry}")
            val args = entry.toRoute<NavigationScreens.AllCategories>()
            val allCategories = args.allCategories

            val allCategoriesViewModel = hiltViewModel<AllCategoriesViewModel>()
            val allCategoriesUiState by allCategoriesViewModel.allCategoriesUiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                allCategoriesViewModel.setData(allCategories)
            }

            AllCategoriesPage(modifier = Modifier.fillMaxSize(),
                isLoading = allCategoriesUiState.isLoading,
                categoriesList = allCategoriesUiState.allCategoriesList,
                onItemClick = { categoryItem ->
                    navHostController.navigateSingleTopTo(
                        NavigationScreens.CategoryItemPage(
                            categoryItems = categoryItem
                        ), navHostController = navHostController
                    )
                })
        }

        composable<NavigationScreens.AllOffers>(
            typeMap = mapOf(
                typeOf<List<Offers>>() to CustomNavType.allOffers
            )
        ) { entry ->
            Log.d("BackStack","${navHostController.currentBackStackEntry}")
            val args = entry.toRoute<NavigationScreens.AllOffers>()
            val allOffers = args.allOffers

            val allOffersViewModel = hiltViewModel<AllOffersViewModel>()
            val allOffersUiState by allOffersViewModel.allOffersUiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                allOffersViewModel.setData(allOffers)
            }

            AllOffersPage(
                modifier = Modifier.fillMaxSize(),
                isLoading = allOffersUiState.isLoading,
                offersList = allOffersUiState.allOffersList,
                onItemClick = { offers ->
                    navHostController.navigateSingleTopTo(
                        NavigationScreens.OfferItemPage(
                            offers = offers
                        ),
                        navHostController = navHostController
                    )
                }
            )
        }

        composable<NavigationScreens.CategoryItemPage>(
            typeMap = mapOf(
                typeOf<CategoryItems>() to CustomNavType.categoryItems,
            )
        ) { entry ->
            Log.d("BackStack","${navHostController.currentBackStackEntry}")
            val args = entry.toRoute<NavigationScreens.CategoryItemPage>()
            val categoryItems = args.categoryItems

            val categoryItemViewModel = hiltViewModel<CategoryItemViewModel>()
            val categoryItemUiState by categoryItemViewModel.categoryItemUiState.collectAsStateWithLifecycle()


            LaunchedEffect(Unit) {
                categoryItemViewModel.setCategoryItems(categoryItems)
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
                    navHostController.navigate(
                        NavigationScreens.ShoppingCartPage(
                            categoryItemsCart = categoryItemCart, offerCart = OfferCart()
                        )
                    ) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                })
        }

        //Offer Item Page might be similar to CategoryItemPage but it was added for later use
        //Offers data class structure might change which lead to error having single page for
        //both CategoryItems and OffersItem
        composable<NavigationScreens.OfferItemPage>(
            typeMap = mapOf(
                typeOf<Offers>() to CustomNavType.offers
            )
        ) { entry ->
            Log.d("BackStack","${navHostController.currentBackStackEntry}")
            val args = entry.toRoute<NavigationScreens.OfferItemPage>()
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
                    navHostController.navigate(
                        NavigationScreens.ShoppingCartPage(
                            categoryItemsCart = CategoryItemsCart(), offerCart = offerCart
                        )
                    ) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                })
        }

        composable<NavigationScreens.ShoppingCartPage>(
            typeMap = mapOf(
                typeOf<OfferCart>() to CustomNavType.offerCart,
                typeOf<CategoryItemsCart>() to CustomNavType.categoryItemsCart
            )
        ) { entry ->
            val context = LocalContext.current
            Log.d("BackStack","${navHostController.currentBackStackEntry}")
            val args = entry.toRoute<NavigationScreens.ShoppingCartPage>()
            val categoryItemsCart = args.categoryItemsCart
            val offerCart = args.offerCart

            val parentBackStackEntry: NavBackStackEntry =
                navHostController.getBackStackEntry(NavigationScreens.CoffeeShop)

            //here if the shoppingCartViewModel already in BackStackEntry it will use the old one
            //if not it will create it
            val shoppingCartViewModel =
                hiltViewModel<ShoppingCartViewModel>(parentBackStackEntry)

            val shoppingCartUiState by shoppingCartViewModel.shoppingCartUiState.collectAsStateWithLifecycle()

            val scope = rememberCoroutineScope()

            LaunchedEffect(categoryItemsCart, offerCart) {
                shoppingCartViewModel.updateShoppingCartLists(
                    categoryItemsCart = categoryItemsCart, offerCart = offerCart
                )
            }

            LaunchedEffect(shoppingCartViewModel.shoppingCartSharedFlow) {
                shoppingCartViewModel.shoppingCartSharedFlow.collect { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
            ShoppingCartPage(modifier = Modifier.fillMaxSize(),
                checkOutEnabled = shoppingCartUiState.shoppingCart.totalPrice != 0.0,
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
                    scope.launch {
                        shoppingCartViewModel.saveShoppingCartItems(shoppingCart = currentShoppingCart)
                    }
                })
        }


        composable<NavigationScreens.SettingsPage> {
            Log.d("BackStack","${navHostController.currentBackStackEntry}")
            val context = LocalContext.current
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
                        email = username,
                        newPassword = newPassword,
                        confirmNewPassword = confirmPassword
                    )
                },
                resetShowDialog = resetShowDialog,
                resetPassword = {
                    Log.d("MyTag", "hey")
                    authenticationViewModel.resetPassword(
                        email = "",
                        resetPage = ResetPage.SettingsPage
                    )
                },
                resetDismiss = authenticationViewModel::resetResetHideDialog,
                resetIsLoading = resetIsLoading,
                onResetPassword = authenticationViewModel::resetResetShowDialog,
                onSignOut = authenticationViewModel::resetShowDialog,
                signOutShowDialog = signOutShowDialog,
                signOutConfirm = authenticationViewModel::signOut,
                signOutDismiss = authenticationViewModel::resetHideDialog,
                signOutIsLoading = signOutIsLoading,
            )
        }

        composable<NavigationScreens.ProfilePage> {
            Log.d("BackStack","${navHostController.currentBackStackEntry}")
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            ProfilePage(modifier = Modifier.fillMaxSize())
        }

        composable<NavigationScreens.MyOrders> {
            Log.d("BackStack","${navHostController.currentBackStackEntry}")
            val myOrdersViewModel = hiltViewModel<MyOrdersViewModel>()
            val myOrdersUiState by myOrdersViewModel.myOrdersUiState.collectAsStateWithLifecycle()

            MyOrdersPage(
                modifier = Modifier.fillMaxSize(),
                myOrdersList = myOrdersUiState.myOrdersList,
                onExpand = { index ->
                    myOrdersViewModel.setExpanded(index)
                },
                isLoading = myOrdersUiState.orderState == OrderStatus.IsLoading,
                isRefreshing = myOrdersUiState.isRefreshing,
                onRefresh = myOrdersViewModel::loadNewOrders
            )
        }
    }
}