package com.vsebastianvc.flickr.photogallery.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.vsebastianvc.flickr.data.local.PhotoEntity
import com.vsebastianvc.flickr.navigation.FlickrScreens
import com.vsebastianvc.flickr.photogallery.viewmodel.PhotoGalleryViewModel
import com.vsebastianvc.flickr.ui.common.Loading
import org.koin.androidx.compose.getViewModel


@Composable
fun PhotoGalleryScreen(
    navController: NavController,
    viewModel: PhotoGalleryViewModel = getViewModel()
) {
    val photos by viewModel.photos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            SearchAppBar(
                onSearch = { query ->
                    viewModel.searchPhotos(query)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                Loading(Color.Transparent)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(photos) { photo ->
                        PhotoItem(navController, photo)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    onSearch: (String) -> Unit
) {
    val tag = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(tag.value) {
        tag.value.trim().isNotEmpty()
    }

    TopAppBar(
        modifier = Modifier.padding(horizontal = 4.dp),
        title = {
            TextField(
                value = tag.value,
                onValueChange = {
                    tag.value = it
                },
                placeholder = { Text("Search Flickr", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (valid) {
                            onSearch(tag.value.trim())
                            tag.value = ""
                        }
                        keyboardController?.hide()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        }
    )
}

@Composable
fun PhotoItem(navController: NavController, photo: PhotoEntity) {
    val photoDetailRoute = "${FlickrScreens.PhotoDetail.name}/${photo.id}"
    var aspectRatio by remember { mutableFloatStateOf(1f) } // Default to 1:1 square
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(photoDetailRoute) }
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Clear,
            contentDescription = "Image failed to load",
            modifier = Modifier.size(48.dp),
            tint = Color.White
        )
        Text(
            text = "Something went wrong :(",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}