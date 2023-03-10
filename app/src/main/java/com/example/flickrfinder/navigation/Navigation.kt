package com.example.flickrfinder.navigation

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flickrfinder.components.screens.*
import com.example.flickrfinder.components.screens.home.HomeScreen
import com.example.flickrfinder.viewmodel.PhotoViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun Navigation(navController: NavHostController, navigationViewModel: PhotoViewModel, context: Context) {
    NavHost(
        navController = navController,
        startDestination = HomeScreen.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            route = HomeScreen.route
        ) {
            HomeScreen(
                navigationViewModel = navigationViewModel,
                onItemClicked = {
                    if (navigationViewModel.isNetworkConnected()) {
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
                    if (navigationViewModel.isNetworkConnected()) {
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