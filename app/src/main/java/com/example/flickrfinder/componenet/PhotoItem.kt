package com.example.flickrfinder.componenet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import coil.compose.rememberAsyncImagePainter
import com.example.flickrfinder.model.PhotoData

@Composable
fun PhotoItem(
    item: PhotoData,
    onItemClicked: (item: PhotoData) -> Unit
) {
    Column(modifier = Modifier.clickable {
        onItemClicked(item)
    }) {
        Image(
            painter = rememberAsyncImagePainter(item.url),
            contentDescription = null,
            modifier = Modifier
                .width(223.dp)
                .height(125.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(5.dp))
        )
        Text(
            text = item.title,
            modifier = Modifier
                .padding(top = 5.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}