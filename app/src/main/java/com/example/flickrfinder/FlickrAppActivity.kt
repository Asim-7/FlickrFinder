package com.example.flickrfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flickrfinder.componenet.main.StandardScaffold
import com.example.flickrfinder.nav.Navigation
import com.example.flickrfinder.nav.Screen
import com.example.flickrfinder.ui.theme.FlickrFinderTheme
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
            setKeepVisibleCondition {
                splashViewModel.isLoading.value
            }
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
    FlickrFinderTheme {
        val context = LocalContext.current
        navigationViewModel.initData(context)

        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxSize()
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            StandardScaffold(
                navController = navController,
                showBottomBar = navBackStackEntry?.destination?.route in listOf(
                    Screen.HomeScreen.route,
                    Screen.FavoriteScreen.route,
                    Screen.OrderScreen.route,
                    Screen.ProfileScreen.route,
                ),
                onFabClick = {
                    navController.navigate(Screen.SearchScreen.route)
                }
            ) {
                Navigation(navController)
            }
        }

        /*NavHost(
            navController = navController,
            startDestination = MainContent.route
        ) {
            composable(route = MainContent.route) {
                MainView(
                    navigationViewModel = navigationViewModel,
                    onItemClicked = {
                        if (navigationViewModel.isNetworkConnected(context)) {
                            val encodedUrl = URLEncoder.encode(it.url_large, StandardCharsets.UTF_8.toString())
                            navController.navigateSingleTopTo("${PhotoContent.route}/${it.title}/$encodedUrl")
                        } else {
                            navigationViewModel.showMessage("Cannot preview: No internet", context)
                        }
                    },
                    onSearchClicked = {
                        navigationViewModel.updateSearchItem("")
                        navController.navigateSingleTopTo(SearchContent.route)
                    },
                    onLastItemReached = {
                        navigationViewModel.fetchData(context, navigationViewModel.titleText, true)
                    },
                    onRetryClicked = {
                        navigationViewModel.fetchData(context, navigationViewModel.titleText)
                    }
                )
            }

            composable(
                route = PhotoContent.routeWithArgs,
                arguments = PhotoContent.arguments
            ) { navBackStack ->
                val title = navBackStack.arguments?.getString("title")!!
                val url = navBackStack.arguments?.getString("url")!!
                val decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
                PhotoView(
                    title = title,
                    url = decodedUrl,
                    onCloseClicked = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = SearchContent.route) {
                SearchView(
                    navigationViewModel = navigationViewModel,
                    onSubmitSearch = {
                        navigationViewModel.addPrediction(it, true)
                        navigationViewModel.fetchData(context, it)
                        if (navigationViewModel.isNetworkConnected(context)) {
                            navController.navigate(route = MainContent.route) { popUpToRoute }
                        } else {
                            navController.popBackStack(route = MainContent.route, inclusive = false)
                        }
                    }
                )
            }
        }*/

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlickrFinderTheme {
        FlickrAppLayout(rememberNavController(), hiltViewModel())
    }
}