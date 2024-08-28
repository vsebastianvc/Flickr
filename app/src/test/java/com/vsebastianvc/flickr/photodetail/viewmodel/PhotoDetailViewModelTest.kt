package com.vsebastianvc.flickr.photodetail.viewmodel

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
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoDetailViewModelTest {

    @Mock
    private lateinit var mockPhotoRepository: PhotoRepository

    private lateinit var viewModel: PhotoDetailViewModel
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
    fun loadPhotoDetailLoadsPhotoFromRepositoryAndUpdatesState() = runTest {
        // Arrange
        val photoId = "1"
        val photoDetail = PhotoEntity(photoId, "Moon", "Nice moon", null, null, "url")
        `when`(mockPhotoRepository.getPhotoById(photoId)).thenReturn(photoDetail)

        // Act
        viewModel = PhotoDetailViewModel(mockPhotoRepository)
        viewModel.loadPhotoDetail(photoId)

        // Advance until the coroutine is finished
        testScheduler.advanceUntilIdle()

        // Assert
        assertEquals(photoDetail, viewModel.photoDetail.first())
    }
}
