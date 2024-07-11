package com.example.flickrfinder.components.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.flickrfinder.ui.theme.colorBlack

@Composable
fun NotificationScreen (navController: NavController) {
    Box(
    modifier = Modifier
    .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}