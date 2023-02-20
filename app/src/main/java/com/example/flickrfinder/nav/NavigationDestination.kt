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

object SearchContent : NavigationDestination {
    override val route = "searchContent"
}

object FavoriteContent : NavigationDestination {
    override val route = "favoriteContent"
}

object NotificationContent : NavigationDestination {
    override val route = "notificationContent"
}

object ProfileContent : NavigationDestination {
    override val route = "profileContent"
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