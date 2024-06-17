package com.example.maliva.view.detail

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.maliva.R
import com.example.maliva.adapter.viewpager.ViewPagerAdapter
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.response.GalleryItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        // Retrieve DataItem from intent extras
        val destination: DataItem? = intent.getParcelableExtra("DESTINATION_DATA")

        if (destination == null) {
            Log.e("DetailActivity", "Destination data is null")
            // Handle the case where destination is null (e.g., show an error message or finish the activity)
            finish()
            return
        }

        // Proceed with setting up UI using destination
        findViewById<TextView>(R.id.titleTextView).text = destination.destinationName
        findViewById<TextView>(R.id.locationTextView).text = destination.location?.place
        findViewById<TextView>(R.id.priceTextView).text = "${destination.pricing} IDR"
        findViewById<TextView>(R.id.ratingTextView).text = destination.rating?.toString()

        Glide.with(this)
            .load(destination.images)
            .into(findViewById(R.id.destinationImageView))

        // Generate or fetch GalleryItem ID based on destination data
        val galleryItemId = generateGalleryItemId(destination)
        val galleryItem = GalleryItem(galleryItemId, destination.id.toString())

        // Initialize ViewPagerAdapter with destination and galleryItem
        val viewPagerAdapter = ViewPagerAdapter(this, destination, galleryItem)
        viewPager.adapter = viewPagerAdapter

        // Configure TabLayoutMediator after setting the adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Description"
                1 -> "Gallery"
                2 -> "Review"
                else -> ""
            }
        }.attach()
    }

    // Method to generate or fetch GalleryItem ID
    private fun generateGalleryItemId(destination: DataItem): String {
        // This is a simple example; replace with actual logic to generate or fetch ID
        return "gallery_" + destination.id
    }
}