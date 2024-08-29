package com.vsebastianvc.flickr.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vsebastianvc.flickr.photodetail.ui.PhotoDetailScreen
import com.vsebastianvc.flickr.photogallery.ui.PhotoGalleryScreen

@Composable
fun FlickrNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = FlickrScreens.PhotoGallery.name
    ) {

        composable(FlickrScreens.PhotoGallery.name) { PhotoGalleryScreen(navController) }

        val photoDetailRoute = "${FlickrScreens.PhotoDetail.name}/{photoId}"
        composable(photoDetailRoute) { backStackEntry ->
            PhotoDetailScreen(photoId = backStackEntry.arguments?.getString("photoId"), navController)
        }
    }
}
