package com.vsebastianvc.flickr.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.vsebastianvc.flickr.R
import com.vsebastianvc.flickr.data.local.PhotoEntity

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

@Composable
fun PhotoItem(navController: NavController? = null, photo: PhotoEntity, route: String? = null) {
    var aspectRatio by remember { mutableFloatStateOf(1f) } // Default to 1:1 square

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (navController != null && route != null) {
                    Modifier.clickable {
                        // Check if the NavController has a graph set before navigating
                        if (navController.graph.startDestinationRoute != null) {
                            navController.navigate(route)
                        }
                    }
                } else {
                    Modifier
                }
            )
    ) {
        SubcomposeAsyncImage(
            model = photo.imageUrl,
            contentDescription = photo.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio),
            contentScale = ContentScale.Fit,
            onSuccess = { successState ->
                val imageWidth = successState.painter.intrinsicSize.width
                val imageHeight = successState.painter.intrinsicSize.height
                if (imageWidth > 0 && imageHeight > 0) {
                    aspectRatio = imageWidth / imageHeight
                }
            },
            loading = {
                Loading()
            },
            error = {
                ImageErrorPlaceHolder()
            }
        )
    }
}

@Composable
private fun ImageErrorPlaceHolder() {
    Image(
        painter = painterResource(id = R.drawable.hero_image_error),
        contentDescription = stringResource(id = R.string.image_placeholder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}


