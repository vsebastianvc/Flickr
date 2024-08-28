package com.vsebastianvc.flickr.photogallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsebastianvc.flickr.data.local.PhotoEntity
import com.vsebastianvc.flickr.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PhotoGalleryViewModel(private val photoRepository: PhotoRepository) : ViewModel() {
    private val _photos = MutableStateFlow<List<PhotoEntity>>(emptyList())
    val photos: StateFlow<List<PhotoEntity>> = _photos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadCachedPhotos()
    }

    private fun loadCachedPhotos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cachedPhotos = photoRepository.getCachedPhotos()
                if (cachedPhotos.isNotEmpty()) {
                    _photos.value = cachedPhotos
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchPhotos(tag: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val networkPhotos = photoRepository.getPhotos(tag)
                _photos.value = networkPhotos
            } finally {
                _isLoading.value = false
            }
        }
    }
}
