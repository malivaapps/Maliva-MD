package com.example.maliva.view.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.response.DestinationResponse
import com.example.maliva.data.response.GalleryResponse
import com.example.maliva.data.state.Result

class GalleryViewModel(private val repository: DestinationRepository) : ViewModel() {
    private var _galleryData: LiveData<Result<GalleryResponse>>? = null

    fun getDestinationGallery(destinationId: String): LiveData<Result<GalleryResponse>> {
        return repository.getDestinationGallery(destinationId)
    }
    fun refreshDestinationGallery(destinationId: String) {
        _galleryData = repository.getDestinationGallery(destinationId)
    }
}
