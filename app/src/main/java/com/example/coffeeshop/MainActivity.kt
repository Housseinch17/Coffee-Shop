package com.example.coffeeshop

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.coffeeshop.ui.screen.mainpage.MainPage
import com.example.coffeeshop.ui.theme.CoffeeShopTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        val startTime = System.currentTimeMillis()
        Log.d("MainActivity", "onCreate started")
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeShopTheme {
                FirebaseApp.initializeApp(this)
                MainPage()
                Log.d(
                    "MainActivity",
                    "onCreate finished in ${System.currentTimeMillis() - startTime} ms"
                )
            }
        }
    }
}