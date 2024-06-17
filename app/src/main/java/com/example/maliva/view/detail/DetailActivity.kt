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
        val destination: DataItem? = intent.getParcelableExtra("DESTINATION_DATA")

        if (destination == null) {
            Log.e("DetailActivity", "Destination data is null")
            finish()
            return
        }

        findViewById<TextView>(R.id.titleTextView).text = destination.destinationName
        findViewById<TextView>(R.id.locationTextView).text = destination.location?.place
        findViewById<TextView>(R.id.priceTextView).text = "${destination.pricing} IDR"
        findViewById<TextView>(R.id.ratingTextView).text = destination.rating?.toString()

        // Use Glide to load images asynchronously
        Glide.with(this)
            .load(destination.images)
            .into(findViewById(R.id.destinationImageView))

        val viewPagerAdapter = ViewPagerAdapter(this, destination)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Description"
                1 -> "Gallery"
                2 -> "Review"
                else -> ""
            }
        }.attach()
    }
}