package com.vsebastianvc.flickr.network

import com.googlecode.flickrjandroid.photos.Photo
import com.googlecode.flickrjandroid.photos.PhotoList
import com.googlecode.flickrjandroid.photos.PhotosInterface
import com.googlecode.flickrjandroid.photos.SearchParameters
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
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
    fun searchPhotosReturnsCorrectData() = runBlocking {
        val dummyPhotos = PhotoList()
        dummyPhotos.add(Photo())
        `when`(mockPhotosInterface.search(any(SearchParameters::class.java), anyInt(), anyInt())).thenReturn(dummyPhotos)

        val result = flickrApiServiceImpl.searchPhotos("moon")
        verify(mockPhotosInterface).search(any(SearchParameters::class.java), eq(20), eq(1))
        assert(result == dummyPhotos)
    }
}
