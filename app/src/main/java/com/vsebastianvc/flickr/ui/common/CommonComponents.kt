package com.vsebastianvc.flickr.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Loading(backGroundColor: Color = Color.DarkGray) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backGroundColor),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
