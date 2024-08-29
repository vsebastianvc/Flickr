package com.vsebastianvc.flickr.network

import com.googlecode.flickrjandroid.photos.Extras
import com.googlecode.flickrjandroid.photos.PhotoList
import com.googlecode.flickrjandroid.photos.SearchParameters

class FlickrApiServiceImpl(private val flickrService: FlickrService) : FlickrApiService {
    override suspend fun searchPhotos(query: String, page: Int, pageSize: Int): PhotoList? {
        val photosInterface = flickrService.getPhotosInterface()
        val searchParameters = SearchParameters().apply {
            tags = arrayOf(query)
            setExtras(setOf(
                Extras.DESCRIPTION,
                Extras.DATE_TAKEN,
                Extras.DATE_UPLOAD
            ))
        }
        return photosInterface.search(searchParameters, pageSize, page)
    }
}
