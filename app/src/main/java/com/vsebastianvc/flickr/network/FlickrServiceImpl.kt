package com.vsebastianvc.flickr.network

import com.googlecode.flickrjandroid.Flickr
import com.googlecode.flickrjandroid.photos.PhotosInterface

class FlickrServiceImpl(apiKey: String, apiSecret: String): FlickrService {
    private val flickr = Flickr(apiKey, apiSecret)

    override fun getPhotosInterface(): PhotosInterface = flickr.photosInterface
}