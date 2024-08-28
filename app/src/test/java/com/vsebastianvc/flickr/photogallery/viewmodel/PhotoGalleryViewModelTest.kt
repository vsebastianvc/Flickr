package com.vsebastianvc.flickr.photogallery.viewmodel

import com.vsebastianvc.flickr.data.local.PhotoEntity
import com.vsebastianvc.flickr.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    fun loadCachedPhotosLoadsPhotosFromRepositoryAndUpdatesState() = runTest {
        // Arrange
        val cachedPhotos = listOf(
            PhotoEntity("1", "Moon", "Nice moon", null, null, "url")
        )
        `when`(mockPhotoRepository.getCachedPhotos()).thenReturn(cachedPhotos)

        // Act
        viewModel = PhotoGalleryViewModel(mockPhotoRepository)

        // Advance until the coroutine is finished
        testScheduler.advanceUntilIdle()

        // Assert
        assertEquals(cachedPhotos, viewModel.photos.first())
        assertFalse(viewModel.isLoading.first())
    }

    @Test
    fun searchPhotosUpdatesPhotosAndLoadingState() = runTest {
        // Arrange
        val cachedPhotos = emptyList<PhotoEntity>()
        val networkPhotos = listOf(
            PhotoEntity("2", "Sun", "Bright sun", null, null, "url")
        )

        `when`(mockPhotoRepository.getCachedPhotos()).thenReturn(cachedPhotos)
        `when`(mockPhotoRepository.getPhotos("sun")).thenReturn(networkPhotos)

        viewModel = PhotoGalleryViewModel(mockPhotoRepository)

        // Act
        viewModel.searchPhotos("sun")

        // Advance until the coroutine is finished
        testScheduler.advanceUntilIdle()

        // Assert
        assertEquals(networkPhotos, viewModel.photos.first())
        assertFalse(viewModel.isLoading.first())
    }
}