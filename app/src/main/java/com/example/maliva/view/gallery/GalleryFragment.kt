package com.example.maliva.view.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maliva.R
import com.example.maliva.adapter.gallery.GalleryAdapter

class GalleryFragment : Fragment(R.layout.fragment_gallery) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_gallery)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        val images = listOf(
            R.drawable.img_destination1,
            R.drawable.img_destination2,
            R.drawable.img_destination3,
            R.drawable.img_destination1,
            R.drawable.img_destination2,
            R.drawable.img_destination3,
            R.drawable.img_destination1,
            R.drawable.img_destination2,
            R.drawable.img_destination3,
            R.drawable.img_destination1,
            R.drawable.img_destination2,
            R.drawable.img_destination3,
        )

        recyclerView.adapter = GalleryAdapter(images)
    }
}
