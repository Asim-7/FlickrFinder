package com.example.flickrfinder.componenet.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onSearchClicked: () -> Unit,
    onLastItemReached: () -> Unit,
    onRetryClicked: () -> Unit
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

        MainPhotoView(
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
        text = navigationViewModel.titleText,
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
    IconButton(
        onClick = {
            onSearchClicked()
        },
        modifier = Modifier
            .then(Modifier.size(30.dp))
            .border(1.dp, Color.Transparent, shape = CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "search"
        )
    }
}