package com.example.flickrfinder.componenet.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flickrfinder.model.PhotoData
import com.example.flickrfinder.ui.theme.WhiteShadow
import com.example.flickrfinder.viewmodel.PhotoViewModel

@Composable
fun MainPhotoView(
    navigationViewModel: PhotoViewModel,
    onItemClicked: (photo: PhotoData) -> Unit,
    onLastItemReached: () -> Unit,
    onRetryClicked: () -> Unit
) {
    if (navigationViewModel.photosList.isEmpty() && navigationViewModel.showRedo) {
        Retry(onRetryClicked = { if (!navigationViewModel.inProgress) onRetryClicked() })
    } else {
        PhotoGrid(
            navigationViewModel = navigationViewModel,
            onItemClicked = onItemClicked,
            onLastItemReached = onLastItemReached
        )
    }
}

@Composable
fun Retry(onRetryClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .width(100.dp)
                .wrapContentHeight()
                .background(WhiteShadow)
                .clickable { onRetryClicked() },
            text = "Retry",
            fontSize = 25.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PhotoGrid(
    navigationViewModel: PhotoViewModel,
    onItemClicked: (photo: PhotoData) -> Unit,
    onLastItemReached: () -> Unit
) {
    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        state = listState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 10.dp)
    ) {
        items(navigationViewModel.photosList) { item ->
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
}