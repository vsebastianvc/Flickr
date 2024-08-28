package com.vsebastianvc.flickr.photodetail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vsebastianvc.flickr.R
import com.vsebastianvc.flickr.data.local.PhotoEntity
import com.vsebastianvc.flickr.photodetail.viewmodel.PhotoDetailViewModel
import com.vsebastianvc.flickr.ui.common.Loading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailScreen(
    photoId: String?,
    navController: NavController,
    viewModel: PhotoDetailViewModel = getViewModel()
) {
    photoId?.let { viewModel.loadPhotoDetail(it) }
    val photoDetail by viewModel.photoDetail.collectAsState()

    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            photoDetail?.let { detail ->
                PhotoDetailSheetContent(detail, navController)
            } ?: LoadingContent("Loading details...")
        }
    ) {
        PhotoDetailBody(photoDetail, scaffoldState, coroutineScope)
    }
}

@Composable
fun PhotoDetailSheetContent(detail: PhotoEntity, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        Text(
            text = "Title: ${detail.title}",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Description: ${detail.description?.ifEmpty { "No description available" }}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Date Taken: ${detail.dateTaken ?: "Date not available"}",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "Date Posted: ${detail.datePosted ?: "Date not available"}",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Back to Gallery"),
            onClick = { navController.navigateUp() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailBody(
    photoDetail: PhotoEntity?,
    scaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            photoDetail?.let { detail ->
                PhotoDetailImage(detail)
            } ?: Loading()
            photoDetail?.let { detail ->
                PhotoDetailTitle(detail.title)
            }
            PhotoDetailIcon(scaffoldState, coroutineScope)
        }
    }
}

@Composable
fun PhotoDetailImage(detail: PhotoEntity) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(detail.imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = detail.title,
        placeholder = painterResource(id = R.drawable.hero_image_placeholder),
        error = painterResource(id = R.drawable.hero_image_error),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}

@Composable
fun PhotoDetailTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.White,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailIcon(
    scaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope
) {
    Icon(
        imageVector = Icons.Filled.Info,
        contentDescription = "Show Details",
        modifier = Modifier
            .size(48.dp)
            .clickable {
                coroutineScope.launch {
                    scaffoldState.bottomSheetState.expand()
                }
            },
        tint = Color.White
    )
}

@Composable
fun LoadingContent(message: String) {
    Text(text = message, modifier = Modifier.padding(16.dp))
}