package com.vsebastianvc.flickr.di

import com.vsebastianvc.flickr.data.local.AppDatabase
import com.vsebastianvc.flickr.data.local.PhotoDao
import com.vsebastianvc.flickr.network.FlickrApiService
import com.vsebastianvc.flickr.network.FlickrService
import com.vsebastianvc.flickr.photogallery.viewmodel.PhotoGalleryViewModel
import com.vsebastianvc.flickr.repository.PhotoRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mockito.Mockito.mock

val testAppModule = module {
    single { mock(AppDatabase::class.java) }
    single { mock(PhotoDao::class.java) }
    single<FlickrService> { mock(FlickrService::class.java) }
    single<FlickrApiService> { mock(FlickrApiService::class.java) }
    single { PhotoRepository(flickrApiService = get(), photoDao = get()) }

    viewModel { PhotoGalleryViewModel(photoRepository = get()) }
}