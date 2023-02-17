package com.example.flickrfinder.componenet

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    Column(modifier = Modifier
        .wrapContentSize()
        .clickable { onCloseClicked() }
    ) {
        val scroll = rememberScrollState(0)
        Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .padding(20.dp)
                .clip(RoundedCornerShape(5.dp))
        )
        Text(
            text = title,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)
                .background(WhiteShadow)
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)
                .horizontalScroll(scroll),
            textAlign = TextAlign.Center
        )
    }
}