package com.vsebastianvc.flickr.di

import com.vsebastianvc.flickr.photogallery.viewmodel.PhotoGalleryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
class AppModuleTest : KoinTest {

    private val photoGalleryViewModel: PhotoGalleryViewModel by inject()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Arrange
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        // Cleanup
        Dispatchers.resetMain()
        stopKoin()
    }

    @Test
    fun allModulesAreCorrectlyBound() {
        // Act & Assert
        checkModules {
            modules(testAppModule)
        }
    }

    @Test
    fun allComponentsWereInjectedSuccessfully() {
        // Arrange
        startKoin {
            modules(testAppModule)
        }

        // Act & Assert
        assertNotNull(photoGalleryViewModel)
    }
}