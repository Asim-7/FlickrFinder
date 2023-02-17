package com.example.flickrfinder.componenet

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.example.flickrfinder.model.PhotoData
import com.example.flickrfinder.ui.theme.WhiteShadow

@Composable
fun PhotoItem(
    item: PhotoData,
    onItemClicked: (item: PhotoData) -> Unit
) {
    Column(modifier = Modifier
        .wrapContentSize()
        .clip(RoundedCornerShape(5.dp))
        .clickable { onItemClicked(item) }
    ) {
        val scroll = rememberScrollState(0)
        Image(
            painter = rememberAsyncImagePainter(item.url),
            contentDescription = null,
            modifier = Modifier
                .width(150.dp)
                .height(100.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = item.title,
            modifier = Modifier
                .background(WhiteShadow)
                .width(150.dp)
                .padding(5.dp)
                .horizontalScroll(scroll),
            textAlign = TextAlign.Center
        )
    }
}