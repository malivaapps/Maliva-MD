package com.example.maliva.view.gallery

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.response.DestinationResponse
import com.example.maliva.data.response.GalleryResponse
import com.example.maliva.data.response.UploadImageResponse
import com.example.maliva.data.state.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class GalleryViewModel(private val repository: DestinationRepository) : ViewModel() {

    fun getDestinationGallery(destinationId: String): LiveData<Result<GalleryResponse>> {
        return repository.getDestinationGallery(destinationId)
    }
    fun submitGallery(
        context: Context,
        destinationId: String,
        file: File?,
    ): LiveData<Result<Unit>> {
        val resultLiveData = MutableLiveData<Result<Unit>>()
        repository.uploadImage(context, destinationId,file)
            .observeForever { result ->
                when (result) {
                    is Result.Success -> {
                        resultLiveData.value = Result.Success(Unit)
                    }
                    is Result.Error -> {
                        resultLiveData.value = Result.Error(result.error)
                    }
                    is Result.Loading -> {
                        resultLiveData.value = Result.Loading
                    }
                }
            }
        return resultLiveData
    }
}
