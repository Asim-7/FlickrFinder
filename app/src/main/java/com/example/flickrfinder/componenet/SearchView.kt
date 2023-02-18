package com.example.flickrfinder.componenet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.flickrfinder.ui.theme.Teal200

@Composable
fun SearchView(onSubmitSearch: (searchText: String) -> Unit) {
    Text(
        text = "",
        modifier = Modifier
            .fillMaxSize()
            .clickable { onSubmitSearch("mountains") }
            .background(Teal200)
    )
}