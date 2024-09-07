package com.example.coffeeshop

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.coffeeshop.ui.MyAppViewModel
import com.example.coffeeshop.ui.navigation.CurrentDestination
import com.example.coffeeshop.ui.navigation.Navigation
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
                Log.d(
                    "MainActivity",
                    "onCreate finished in ${System.currentTimeMillis() - startTime} ms"
                )
                //initialize viewmodel to check if already logged in via shared preferences
                val myAppViewModel = hiltViewModel<MyAppViewModel>()
                val status by myAppViewModel.status.collectAsStateWithLifecycle()

                val navController = rememberNavController()

                val backStackEntry by navController.currentBackStackEntryAsState()
                Log.d("currentScreen", "ff ${backStackEntry?.destination}")
                //get current screen
                val currentScreen = backStackEntry?.destination?.route
                //get current screen route without (com.example.coffee...)
                val currentScreenRoute = getScreenName(currentScreen)

                //not hideBottomBar
                val showBottomBar = !(hideBottomBar(status,currentScreenRoute))


                if (status != CurrentDestination.Loading) {
                    Scaffold(
                        bottomBar = {
                            if (showBottomBar) {
                                BottomBar(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                ) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Bottom navigation CLicked",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        },
                    ) { innerPadding ->
                        Navigation(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            navController = navController,
                            startDestination = status
                        )
                    }
                } else {
                    Scaffold { innerPadding ->
                        Image(
                            painter = painterResource(id = R.drawable.loading),
                            contentDescription = stringResource(id = R.string.loading),
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(modifier: Modifier, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = modifier) {
        Text(
            "Click me", color = Color.Black, style = MaterialTheme.typography.titleLarge
        )
    }
}

private fun hideBottomBar(status: CurrentDestination,currentScreenRoute: String): Boolean{
    if(status == CurrentDestination.LogInPage){
        //currentScreenRoute is LogInPage
        //currentScreenRoute is SignUpPage
        //currentScreenRoute is Empty
        if(currentScreenRoute == CurrentDestination.LogInPage.ROUTE || currentScreenRoute == CurrentDestination.SignUpPage.ROUTE || currentScreenRoute.isEmpty()){
            return true
        }
    }
    return false
}

//instead of getting com.example.coffee.LogInPage it will show LogInPage only
private fun getScreenName(route: String?): String {
    return route?.substringAfterLast('.') ?: ""
}
