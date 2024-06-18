package com.example.maliva.adapter.destination

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
import com.example.maliva.data.database.FavoriteDestination

class SavedDestinationAdapter :
    ListAdapter<FavoriteDestination, SavedDestinationAdapter.SavedDestinationViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteDestination>() {
            override fun areItemsTheSame(oldItem: FavoriteDestination, newItem: FavoriteDestination): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteDestination, newItem: FavoriteDestination): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedDestinationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_saved, parent, false)
        return SavedDestinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedDestinationViewHolder, position: Int) {
        val destination = getItem(position)
        holder.bind(destination)
    }

    inner class SavedDestinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val destinationImageView: ImageView = itemView.findViewById(R.id.destinationImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)

        fun bind(destination: FavoriteDestination) {
            destinationImageView.setImageResource(0) // Clear previous image if needed
            Glide.with(itemView.context).load(destination.imageUrl).into(destinationImageView)
            titleTextView.text = destination.destinationName
            locationTextView.text = destination.location ?: ""
            priceTextView.text = "${destination.pricing ?: 0} IDR"
        }
    }

}
