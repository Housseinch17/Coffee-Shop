package com.example.coffeeshop

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.coffeeshop.ui.MainPage
import com.example.coffeeshop.ui.theme.CoffeeShopTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        val startTime = System.currentTimeMillis()
        Log.d("MainActivity", "onCreate started")
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val restoreState = savedInstanceState?.getBundle("nav_state")
        enableEdgeToEdge()
        setContent {
            CoffeeShopTheme {
                FirebaseApp.initializeApp(this)
                navController = rememberNavController()
                navController.restoreState(restoreState)
                MainPage(navController = navController)
                Log.d(
                    "MainActivity",
                    "onCreate finished in ${System.currentTimeMillis() - startTime} ms"
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //saving state when app is in background
        if (::navController.isInitialized) {
            outState.putBundle("nav_state", navController.saveState())
        }
    }
}