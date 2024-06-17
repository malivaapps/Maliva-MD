package com.example.maliva.view.review

import android.content.Context
import android.media.Rating
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.response.GalleryResponse
import com.example.maliva.data.response.ReviewsResponse
import com.example.maliva.data.response.UploadImageResponse
import com.example.maliva.data.response.UploadReviewResponse
import com.example.maliva.data.state.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ReviewsViewModel (private val repository: DestinationRepository) : ViewModel() {

    fun getDestinationReviews(reviewsId: String): LiveData<Result<ReviewsResponse>> {
        return repository.getDestinationReviews(reviewsId)
    }

    fun submitReview(context: Context, destinationId: String, rating: Int, review: String): LiveData<Result<UploadReviewResponse>> = liveData(
        Dispatchers.IO) {
        emit(Result.Loading)
        val result = withContext(Dispatchers.IO) {
            repository.uploadReview(context, destinationId, rating, review).value
        }
        result?.let { emit(it) }
    }
}
