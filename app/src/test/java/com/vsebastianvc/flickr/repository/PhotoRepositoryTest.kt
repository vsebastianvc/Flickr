package com.vsebastianvc.flickr.repository

import com.googlecode.flickrjandroid.photos.Photo
import com.googlecode.flickrjandroid.photos.PhotoList
import com.vsebastianvc.flickr.data.local.PhotoDao
import com.vsebastianvc.flickr.data.local.PhotoEntity
import com.vsebastianvc.flickr.network.FlickrApiService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.text.SimpleDateFormat
import java.util.Date

class PhotoRepositoryTest {


    @Mock
    private lateinit var mockApiService: FlickrApiService

    @Mock
    private lateinit var mockPhotoDao: PhotoDao

    private lateinit var photoRepository: PhotoRepository
    private lateinit var closeable: AutoCloseable

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
    private lateinit var fixedDate: Date

    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        photoRepository = PhotoRepository(mockApiService, mockPhotoDao)
        fixedDate = dateFormatter.parse("2024-08-25") as Date
    }

    @After
    fun tearDown() {
        closeable.close()
    }

    @Test
    fun getPhotosReturnsPhotosFromDAOWhenAvailable() = runTest {
        // Arrange
        val cachedPhotos = listOf(
            PhotoEntity(
                id = "1",
                title = "Moon",
                description = "Nice moon",
                dateTaken = fixedDate,
                datePosted = fixedDate,
                imageUrl = "url"
            )
        )
        `when`(mockPhotoDao.getAllPhotos()).thenReturn(cachedPhotos)

        // Act
        val result = photoRepository.getCachedPhotos()

        // Assert
        verify(mockPhotoDao).getAllPhotos() // Ensure getAllPhotos was called
        assert(result == cachedPhotos) // Ensure the result matches expected
    }

    @Test
    fun getPhotosFetchesFromAPIAndCachesThem() = runTest {
        // Arrange
        val newPhotoList = PhotoList()
        val newPhoto = Photo().apply {
            id = "2"
            title = "New Moon"
            description = "Nice moon"
            dateTaken = fixedDate
            datePosted = fixedDate
            url = "url"
        }
        newPhotoList.add(newPhoto)

        val expectedPhotoEntities = newPhotoList.map { photo ->
            PhotoEntity(
                id = photo.id,
                title = photo.title,
                description = photo.description,
                dateTaken = fixedDate,
                datePosted = fixedDate,
                imageUrl = photo.mediumUrl
            )
        }

        `when`(mockApiService.searchPhotos("moon")).thenReturn(newPhotoList)
        `when`(mockPhotoDao.getAllPhotos()).thenReturn(emptyList())

        // Act
        val result = photoRepository.getPhotos("moon")

        // Assert
        verify(mockPhotoDao).deleteAllPhotos() // Ensure the cache was cleared
        verify(mockPhotoDao).insertAll(expectedPhotoEntities) // Ensure new photos were cached
        verify(mockApiService).searchPhotos("moon") // Ensure the API was called
        assert(result == expectedPhotoEntities) // Ensure the result matches expected
    }

    @Test
    fun getPhotosReturnsEmptyListWhenAPIReturnsNull() = runTest {
        // Arrange
        `when`(mockApiService.searchPhotos("moon")).thenReturn(null)

        // Act
        val result = photoRepository.getPhotos("moon")

        // Assert
        verify(mockPhotoDao).deleteAllPhotos() // Ensure the cache was cleared
        verify(mockPhotoDao).insertAll(emptyList()) // Ensure empty list was cached
        assert(result.isEmpty()) // Ensure the result is empty
    }
}