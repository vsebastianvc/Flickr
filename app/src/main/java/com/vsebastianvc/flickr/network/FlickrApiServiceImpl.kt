package com.vsebastianvc.flickr.network

import com.googlecode.flickrjandroid.photos.SearchParameters
import com.googlecode.flickrjandroid.photos.PhotoList

class FlickrApiServiceImpl(private val flickrService: FlickrService) : FlickrApiService {
    override suspend fun searchPhotos(query: String): PhotoList {
        val photosInterface = flickrService.getPhotosInterface()
        val searchParameters = SearchParameters().apply {
            tags = arrayOf(query)
        }
        return photosInterface.search(searchParameters, 20, 1)
    }
}
