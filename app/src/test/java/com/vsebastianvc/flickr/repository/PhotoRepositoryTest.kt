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
import org.mockito.Mockito.verifyNoInteractions
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

        val result = photoRepository.getPhotos("moon")

        verify(mockPhotoDao).getAllPhotos()
        verifyNoInteractions(mockApiService)
        assert(result == cachedPhotos)
    }

    @Test
    fun getPhotosFetchesFromNetworkWhenCacheIsEmptyAndSavesToDatabase() = runTest {
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
        `when`(mockPhotoDao.getAllPhotos()).thenReturn(emptyList())
        `when`(mockApiService.searchPhotos("moon")).thenReturn(newPhotoList)

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

        val result = photoRepository.getPhotos("moon")

        verify(mockPhotoDao).insertAll(expectedPhotoEntities)
        verify(mockApiService).searchPhotos("moon")
        assert(result == expectedPhotoEntities)
    }
}
