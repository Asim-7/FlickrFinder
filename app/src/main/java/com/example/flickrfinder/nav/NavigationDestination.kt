package com.example.flickrfinder.nav

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface NavigationDestination {
    val route: String
}

object MainContent : NavigationDestination {
    override val route = "mainContent"
}

object PhotoContent : NavigationDestination {
    override val route = "photoContent"
    val routeWithArgs = "$route/{title}/{url}"
    val arguments = listOf(
        navArgument("title") { type = NavType.StringType },
        navArgument("url") { type = NavType.StringType }
    )
}

fun NavHostController.navigateSingleTopTo(route: String) = this.navigate(route) { launchSingleTop = true }