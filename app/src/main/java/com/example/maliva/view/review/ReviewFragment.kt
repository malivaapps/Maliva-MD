package com.example.maliva.view.review

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maliva.R
import com.example.maliva.data.state.Result

import com.example.maliva.databinding.FragmentReviewBinding
import com.example.maliva.adapter.review.ReviewAdapter
import com.example.maliva.adapter.review.ReviewItem
import com.example.maliva.data.api.ApiConfig
import com.example.maliva.data.preference.LoginPreferences
import com.example.maliva.data.preference.dataStore
import com.example.maliva.data.repository.DestinationRepository
class ReviewFragment : Fragment() {
    private lateinit var binding: FragmentReviewBinding
    private lateinit var reviewsViewModel: ReviewsViewModel
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var destinationId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        destinationId = requireArguments().getString(ARG_DESTINATION_ID) ?: ""

        // Initialize ViewModel
        val apiService = ApiConfig.getApiService("")
        val loginPreferences = LoginPreferences.getInstance(requireContext().dataStore)
        val repository = DestinationRepository.getInstance(apiService, loginPreferences)
        val viewModelFactory = ReviewsViewModelFactory(repository)
        reviewsViewModel = ViewModelProvider(this, viewModelFactory).get(ReviewsViewModel::class.java)

        setupRecyclerView()
        loadReviewsData()
    }

    private fun setupRecyclerView() {
        reviewAdapter = ReviewAdapter(emptyList())
        binding.rvReview.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReview.adapter = reviewAdapter
    }

    private fun loadReviewsData() {
        reviewsViewModel.getDestinationReviews(destinationId).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d(TAG, "Loading reviews data...")
                }
                is Result.Success -> {
                    val reviewsResponse = result.data
                    val reviews = reviewsResponse?.data ?: emptyList()
                    Log.d(TAG, "Received ${reviews.size} reviews")
                    reviewAdapter.updateReviews(reviews)
                }
                is Result.Error -> {
                    val errorMessage = result.error
                    Log.e(TAG, "Error loading reviews: $errorMessage")
                }
                else -> {
                    Log.w(TAG, "Unknown result type")
                }
            }
        })
    }

    companion object {
        private const val ARG_DESTINATION_ID = "destination_id"
        private const val TAG = "ReviewFragment"

        fun newInstance(destinationId: String): ReviewFragment {
            val fragment = ReviewFragment()
            val args = Bundle().apply {
                putString(ARG_DESTINATION_ID, destinationId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}