package com.example.coffeeshop.ui.screen.mainpage

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.TopAppBar
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.coffeeshop.R
import com.example.coffeeshop.ui.navigation.CurrentDestination
import com.example.coffeeshop.ui.navigation.Navigation

@Composable
fun MainPage(navController: NavHostController) {
    //initialize viewmodel to check if already logged in via shared preferences
    val mainViewModel = hiltViewModel<MainViewModel>()
    val status by mainViewModel.status.collectAsStateWithLifecycle()


    val backStackEntry by navController.currentBackStackEntryAsState()
    //get current screen
    val currentScreen = backStackEntry?.destination?.route
    //get current screen route without (com.example.coffee...)
    val currentScreenRoute = getScreenName(currentScreen)
    Log.d("currentScreen",currentScreenRoute)


    //not hideBottomBar
    val showBottomBar = !(hideBottomBar(currentScreenRoute))

    if (status != CurrentDestination.Loading) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .height(56.dp),
                    backgroundColor = Color.Green,
                ) {
                    Text(
                        "Anything is here",
                        textAlign = TextAlign.Center
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigation(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .fillMaxWidth()
                            .height(56.dp),
                        backgroundColor = Color.Green,
                    ) {
                        BottomBar(
                            modifier = Modifier
                        ) {

                        }
                    }
                }
            },
        ) { innerPadding ->
            Navigation(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .statusBarsPadding()
                    .navigationBarsPadding(),
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
                    .statusBarsPadding()
                    .navigationBarsPadding()
            )
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

private fun hideBottomBar(currentScreenRoute: String): Boolean {
    //currentScreenRoute is LogInPage
    //currentScreenRoute is SignUpPage
    //currentScreenRoute is Empty
    return currentScreenRoute == CurrentDestination.LogInPage.ROUTE || currentScreenRoute == CurrentDestination.SignUpPage.ROUTE || currentScreenRoute.isEmpty()
}

//instead of getting com.example.coffee.LogInPage it will show LogInPage only
private fun getScreenName(route: String?): String {
    return route?.substringAfterLast('.') ?: ""
}