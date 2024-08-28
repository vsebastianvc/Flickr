package com.vsebastianvc.flickr.network

import com.googlecode.flickrjandroid.photos.Photo
import com.googlecode.flickrjandroid.photos.PhotoList
import com.googlecode.flickrjandroid.photos.PhotosInterface
import com.googlecode.flickrjandroid.photos.SearchParameters
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.eq
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class FlickrApiServiceImplTest {

    @Mock
    private lateinit var mockFlickrService: FlickrService

    @Mock
    private lateinit var mockPhotosInterface: PhotosInterface

    private lateinit var flickrApiServiceImpl: FlickrApiServiceImpl
    private lateinit var closeable: AutoCloseable

    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        `when`(mockFlickrService.getPhotosInterface()).thenReturn(mockPhotosInterface)
        flickrApiServiceImpl = FlickrApiServiceImpl(mockFlickrService)
    }

    @After
    fun tearDown() {
        closeable.close()
    }

    @Test
    fun searchPhotosReturnsCorrectData() = runTest {
        // Arrange
        val dummyPhotos = PhotoList()
        dummyPhotos.add(Photo())
        `when`(
            mockPhotosInterface.search(
                any(SearchParameters::class.java),
                anyInt(),
                anyInt()
            )
        ).thenReturn(dummyPhotos)

        // Act
        val result = flickrApiServiceImpl.searchPhotos("moon")

        // Assert
        verify(mockPhotosInterface).search(any(SearchParameters::class.java), eq(20), eq(1))
        assert(result == dummyPhotos)
    }

    @Test
    fun searchPhotosReturnsEmptyListWhenNoPhotosAreFound() = runTest {
        // Arrange
        val emptyPhotoList = PhotoList()
        `when`(
            mockPhotosInterface.search(
                any(SearchParameters::class.java),
                anyInt(),
                anyInt()
            )
        ).thenReturn(emptyPhotoList)

        // Act
        val result = flickrApiServiceImpl.searchPhotos("moon")

        // Assert
        verify(mockPhotosInterface).search(any(SearchParameters::class.java), eq(20), eq(1))
        assert(result?.isEmpty() ?: true)
    }
}
