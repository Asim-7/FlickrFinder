package com.example.flickrfinder.componenet

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        CloseButton {
            onCloseClicked()
        }

        PhotoPreview(
            url = url,
            title = title
        )
    }
}

@Composable
fun PhotoPreview(url: String, title: String) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .fillMaxHeight(),
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
                .width(400.dp)
                .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)
                .horizontalScroll(scroll),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CloseButton(onCloseClicked: () -> Unit) {
    IconButton(
        onClick = onCloseClicked,
        modifier = Modifier
            .padding(bottom = 15.dp)
            .then(Modifier.size(30.dp))
            .border(1.dp, Color.Transparent, shape = CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "close"
        )
    }
}