package com.vsebastianvc.flickr.network

import com.googlecode.flickrjandroid.photos.PhotoList

interface FlickrApiService {
    suspend fun searchPhotos(query: String): PhotoList
}