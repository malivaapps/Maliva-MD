package com.example.maliva.adapter.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.maliva.R
import com.google.android.material.imageview.ShapeableImageView

class GalleryAdapter(private val images: List<Int>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val destinationImageView: ShapeableImageView = itemView.findViewById(R.id.destinationImageView)

        fun bind(imageResId: Int) {
            destinationImageView.setImageResource(imageResId)
        }
    }
}