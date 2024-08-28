package com.vsebastianvc.flickr.repository

import com.vsebastianvc.flickr.data.local.PhotoDao
import com.vsebastianvc.flickr.data.local.PhotoEntity
import com.vsebastianvc.flickr.network.FlickrApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoRepository(
    private val flickrApiService: FlickrApiService,
    private val photoDao: PhotoDao
) {

    suspend fun getCachedPhotos(): List<PhotoEntity> {
        return withContext(Dispatchers.IO) {
            photoDao.getAllPhotos()
        }
    }

    suspend fun getPhotos(query: String): List<PhotoEntity> {
        return withContext(Dispatchers.IO) {
            val photos = flickrApiService.searchPhotos(query) ?: emptyList()
            val photoEntities = photos.map { photo ->
                PhotoEntity(
                    id = photo.id,
                    title = photo.title,
                    description = photo.description,
                    dateTaken = photo.dateTaken,
                    datePosted = photo.datePosted,
                    imageUrl = photo.mediumUrl
                )
            }
            clearCachedPhotos()
            photoDao.insertAll(photoEntities)
            photoEntities
        }
    }

    private suspend fun clearCachedPhotos() {
        withContext(Dispatchers.IO) {
            photoDao.deleteAllPhotos()
        }
    }

    suspend fun getPhotoById(photoId: String): PhotoEntity? {
        return withContext(Dispatchers.IO) {
            photoDao.getPhotoById(photoId)
        }
    }
}