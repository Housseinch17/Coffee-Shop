package com.example.coffeeshop.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomAppBar
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.FabPosition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.coffeeshop.ui.navigation.NavigationScreens
import com.example.coffeeshop.ui.navigation.Navigation
import com.example.coffeeshop.ui.theme.MatteBlack
import com.example.coffeeshop.ui.theme.Orange
import com.example.coffeeshop.ui.util.navigateSingleTopTo

@Composable
fun MainPage(navController: NavHostController) {
    //initialize viewmodel to check if already logged in via shared preferences
    val mainViewModel = hiltViewModel<MainViewModel>()
    val status by mainViewModel.status.collectAsStateWithLifecycle()


    val backStackEntry by navController.currentBackStackEntryAsState()
    //get current screen
    val currentScreenDestination = backStackEntry?.destination?.route
    //get current screen route without (com.example.coffee...)
    val currentScreenRoute = getScreenName(currentScreenDestination)

    Log.d("currentScreen", "current screen ${navController.currentDestination?.route}")
    Log.d("currentScreen", "current home ${NavigationScreens.CategoryItemPage}")


    //not hideBottomBar
    val showBottomBar = !(hideBottomBar(currentScreenRoute))

    //here if the status is NavigationScreens.Loading it will show the background image set in the theme
    if (status != NavigationScreens.Loading) {
        //use material not material 3 to use docked
        androidx.compose.material.Scaffold(
            floatingActionButton = {
                if (showBottomBar) {
                    //check if currentScreen is CategoryItemPage or OfferItemPage
                    val currentScreenIsItemPage: Boolean =
                        (navController.currentDestination?.route!!.startsWith("com.example.coffeeshop.ui.navigation.NavigationScreens.CategoryItemPage")
                                || navController.currentDestination?.route!!.startsWith("com.example.coffeeshop.ui.navigation.NavigationScreens.OfferItemPage"))
                    FloatingButtonBar(onShoppingCartClick = {
                        if(currentScreenIsItemPage){
                            navController.navigate(NavigationScreens.HomePage){
                                popUpTo(navController.currentDestination?.route!!){
                                    inclusive = true
                                }
                            }
                        } else {
                            navController.navigateSingleTopTo(
                                route = NavigationScreens.ShoppingCartPage(),
                                navController
                            )
                        }
                    })
                }
            },
            //to show it docked
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                if (showBottomBar) {
                    //check if currentScreen is CategoryItemPage or OfferItemPage
                    val currentScreenIsItemPage: Boolean = (navController.currentDestination?.route!!.startsWith("com.example.coffeeshop.ui.navigation.NavigationScreens.CategoryItemPage")
                            || navController.currentDestination?.route!!.startsWith("com.example.coffeeshop.ui.navigation.NavigationScreens.OfferItemPage"))

                    BottomAppBar(
                        backgroundColor = MatteBlack,
                        modifier = Modifier
                            .navigationBarsPadding()
                            .fillMaxWidth()
                            .height(56.dp),
                        //will cut floating to show white background behind it
                        cutoutShape = RoundedCornerShape(50),
                        content = {
                            BottomAppBar(
                                onHomeClick = {
                                    if(currentScreenIsItemPage){
                                        navController.navigate(NavigationScreens.HomePage){
                                            popUpTo(navController.currentDestination?.route!!){
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        navController.navigateSingleTopTo(
                                            route = NavigationScreens.HomePage,
                                            navController
                                        )
                                    }
                                },
                                onProfileClick = {
                                    if (currentScreenIsItemPage) {
                                        navController.navigate(NavigationScreens.ProfilePage){
                                            popUpTo(navController.currentDestination?.route!!){
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        navController.navigateSingleTopTo(
                                            route = NavigationScreens.ProfilePage,
                                            navController
                                        )
                                    }
                                },
                                onMyOrdersClick = {
                                    if (currentScreenIsItemPage) {
                                        navController.navigate(NavigationScreens.MyOrders){
                                            popUpTo(navController.currentDestination?.route!!){
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        navController.navigateSingleTopTo(
                                            route = NavigationScreens.MyOrders,
                                            navController
                                        )
                                    }
                                },
                                onSettingsClick = {
                                    if (currentScreenIsItemPage) {
                                        navController.navigate(NavigationScreens.SettingsPage){
                                            popUpTo(navController.currentDestination?.route!!){
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        navController.navigateSingleTopTo(
                                            route = NavigationScreens.SettingsPage,
                                            navController
                                        )
                                    }
                                }
                            )
                        }
                    )
                }

            },
        ) { innerPadding ->
            //we  use innerPadding because we added statusBarsPadding() and navigationBarsPadding()
            // to topBar and bottomBar
            //but here i removed topBar so i had to add statusBarsPadding() to show status bar color
            Navigation(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(innerPadding)
                    .fillMaxSize(),
                navController = navController,
                startDestination = status
            )
        }
    }
}

@Composable
fun FloatingButtonBar(onShoppingCartClick: () -> Unit) {
    FloatingActionButton(
        onClick = onShoppingCartClick,
        shape = RoundedCornerShape(50.dp),
        containerColor = Orange
    ) {
        Icon(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = "ShoppingCart",
            tint = Color.White
        )
    }
}

//BottomBar without Floating Button
@Composable
fun BottomAppBar(
    onHomeClick: () -> Unit, onProfileClick: () -> Unit,
    onMyOrdersClick: () -> Unit, onSettingsClick: () -> Unit
) {
    BottomNavigation(
        backgroundColor = MatteBlack,
    ) {
        BottomNavigationItem(
            selectedContentColor = Orange,
            selected = true,
            onClick = onHomeClick,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = Color.White
                )
            }
        )
        BottomNavigationItem(
            selectedContentColor = Orange,
            selected = false,
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = Color.White
                )
            }
        )

        BottomNavigationItem(
            selectedContentColor = Orange,
            selected = false,
            onClick = onMyOrdersClick,
            icon = {
                Icon(
                    imageVector = Icons.Filled.ShoppingBag,
                    contentDescription = "My Orders",
                    tint = Color.White
                )
            }
        )
        BottomNavigationItem(
            selectedContentColor = Orange,
            selected = false,
            onClick = onSettingsClick,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = Color.White
                )
            }
        )
    }
}

private fun hideBottomBar(currentScreenRoute: String): Boolean {
    //currentScreenRoute is LogInPage
    //currentScreenRoute is SignUpPage
    //currentScreenRoute is Empty
    return currentScreenRoute == NavigationScreens.LogInPage.ROUTE || currentScreenRoute == NavigationScreens.SignUpPage.ROUTE || currentScreenRoute.isEmpty()
}

//instead of getting com.example.coffee.LogInPage it will show LogInPage only
private fun getScreenName(route: String?): String {
    return route?.substringAfterLast('.') ?: ""
}