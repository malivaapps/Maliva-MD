package com.example.maliva.adapter.destination

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
import com.example.maliva.view.detail.DetailActivity

class DestinationAdapter(
    private val context: Context,
    private val showRating: Boolean = true,
    private val itemLayoutResId: Int,
) : ListAdapter<DataItem, DestinationAdapter.DestinationViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(itemLayoutResId, parent, false)
        return DestinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val destination = getItem(position)
        holder.bind(destination)

        Log.d("DestinationAdapter", "Location: ${destination.location?.place}")

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("DESTINATION_DATA", destination)
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

        fun bind(destination: DataItem) {
            Glide.with(itemView.context).load(destination.images).into(destinationImageView)
            titleTextView.text = destination.destinationName
            locationTextView.text = destination.location?.place
            priceTextView.text = "${destination.pricing} IDR"
            ratingTextView?.let {
                if (showRating) {
                    it.text = destination.rating.toString()
                } else {
                    it.visibility = View.GONE
                }
            }
        }
    }
}