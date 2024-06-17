package com.example.maliva.adapter.review

import android.graphics.text.LineBreaker
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maliva.R
import com.example.maliva.adapter.gallery.GalleryAdapter
import com.example.maliva.data.response.GalleryItem
import com.example.maliva.data.response.ReviewsItem
import com.example.maliva.data.response.ReviewsResponse
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class ReviewAdapter(private var reviewsItems: List<ReviewsItem>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviewItem = reviewsItems[position]
        holder.bind(reviewItem)
    }

    override fun getItemCount(): Int {
        return reviewsItems.size
    }

    fun updateReviews(newReviewsItems: List<ReviewsItem>) {
        reviewsItems = newReviewsItems
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
        private val reviewTextView: TextView = itemView.findViewById(R.id.reviewTextView)

        fun bind(review: ReviewsItem) {
            ratingTextView.text = review.rating?.toString()
            reviewTextView.text = review.review
            usernameTextView.text = review.username.toString()
            val createAtTimestamp = review.createAt
            if (createAtTimestamp != null) {
                val date = java.util.Date(createAtTimestamp)
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formattedDate = format.format(date)
                dateTextView.text = formattedDate
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                reviewTextView.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            }
        }
    }
}