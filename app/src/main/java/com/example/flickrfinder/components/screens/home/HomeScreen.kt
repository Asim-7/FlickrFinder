package com.example.flickrfinder.components.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flickrfinder.model.PhotoData
import com.example.flickrfinder.ui.theme.WhiteShadow
import com.example.flickrfinder.ui.theme.colorRedDark
import com.example.flickrfinder.ui.theme.colorWhite
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
                .background(WhiteShadow)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleText(navigationViewModel = navigationViewModel)
            SearchButton()
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
fun SearchButton() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(shape = CircleShape)
            .background(colorRedDark)
    ) {
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "",
                tint = colorWhite
            )
        }
    }
}