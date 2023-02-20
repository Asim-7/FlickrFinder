package com.example.flickrfinder.nav

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object FavoriteScreen : Screen("favorite_screen")
    object SearchScreen : Screen("search_screen")
    object OrderScreen : Screen("order_screen")
    object ProfileScreen : Screen("profile_screen")
}