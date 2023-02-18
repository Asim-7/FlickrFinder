package com.example.flickrfinder.componenet

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flickrfinder.model.PhotoData
import com.example.flickrfinder.ui.theme.WhiteShadow
import com.example.flickrfinder.viewmodel.PhotoViewModel

@Composable
fun MainView(
    navigationViewModel: PhotoViewModel,
    onItemClicked: (photo: PhotoData) -> Unit,
    onSearchClicked: () -> Unit
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
            TitleText(navigationViewModel = navigationViewModel)
            SearchButton(onSearchClicked)
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
fun TitleText(navigationViewModel: PhotoViewModel) {
    val scroll = rememberScrollState(0)
    Text(
        text = navigationViewModel.searchTextValue,
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
fun SearchButton(onSearchClicked: () -> Unit) {
    Icon(
        modifier = Modifier
            .width(30.dp)
            .height(30.dp)
            .clickable { onSearchClicked() },
        imageVector = Icons.Default.Search,
        contentDescription = "search"
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