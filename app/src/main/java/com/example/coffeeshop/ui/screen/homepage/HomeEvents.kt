package com.example.coffeeshop.ui.screen.homepage

import androidx.navigation.NavHostController
import com.example.coffeeshop.ui.navigation.CurrentDestination

sealed interface HomeEvents {
    data class OnSeeAllClick(val navHostController: NavHostController, val route: CurrentDestination): HomeEvents

}