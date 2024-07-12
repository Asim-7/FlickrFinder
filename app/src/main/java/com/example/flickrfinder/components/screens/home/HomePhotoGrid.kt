package com.example.flickrfinder.components.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flickrfinder.model.FlickrUiState
import com.example.flickrfinder.model.NetworkState
import com.example.flickrfinder.model.PhotoData
import com.example.flickrfinder.ui.theme.colorWhite
import com.example.flickrfinder.viewmodel.PhotoViewModel

@Composable
fun HomePhotoGrid(
    navigationViewModel: PhotoViewModel,
    onItemClicked: (photo: PhotoData) -> Unit,
    onLastItemReached: () -> Unit,
    onRetryClicked: () -> Unit
) {
    val uiState by navigationViewModel.uiState.collectAsState()

    if (uiState.requestState != NetworkState.Loading && uiState.photosList.isEmpty()) {
        Retry(onRetryClicked = { if (!navigationViewModel.isLoading) onRetryClicked() })
    }

    PhotoGrid(
        uiState = uiState,
        onItemClicked = onItemClicked,
        onLastItemReached = onLastItemReached
    )
}

@Composable
fun Retry(onRetryClicked: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onRetryClicked,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            modifier = Modifier
                .width(300.dp)
                .height(50.dp)
                .padding(start = 40.dp, end = 40.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Try again \uD83D\uDE1F",
                color = colorWhite,
                style = MaterialTheme.typography.button,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun PhotoGrid(
    uiState: FlickrUiState,
    onItemClicked: (photo: PhotoData) -> Unit,
    onLastItemReached: () -> Unit
) {
    val listState = rememberLazyGridState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 10.dp)
        ) {
            items(uiState.photosList) { item ->
                PhotoItem(item) { photoItem ->
                    onItemClicked(photoItem)
                }
            }
            item {
                LaunchedEffect(true) {
                    onLastItemReached()
                }
            }
        }

        // Circular progress indicator
        if (uiState.requestState == NetworkState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}