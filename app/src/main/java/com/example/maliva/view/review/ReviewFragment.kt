package com.example.maliva.view.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maliva.R
import com.example.maliva.databinding.FragmentReviewBinding
import com.example.maliva.adapter.review.ReviewAdapter
import com.example.maliva.adapter.review.ReviewItem

class ReviewFragment : Fragment() {
    private lateinit var binding: FragmentReviewBinding
    private lateinit var reviews: List<ReviewItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the reviews list here
        reviews = listOf(
            ReviewItem(
                userImage = R.drawable.img_user1,
                username = "Freya Jayawardana",
                date = "8 Mei 2024",
                rating = 5,
                review = getString(R.string.description)
            ),
            ReviewItem(
                userImage = R.drawable.img_user1,
                username = "John Doe",
                date = "10 Mei 2024",
                rating = 4,
                review = getString(R.string.description)
            ),
        )

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = ReviewAdapter(reviews)
        binding.rvReview.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReview.adapter = adapter
    }
}
