package com.example.maliva.adapter.destination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maliva.R

class DestinationAdapter(
    private val destinationList: List<DestinationItem>,
    private val viewType: Int,
    private val showRating: Boolean = true
) : RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        // Inflate item_destination.xml or item_saved.xml based on viewType
        val layoutRes = when (viewType) {
            VIEW_TYPE_POPULAR -> R.layout.item_destination
            VIEW_TYPE_SAVED -> R.layout.item_saved
            else -> R.layout.item_destination_2 // Default or additional view type
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return DestinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val destination = destinationList[position]
        holder.bind(destination)
    }

    override fun getItemCount(): Int = destinationList.size

    override fun getItemViewType(position: Int): Int {
        // Return the view type based on the passed viewType parameter
        return viewType
    }

    inner class DestinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val destinationImageView: ImageView = itemView.findViewById(R.id.destinationImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val ratingTextView: TextView? = itemView.findViewById(R.id.ratingTextView)

        fun bind(destination: DestinationItem) {
            destinationImageView.setImageResource(destination.image)
            titleTextView.text = destination.title
            locationTextView.text = destination.location
            priceTextView.text = "${destination.price} IDR"
            ratingTextView?.let {
                if (showRating) {
                    it.text = destination.rating.toString()
                } else {
                    it.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        const val VIEW_TYPE_POPULAR = 0
        const val VIEW_TYPE_SAVED = 1
        const val VIEW_TYPE_RECOMMENDED = 2
    }
}
