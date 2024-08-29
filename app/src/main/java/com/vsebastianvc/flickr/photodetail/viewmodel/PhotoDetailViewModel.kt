package com.vsebastianvc.flickr.photodetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsebastianvc.flickr.data.local.PhotoEntity
import com.vsebastianvc.flickr.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhotoDetailViewModel(
    private val photoRepository: PhotoRepository
) : ViewModel() {
    private val _photoDetail = MutableStateFlow<PhotoEntity?>(null)
    val photoDetail = _photoDetail.asStateFlow()

    fun loadPhotoDetail(photoId: String) {
        viewModelScope.launch {
            _photoDetail.value = photoRepository.getPhotoById(photoId)
        }
    }
}