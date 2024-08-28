package com.vsebastianvc.flickr.di

import com.vsebastianvc.flickr.BuildConfig
import com.vsebastianvc.flickr.data.local.AppDatabase
import com.vsebastianvc.flickr.network.FlickrApiService
import com.vsebastianvc.flickr.network.FlickrApiServiceImpl
import com.vsebastianvc.flickr.network.FlickrService
import com.vsebastianvc.flickr.network.FlickrServiceImpl
import com.vsebastianvc.flickr.photogallery.viewmodel.PhotoGalleryViewModel
import com.vsebastianvc.flickr.repository.PhotoRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Database instance
    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().photoDao() }

    // Flickr Service
    single<FlickrService> {
        FlickrServiceImpl(
            apiKey = BuildConfig.FLICKR_API_KEY,
            apiSecret = BuildConfig.FLICKR_API_SECRET
        )
    }

    // Flickr API Service
    single<FlickrApiService> {
        FlickrApiServiceImpl(flickrService = get())
    }

    // PhotoRepository with FlickrApiService and PhotoDao
    single { PhotoRepository(flickrApiService = get(), photoDao = get()) }

    // ViewModel for PhotoGallery
    viewModel { PhotoGalleryViewModel(photoRepository = get()) }

}