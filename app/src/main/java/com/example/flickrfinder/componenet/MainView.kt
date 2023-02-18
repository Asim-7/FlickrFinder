package com.example.flickrfinder.componenet

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flickrfinder.model.PhotoData
import com.example.flickrfinder.ui.theme.Teal200
import com.example.flickrfinder.ui.theme.WhiteShadow
import com.example.flickrfinder.viewmodel.PhotoViewModel

@Composable
fun MainView(
    navigationViewModel: PhotoViewModel,
    onItemClicked: (photo: PhotoData) -> Unit
) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .background(WhiteShadow)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleText()
            SearchButton()
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )

        PhotoGrid(
            navigationViewModel = navigationViewModel,
            onItemClicked = onItemClicked
        )
    }
}

@Composable
fun TitleText() {
    val scroll = rememberScrollState(0)
    Text(
        text = "Nature",
        modifier = Modifier
            .background(Teal200)
            .padding(5.dp)
            .horizontalScroll(scroll)
    )
}

@Composable
fun SearchButton() {
    Text(
        text = "Search",
        modifier = Modifier
            .width(150.dp)
            .padding(5.dp)
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