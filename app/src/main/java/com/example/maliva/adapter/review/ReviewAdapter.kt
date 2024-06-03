package com.example.maliva.adapter.review

import android.graphics.text.LineBreaker
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maliva.R

class ReviewAdapter(private val reviews: List<ReviewItem>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userImageView: ImageView = itemView.findViewById(R.id.userImageView)
        private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
        private val reviewTextView: TextView = itemView.findViewById(R.id.reviewTextView)!!

        fun bind(review: ReviewItem) {
            with(itemView) {
                userImageView.setImageResource(review.userImage)
                usernameTextView.text = review.username
                dateTextView.text = review.date
                ratingTextView.text = review.rating.toString()
                reviewTextView.text = review.review
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    reviewTextView.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
                }
            }
        }
    }
}
