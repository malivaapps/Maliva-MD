package com.example.maliva.view.review

import android.content.Context
import android.media.Rating
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    fun submitReview(
        destinationId: String,
        rating: Int,
        review: String
    ): LiveData<Result<Unit>> {
        val resultLiveData = MutableLiveData<Result<Unit>>()
        repository.uploadReview(destinationId, rating, review)
            .observeForever { result ->
                when (result) {
                    is Result.Success -> {
                        resultLiveData.value = Result.Success(Unit)
                        Log.d("ReviewSubmission", "Response: $result")
                    }
                    is Result.Error -> {
                        resultLiveData.value = Result.Error(result.error)
                        Log.d("ReviewSubmission", "Response: $result")
                    }
                    is Result.Loading -> {
                        Log.d("ReviewSubmission", "Response: $result")
                        resultLiveData.value = Result.Loading
                    }
                }
            }
        return resultLiveData
    }
}
