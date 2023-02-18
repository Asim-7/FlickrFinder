package com.example.flickrfinder.componenet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flickrfinder.model.PhotoData
import com.example.flickrfinder.viewmodel.PhotoViewModel

@Composable
fun MainPhotoView(
    navigationViewModel: PhotoViewModel,
    onItemClicked: (photo: PhotoData) -> Unit
) {
    /*if (navigationViewModel.inProgressValue) {
        ProgressLoader()
    } else {
        PhotoGrid(
            navigationViewModel = navigationViewModel,
            onItemClicked = onItemClicked
        )
    }*/

    PhotoGrid(
        navigationViewModel = navigationViewModel,
        onItemClicked = onItemClicked
    )

}

@Composable
fun ProgressLoader() {
    CircularProgressIndicator(
        modifier = Modifier
            .width(20.dp)
            .height(20.dp)
    )
}

@Composable
fun PhotoGrid(
    navigationViewModel: PhotoViewModel,
    onItemClicked: (photo: PhotoData) -> Unit
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
    }
}