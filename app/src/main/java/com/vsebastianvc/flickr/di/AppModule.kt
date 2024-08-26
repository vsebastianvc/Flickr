package com.vsebastianvc.flickr.di

import com.vsebastianvc.flickr.BuildConfig
import com.vsebastianvc.flickr.data.local.AppDatabase
import com.vsebastianvc.flickr.network.FlickrServiceImpl
import com.vsebastianvc.flickr.repository.PhotoRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().photoDao() }
    single {
        FlickrServiceImpl(
            apiKey = BuildConfig.FLICKR_API_KEY,
            apiSecret = BuildConfig.FLICKR_API_SECRET
        )
    }

    single { PhotoRepository(get(), get()) }

}