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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flickrfinder.componenet.PhotoPreviewScreen
import com.example.flickrfinder.componenet.SearchView
import com.example.flickrfinder.componenet.bottom.*
import com.example.flickrfinder.componenet.main.MainView
import com.example.flickrfinder.componenet.main.StandardScaffold
import com.example.flickrfinder.nav.*
import com.example.flickrfinder.ui.theme.FlickrFinderTheme
import com.example.flickrfinder.viewmodel.PhotoViewModel
import com.example.flickrfinder.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                NavHost(
                    navController = navController,
                    startDestination = HomeScreen.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(
                        route = HomeScreen.route
                    ) {
                        MainView(
                            navigationViewModel = navigationViewModel,
                            onItemClicked = {
                                if (navigationViewModel.isNetworkConnected(context)) {
                                    val encodedUrl = URLEncoder.encode(it.url_large, StandardCharsets.UTF_8.toString())
                                    navController.navigateSingleTopTo("${PhotoPreviewScreen.route}/${it.title}/$encodedUrl")
                                } else {
                                    navigationViewModel.showMessage("Cannot preview: No internet", context)
                                }
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
                        route = PhotoPreviewScreen.routeWithArgs,
                        arguments = PhotoPreviewScreen.arguments
                    ) { navBackStack ->
                        val title = navBackStack.arguments?.getString("title")!!
                        val url = navBackStack.arguments?.getString("url")!!
                        val decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
                        PhotoPreviewScreen(
                            title = title,
                            url = decodedUrl,
                            onCloseClicked = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(
                        route = FavoriteScreen.route
                    ) {
                        FavoriteScreen(navController)
                    }

                    composable(route = SearchScreen.route) {
                        SearchView(
                            navigationViewModel = navigationViewModel,
                            onSubmitSearch = {
                                navigationViewModel.addPrediction(it, true)
                                navigationViewModel.fetchData(context, it)
                                if (navigationViewModel.isNetworkConnected(context)) {
                                    navController.navigate(route = HomeScreen.route) { popUpToRoute }
                                } else {
                                    navController.popBackStack(route = HomeScreen.route, inclusive = false)
                                }
                            }
                        )
                    }

                    composable(
                        route = NotificationScreen.route
                    ) {
                        NotificationScreen(navController)
                    }
                    composable(
                        route = ProfileScreen.route
                    ) {
                        ProfileScreen(navController)
                    }

                }

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