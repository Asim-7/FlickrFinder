package com.example.flickrfinder.components.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flickrfinder.model.PhotoData
import com.example.flickrfinder.viewmodel.PhotoViewModel

@Composable
fun HomeScreen(
    navigationViewModel: PhotoViewModel,
    onItemClicked: (photo: PhotoData) -> Unit,
    onLastItemReached: () -> Unit,
    onRetryClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleText(navigationViewModel = navigationViewModel)
            SearchButton(navigationViewModel = navigationViewModel)
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )

        HomePhotoGrid(
            navigationViewModel = navigationViewModel,
            onItemClicked = onItemClicked,
            onLastItemReached = onLastItemReached,
            onRetryClicked = onRetryClicked
        )
    }
}

@Composable
fun TitleText(navigationViewModel: PhotoViewModel) {
    val scroll = rememberScrollState(0)
    Text(
        text = navigationViewModel.currentSearchQuery,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier
            .width(200.dp)
            .padding(5.dp)
            .horizontalScroll(scroll),
        style = TextStyle(
            fontSize = 25.sp,
            fontFamily = FontFamily.SansSerif
        )
    )
}

@Composable
fun SearchButton(navigationViewModel: PhotoViewModel) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(shape = CircleShape)
            .background(MaterialTheme.colors.primary)
    ) {
        IconButton(onClick = {
            navigationViewModel.toggleTheme()
        }) {
            val darkTheme by navigationViewModel.darkTheme.collectAsState()
            Icon(
                imageVector = if (darkTheme) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                contentDescription = if (darkTheme) "Switch to Light Mode" else "Switch to Dark Mode",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colors.surface
            )
        }
    }
}