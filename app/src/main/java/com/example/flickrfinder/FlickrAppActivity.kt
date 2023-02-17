package com.example.flickrfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flickrfinder.componenet.MainView
import com.example.flickrfinder.componenet.PhotoView
import com.example.flickrfinder.nav.MainContent
import com.example.flickrfinder.nav.PhotoContent
import com.example.flickrfinder.nav.navigateSingleTopTo
import com.example.flickrfinder.ui.theme.FlickrFinderTheme
import com.example.flickrfinder.viewmodel.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class FlickrAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        navigationViewModel.fetchData()

        NavHost(
            navController = navController,
            startDestination = MainContent.route
        ) {
            composable(route = MainContent.route) {
                MainView(
                    navigationViewModel = navigationViewModel,
                    onItemClicked = {
                        val encodedUrl = URLEncoder.encode(it.url, StandardCharsets.UTF_8.toString())
                        navController.navigateSingleTopTo("${PhotoContent.route}/${it.title}/$encodedUrl")
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