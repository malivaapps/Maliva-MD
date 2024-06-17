package com.example.maliva.adapter.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maliva.R
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.response.GalleryItem

import com.google.android.material.imageview.ShapeableImageView


class GalleryAdapter(private var galleryItems: List<GalleryItem>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val galleryItem = galleryItems[position]
        holder.bind(galleryItem)
    }

    override fun getItemCount(): Int {
        return galleryItems.size
    }

    fun updateGallery(newGalleryItems: List<GalleryItem>) {
        galleryItems = newGalleryItems
        notifyDataSetChanged()
    }

    inner class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.destinationImageView)

        fun bind(galleryItem: GalleryItem) {
            Glide.with(itemView.context)
                .load(galleryItem.url)
                .into(imageView)
        }
    }
}