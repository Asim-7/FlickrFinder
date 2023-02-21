package com.example.flickrfinder.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface NavigationDestination {
    val route: String
}

object HomeScreen : NavigationDestination {
    override val route = "homeScreen"
}

object SearchScreen : NavigationDestination {
    override val route = "searchScreen"
}

object FavoriteScreen : NavigationDestination {
    override val route = "favoriteScreen"
}

object NotificationScreen : NavigationDestination {
    override val route = "notificationScreen"
}

object ProfileScreen : NavigationDestination {
    override val route = "profileScreen"
}

object PhotoPreviewScreen : NavigationDestination {
    override val route = "photoPreviewScreen"
    val routeWithArgs = "$route/{title}/{url}"
    val arguments = listOf(
        navArgument("title") { type = NavType.StringType },
        navArgument("url") { type = NavType.StringType }
    )
}

fun NavHostController.navigateSingleTopTo(route: String) = this.navigate(route) { launchSingleTop = true }