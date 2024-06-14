package com.example.maliva.view.detail

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.maliva.R
import com.example.maliva.adapter.viewpager.ViewPagerAdapter
import com.example.maliva.data.response.DataItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        val destination: DataItem? = intent.getParcelableExtra("DESTINATION_DATA")

        destination?.let {
            findViewById<TextView>(R.id.titleTextView).text = it.destinationName
            findViewById<TextView>(R.id.locationTextView).text = it.location?.place
            findViewById<TextView>(R.id.priceTextView).text = "${it.pricing} IDR"
            findViewById<TextView>(R.id.ratingTextView).text = it.rating?.toString()

            Glide.with(this)
                .load(it.images)
                .into(findViewById(R.id.destinationImageView))

            val viewPagerAdapter = ViewPagerAdapter(this, it)
            viewPager.adapter = viewPagerAdapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Description"
                    1 -> "Gallery"
                    2 -> "Review"
                    else -> null
                }
            }.attach()
        }
    }
}