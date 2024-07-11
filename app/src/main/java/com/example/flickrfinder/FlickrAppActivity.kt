package com.example.flickrfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
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
import com.example.flickrfinder.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlickrAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // this part is only for splash screen
        val splashViewModel: SplashViewModel by viewModels()
        installSplashScreen().apply {
            /*setKeepVisibleCondition {
                splashViewModel.isLoading.value
            }*/
        }

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
    val darkTheme = navigationViewModel.darkTheme.observeAsState(initial = isSystemInDarkTheme())
    SetStatusBarColor(darkTheme.value)
    FlickrFinderTheme(darkTheme.value) {
        MainView(navController, navigationViewModel)
    }
}

@Composable
private fun MainView(navController: NavHostController, navigationViewModel: PhotoViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        StandardScaffold(
            navController = navController,
            showBottomBar = navBackStackEntry?.destination?.route in listOf(
                HomeScreen.route,
                FavoriteScreen.route,
                NotificationScreen.route,
                ProfileScreen.route,
            ),
            onFabClick = {
                navigationViewModel.updateSearchItem("")
                navController.navigate(SearchScreen.route)
            },
            onBottomNavClicked = {
                if (SearchScreen.route == it) navigationViewModel.updateSearchItem("")
                navController.navigateSingleTopTo(it)
            }
        ) {
            Navigation(navController, navigationViewModel)
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