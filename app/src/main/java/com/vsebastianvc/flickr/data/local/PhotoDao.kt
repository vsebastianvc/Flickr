package com.vsebastianvc.flickr.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos WHERE id = :photoId")
    suspend fun getPhotoById(photoId: String): PhotoEntity?

    @Query("SELECT * FROM photos")
    suspend fun getAllPhotos(): List<PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<PhotoEntity>)
}