package com.vsebastianvc.flickr.repository

import com.vsebastianvc.flickr.data.local.PhotoDao
import com.vsebastianvc.flickr.data.local.PhotoEntity
import com.vsebastianvc.flickr.network.FlickrApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoRepository(
    private val apiService: FlickrApiService,
    private val photoDao: PhotoDao
) {
    suspend fun getPhotos(query: String): List<PhotoEntity> {
        return withContext(Dispatchers.IO) {
            val localPhotos = photoDao.getAllPhotos()
            localPhotos.ifEmpty {
                val photos = apiService.searchPhotos(query)
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
                photoDao.insertAll(photoEntities)
                photoEntities
            }
        }
    }

    suspend fun getPhotoById(photoId: String): PhotoEntity? {
        return withContext(Dispatchers.IO) {
            photoDao.getPhotoById(photoId)
        }
    }
}