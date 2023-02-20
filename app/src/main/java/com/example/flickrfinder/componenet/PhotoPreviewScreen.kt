package com.example.flickrfinder.componenet

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.flickrfinder.ui.theme.colorRedDark
import com.example.flickrfinder.ui.theme.colorWhite

@Composable
fun PhotoPreviewScreen(
    title: String,
    url: String,
    onCloseClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PhotoPreview(
            url = url,
            title = title
        )

        CloseButton {
            onCloseClicked()
        }
    }
}

@Composable
fun PhotoPreview(url: String, title: String) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .wrapContentSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = null,
            modifier = Modifier
                .height(600.dp)
                .width(400.dp)
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun CloseButton(onCloseClicked: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier.padding(top = 15.dp),
        backgroundColor = colorRedDark,
        onClick = onCloseClicked
    ) {
        Icon(
            imageVector = Icons.Outlined.Close,
            contentDescription = "Close",
            tint = colorWhite
        )
    }
}