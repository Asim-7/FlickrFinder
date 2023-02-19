package com.example.flickrfinder.componenet

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.flickrfinder.ui.theme.WhiteShadow

@Composable
fun PhotoView(
    title: String,
    url: String,
    onCloseClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp)
            .clickable { onCloseClicked() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val scroll = rememberScrollState(0)
        Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = null,
            modifier = Modifier
                .height(600.dp)
                .width(400.dp)
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = title,
            modifier = Modifier
                .padding(top = 15.dp, bottom = 15.dp)
                .background(WhiteShadow)
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)
                .horizontalScroll(scroll),
            textAlign = TextAlign.Center
        )
    }
}