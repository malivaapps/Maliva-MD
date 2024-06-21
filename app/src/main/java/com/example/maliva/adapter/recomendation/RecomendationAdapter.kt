package com.example.maliva.adapter.recomendation

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maliva.R
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.response.RecommendationsItem
import com.example.maliva.view.detail.DetailActivity

class RecommendationAdapter(
    private val context: Context,
    private val showRating: Boolean = true,
    private val itemLayoutResId: Int
) : ListAdapter<RecommendationsItem, RecommendationAdapter.DestinationViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecommendationsItem>() {
            override fun areItemsTheSame(oldItem: RecommendationsItem, newItem: RecommendationsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RecommendationsItem, newItem: RecommendationsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(itemLayoutResId, parent, false)
        return DestinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val recommendation = getItem(position)
        holder.bind(recommendation)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("RECOMMENDATION_DATA", recommendation)
                putExtra("SOURCE", "RECOMMENDATION")
            }
            context.startActivity(intent)
        }
    }

    inner class DestinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val destinationImageView: ImageView = itemView.findViewById(R.id.destinationImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val ratingTextView: TextView? = itemView.findViewById(R.id.ratingTextView)

        fun bind(recommendation: RecommendationsItem) {
            Glide.with(itemView.context).load(recommendation.images).into(destinationImageView)
            titleTextView.text = recommendation.destinationName
            locationTextView.text = recommendation.location?.tempat
            priceTextView.text = "${recommendation.price} IDR"
            ratingTextView?.let {
                if (showRating && recommendation.rating != null) {
                    it.text = recommendation.rating.toString()
                    it.visibility = View.VISIBLE
                } else {
                    it.visibility = View.GONE
                }
            }
        }
    }
}