package com.vsebastianvc.flickr.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String? = "",
    var dateTaken: Date? = null,
    var datePosted: Date? = null,
    val imageUrl: String
)
