package com.vsebastianvc.flickr.photogallery.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vsebastianvc.flickr.R
import com.vsebastianvc.flickr.navigation.FlickrScreens
import com.vsebastianvc.flickr.photogallery.viewmodel.PhotoGalleryViewModel
import com.vsebastianvc.flickr.ui.common.Loading
import com.vsebastianvc.flickr.ui.common.PhotoItem
import com.vsebastianvc.flickr.utils.NetworkUtils
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun PhotoGalleryScreen(
    navController: NavController,
    viewModel: PhotoGalleryViewModel = getViewModel(),
    networkUtils: NetworkUtils = NetworkUtils(LocalContext.current)
) {
    val photos by viewModel.photos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val noInternetMessage = stringResource(id = R.string.no_internet_connection)

    Scaffold(
        topBar = {
            SearchAppBar(
                onSearch = { query ->
                    if(networkUtils.isNetworkAvailable()) {
                        viewModel.searchPhotos(query)
                    } else{
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = noInternetMessage,
                                duration = SnackbarDuration.Long
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) } // Attach SnackbarHost to Scaffold
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading && photos.isEmpty()) {
                Loading(Color.Transparent)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp),
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(photos) { photo ->
                        val photoDetailRoute = "${FlickrScreens.PhotoDetail.name}/${photo.id}"
                        PhotoItem(
                            navController = navController,
                            photo = photo,
                            route = photoDetailRoute
                        )
                    }

                    // Add a loading indicator at the bottom
                    if (isLoading) {
                        item {
                            Loading()
                        }
                    }
                }
            }
        }
    }

    // Infinite scroll trigger
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex == photos.size - 1 && !isLoading) {
                    viewModel.loadMorePhotos()
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
                placeholder = {
                    Text(
                        stringResource(id = R.string.search_flickr),
                        color = Color.Gray
                    )
                },
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
                contentDescription = stringResource(id = R.string.search_icon)
            )
        }
    )
}