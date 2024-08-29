package com.vsebastianvc.flickr.photogallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsebastianvc.flickr.data.local.PhotoEntity
import com.vsebastianvc.flickr.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhotoGalleryViewModel(
    private val photoRepository: PhotoRepository
) : ViewModel() {
    private val _photos = MutableStateFlow<List<PhotoEntity>>(emptyList())
    val photos = _photos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var currentPage = 1
    private var lastQuery: String? = null

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

    fun searchPhotos(query: String) {
        currentPage = 1
        lastQuery = query
        loadPhotos(query)
    }

    fun loadMorePhotos() {
        lastQuery?.let { query ->
            if (!_isLoading.value) {
                loadPhotos(query)
            }
        }
    }

    private fun loadPhotos(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newPhotos = photoRepository.getPhotos(query, currentPage, PAGE_SIZE)
                _photos.value = if (currentPage == 1) {
                    newPhotos
                } else {
                    _photos.value + newPhotos
                }
                currentPage++
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getLastQuery(): String? {
        return lastQuery
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
