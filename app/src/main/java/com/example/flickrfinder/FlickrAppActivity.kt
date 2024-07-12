package com.example.flickrfinder

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flickrfinder.components.bottomnav.StandardScaffold
import com.example.flickrfinder.navigation.*
import com.example.flickrfinder.ui.theme.FlickrFinderTheme
import com.example.flickrfinder.ui.theme.colorRedDark
import com.example.flickrfinder.ui.theme.colorRedDarker
import com.example.flickrfinder.viewmodel.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlickrAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // this part is only for splash screen
        installSplashScreen()

        setContent {
            FlickrAppLayout(rememberNavController())
        }
    }
}

@Composable
fun FlickrAppLayout(
    navController: NavHostController,
    navigationViewModel: PhotoViewModel = hiltViewModel()
) {
    val darkTheme by navigationViewModel.darkTheme.collectAsState(initial = isSystemInDarkTheme())
    SetStatusBarColor(darkTheme)
    FlickrFinderTheme(darkTheme) {
        MainView(navController, navigationViewModel)
    }
}

@Composable
private fun MainView(navController: NavHostController, navigationViewModel: PhotoViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val context = LocalContext.current
        val uiState by navigationViewModel.uiState.collectAsState()

        StandardScaffold(
            navController = navController,
            showBottomBar = navBackStackEntry?.destination?.route in listOf(
                HomeScreen.route,
                FavoriteScreen.route,
                NotificationScreen.route,
                ProfileScreen.route,
            ),
            onFabClick = {
                navigationViewModel.updateSearchQuery("")
                navController.navigate(SearchScreen.route)
            },
            onBottomNavClicked = {
                if (SearchScreen.route == it) navigationViewModel.updateSearchQuery("")
                navController.navigateSingleTopTo(it)
            }
        ) {
            Navigation(navController, navigationViewModel)
        }

        // Show toast message if any
        LaunchedEffect(key1 = uiState.messageState) {
            if (uiState.messageState != null) {
                Toast.makeText(context, uiState.messageState, Toast.LENGTH_SHORT).show()
                navigationViewModel.clearMessage() // Reset after showing
            }
        }
    }
}

@Composable
fun SetStatusBarColor(darkTheme: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as android.app.Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            window.statusBarColor = if (darkTheme) colorRedDarker.toArgb() else colorRedDark.toArgb()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlickrFinderTheme {
        FlickrAppLayout(rememberNavController(), hiltViewModel())
    }
}