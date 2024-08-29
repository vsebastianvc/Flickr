package com.vsebastianvc.flickr.network

import com.googlecode.flickrjandroid.photos.PhotosInterface

interface FlickrService {
    fun getPhotosInterface(): PhotosInterface
}