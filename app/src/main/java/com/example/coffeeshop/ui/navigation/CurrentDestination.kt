package com.example.coffeeshop.ui.navigation

import com.example.coffeeshop.data.model.categoryItems.CategoryItems
import com.example.coffeeshop.data.model.offers.Offers
import com.example.coffeeshop.data.model.shoppingCart.CategoryItemsCart
import com.example.coffeeshop.data.model.shoppingCart.OfferCart
import kotlinx.serialization.Serializable

@Serializable
sealed interface CurrentDestination {
    //Register NavGraph
    @Serializable
    data object Register : CurrentDestination

    //Register Screens
    @Serializable
    data object LogInPage : CurrentDestination {
        const val ROUTE = "LogInPage"
    }

    @Serializable
    data object SignUpPage : CurrentDestination {
        const val ROUTE = "SignUpPage"
    }

    //CoffeeShop NavGraph
    @Serializable
    data object CoffeeShop : CurrentDestination


    //CoffeeShop screens
    @Serializable
    data object HomePage : CurrentDestination

    @Serializable
    data class CategoryItemPage(val categoryItems: CategoryItems = CategoryItems()) :
        CurrentDestination

    @Serializable
    data class OfferItemPage(val offers: Offers = Offers()) : CurrentDestination

    @Serializable
    data object ProfilePage : CurrentDestination

    @Serializable
    data class ShoppingCartPage(
        val categoryItemsCart: CategoryItemsCart = CategoryItemsCart(),
        val offerCart: OfferCart = OfferCart(),
    ) : CurrentDestination

    @Serializable
    data object SettingsPage : CurrentDestination

    @Serializable
    data object MyOrders : CurrentDestination

    @Serializable
    data class AllCategories(val allCategories: List<CategoryItems> = emptyList()) :
        CurrentDestination

    @Serializable
    data class AllOffers(val allOffers: List<Offers> = emptyList()) : CurrentDestination

    @Serializable
    data object Loading : CurrentDestination
}
