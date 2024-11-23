package com.example.coffeeshop.ui.navigation

import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.data.model.offers.Offers
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart
import com.example.coffeeshop.data.model.shoppingCart.OfferCart
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationScreens {
    //Register NavGraph
    @Serializable
    data object Register : NavigationScreens

    //Register Screens
    @Serializable
    data object LogInPage : NavigationScreens {
        const val ROUTE = "LogInPage"
    }

    @Serializable
    data object SignUpPage : NavigationScreens {
        const val ROUTE = "SignUpPage"
    }

    //CoffeeShop NavGraph
    @Serializable
    data object CoffeeShop : NavigationScreens


    //CoffeeShop screens
    @Serializable
    data object HomePage : NavigationScreens

    @Serializable
    data class CategoryItemPage(val categoryItems: CategoryItems = CategoryItems()) :
        NavigationScreens

    @Serializable
    data class OfferItemPage(val offers: Offers = Offers()) : NavigationScreens

    @Serializable
    data object ProfilePage : NavigationScreens

    @Serializable
    data class ShoppingCartPage(
        val categoryItemsCart: CategoryItemsCart = CategoryItemsCart(),
        val offerCart: OfferCart = OfferCart(),
    ) : NavigationScreens

    @Serializable
    data object SettingsPage : NavigationScreens

    @Serializable
    data object MyOrders : NavigationScreens

    @Serializable
    data class AllCategories(val allCategories: List<CategoryItems> = emptyList()) :
        NavigationScreens

    @Serializable
    data class AllOffers(val allOffers: List<Offers> = emptyList()) : NavigationScreens

    @Serializable
    data object Loading : NavigationScreens
}
