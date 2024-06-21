package com.example.maliva.view.review

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.maliva.R
import com.example.maliva.data.api.ApiConfig
import com.example.maliva.data.preference.LoginPreferences
import com.example.maliva.data.preference.dataStore
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.state.Result
import com.example.maliva.view.viewmodelfactory.ViewModelFactory
import com.google.android.material.imageview.ShapeableImageView

private const val ARG_DESTINATION_NAME = "destination_name"
private const val ARG_DESTINATION_ID = "destination_id"
private const val ARG_DESTINATION_IMAGE = "destination_image" // Define constant for destination image

class ReviewDialogFragment : DialogFragment() {

    private lateinit var ratingBar: RatingBar
    private lateinit var reviewEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var destinationName: String
    private lateinit var destinationId: String

    // Initialize viewModel using viewModel delegate and ReviewsViewModelFactory
    private val viewModel: ReviewsViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialogTheme) // Apply custom dialog theme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_review_dialog, container, false)

        ratingBar = view.findViewById(R.id.ratingBar)
        reviewEditText = view.findViewById(R.id.reviewEditText)
        submitButton = view.findViewById(R.id.submitButton)

        // Retrieve destinationName, destinationId, and destinationImage from arguments
        destinationName = arguments?.getString(ARG_DESTINATION_NAME) ?: ""
        destinationId = arguments?.getString(ARG_DESTINATION_ID) ?: ""
        val destinationImage = arguments?.getString(ARG_DESTINATION_IMAGE) ?: ""

        Log.d("ReviewDialogFragment", "Destination ID: $destinationId")
        view.findViewById<TextView>(R.id.tv_name).text = destinationName

        // Load destination image using Glide
        val ivDestination = view.findViewById<ShapeableImageView>(R.id.iv_destination)
        Glide.with(requireContext())
            .load(destinationImage)
            .into(ivDestination)

        // Submit button click listener
        submitButton.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            val review = reviewEditText.text.toString()

            if (rating < 1 || rating > 5) {
                Toast.makeText(
                    requireContext(),
                    "Please rate between 1 and 5 stars",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                submitReview(destinationId, rating, review)
            }
        }

        return view
    }

    private fun submitReview(destinationId: String, rating: Int, review: String) {
        if (destinationId.isNotEmpty() && rating in 1..5 && review.isNotEmpty()) {
            viewModel.submitReview(destinationId, rating, review).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        // Handle loading state if needed
                    }
                    is Result.Success -> {
                        Toast.makeText(requireContext(), "Review submitted successfully", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                    is Result.Error -> {
                        Log.e("ReviewSubmission", "Failed to submit review: ${result.error}")
                        Toast.makeText(requireContext(), "Failed to submit review: ${result.error}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Please fill all required fields",
                Toast.LENGTH_SHORT
            ).show()
        }
    }




    companion object {
        // Static method to create a new instance of ReviewDialogFragment with destinationName, destinationId, and destinationImage
        fun newInstance(destinationName: String, destinationId: String, destinationImage: String): ReviewDialogFragment {
            val fragment = ReviewDialogFragment()
            val args = Bundle().apply {
                putString(ARG_DESTINATION_NAME, destinationName)
                putString(ARG_DESTINATION_ID, destinationId)
                putString(ARG_DESTINATION_IMAGE, destinationImage) // Pass destinationImage in arguments
            }
            fragment.arguments = args
            return fragment
        }
    }
}