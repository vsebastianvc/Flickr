package com.vsebastianvc.flickr.photogallery.viewmodel

import com.vsebastianvc.flickr.data.local.PhotoEntity
import com.vsebastianvc.flickr.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoGalleryViewModelTest {

    @Mock
    private lateinit var mockPhotoRepository: PhotoRepository

    private lateinit var viewModel: PhotoGalleryViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadPhotosUpdatesStateAndLoadingFlag() = runTest {
        // Arrange
        val photoList = listOf(
            PhotoEntity("1", "Moon", "Nice moon", null, null, "url")
        )

        `when`(mockPhotoRepository.getCachedPhotos()).thenReturn(emptyList())
        `when`(mockPhotoRepository.getPhotos("moon", page = 1, pageSize = 20)).thenReturn(photoList)
        viewModel = PhotoGalleryViewModel(mockPhotoRepository)

        // Act
        viewModel.searchPhotos("moon")
        advanceUntilIdle() // Ensure all coroutines have completed

        // Assert
        assert(viewModel.photos.first() == photoList)
        assert(!viewModel.isLoading.first())
    }

    @Test
    fun loadMorePhotosAppendsToList() = runTest {
        // Arrange
        val initialPhotos = listOf(PhotoEntity("1", "Moon", "Nice moon", null, null, "url"))
        val morePhotos = listOf(PhotoEntity("2", "Sun", "Bright sun", null, null, "url"))

        `when`(mockPhotoRepository.getCachedPhotos()).thenReturn(emptyList())
        `when`(mockPhotoRepository.getPhotos("moon", page = 1, pageSize = 20)).thenReturn(initialPhotos)
        `when`(mockPhotoRepository.getPhotos("moon", page = 2, pageSize = 20)).thenReturn(morePhotos)
        viewModel = PhotoGalleryViewModel(mockPhotoRepository)

        // Act
        viewModel.searchPhotos("moon")
        advanceUntilIdle() // Complete initial load

        viewModel.loadMorePhotos()
        advanceUntilIdle() // Complete load more

        // Assert
        val allPhotos = viewModel.photos.first()
        assert(allPhotos.size == 2)
        assert(allPhotos[0] == initialPhotos[0])
        assert(allPhotos[1] == morePhotos[0])
        assert(!viewModel.isLoading.first())
    }
}