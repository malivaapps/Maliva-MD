package com.example.maliva.view.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.view.gallery.GalleryViewModel

class ReviewsViewModelFactory (private val repository: DestinationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}