package com.example.flickrfinder.componenet.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.example.flickrfinder.model.PhotoData
import com.example.flickrfinder.ui.theme.*

@Composable
fun PhotoItem(
    item: PhotoData,
    onItemClicked: (item: PhotoData) -> Unit
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable { onItemClicked(item) },
        elevation = 4.dp,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val scroll = rememberScrollState(0)
            Image(
                painter = rememberAsyncImagePainter(item.url_small),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = item.title,
                modifier = Modifier
                    .background(colorRedGrayLight)
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                    .horizontalScroll(scroll),
                textAlign = TextAlign.Center
            )
        }
    }
}