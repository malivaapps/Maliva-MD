package com.example.maliva.view.detail

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.maliva.R
import com.example.maliva.adapter.viewpager.ViewPagerAdapter
import com.example.maliva.data.database.FavoriteDestination
import com.example.maliva.data.database.FavoriteDestinationDatabase
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.response.GalleryItem
import com.example.maliva.data.repository.FavoriteDestinationRepository
import com.example.maliva.data.response.Location
import com.example.maliva.data.response.RecommendationsItem
import com.example.maliva.view.viewmodelfactory.DetailViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var fabFavorite: FloatingActionButton
    private var isFavorite: Boolean = false
    private lateinit var destination: DataItem

    private val viewModel: DetailViewModel by viewModels {
        val favoriteDestinationDao =
            FavoriteDestinationDatabase.getDatabase(application).favoriteDestinationDao()
        val repository = FavoriteDestinationRepository(favoriteDestinationDao)
        DetailViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Initialize views and variables
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val backButton: ImageView = findViewById(R.id.btn_back)
        fabFavorite = findViewById(R.id.fab_favorite)

        // Determine the source of the intent
        val source = intent.getStringExtra("SOURCE")
        when (source) {
            "DESTINATION" -> {
                // Retrieve DataItem directly from intent extras
                destination = intent.getParcelableExtra("DESTINATION_DATA")!!
            }

            "RECOMMENDATION" -> {
                val recommendation =
                    intent.getParcelableExtra<RecommendationsItem>("RECOMMENDATION_DATA")
                if (recommendation != null) {
                    destination = convertRecommendationToDestination(recommendation)
                } else {
                    showToast("Invalid Recommendation Data")
                    finish()
                    return
                }
            }

            "FAVORITE" -> {
                val favorite = intent.getParcelableExtra<FavoriteDestination>("FAVORITE_DATA")
                if (favorite != null) {
                    destination = convertFavoriteToDataItem(favorite)
                } else {
                    showToast("Invalid Favorite Data")
                    finish()
                    return
                }
            }

            else -> {
                showToast("Invalid Source")
                finish()
                return
            }
        }

        backButton.setOnClickListener {
            finish()
        }
        updateUI()

        // Initialize ViewPager and TabLayout
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

        // Check if the destination is initially marked as favorite
        lifecycleScope.launch {
            isFavorite = viewModel.isFavorite(destination.id!!)
            updateFavoriteState()

            // Set initial FAB icon based on favorite status
            fabFavorite.setImageResource(if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart)
        }

        // Toggle favorite status on FAB click
        fabFavorite.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteState()
            showToast(if (isFavorite) "Added to favorites" else "Removed from favorites")

            // Update FAB icon after state change
            fabFavorite.setImageResource(if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart)
        }
    }

    private fun updateUI() {
        findViewById<TextView>(R.id.titleTextView).text = destination.destinationName
        findViewById<TextView>(R.id.locationTextView).text = destination.location?.place
        findViewById<TextView>(R.id.priceTextView).text = "${destination.pricing} IDR"
        findViewById<TextView>(R.id.ratingTextView).text = destination.rating?.toString()

        Glide.with(this)
            .load(destination.images)
            .into(findViewById(R.id.destinationImageView))
    }

    private fun updateFavoriteState() {
        lifecycleScope.launch {
            if (isFavorite) {
                viewModel.saveDestination(
                    FavoriteDestination(
                        id = destination.id!!,
                        destinationName = destination.destinationName,
                        location = destination.location?.place,
                        pricing = destination.pricing,
                        rating = destination.rating,
                        imageUrl = destination.images,
                        description = destination.description,
                        facilities = destination.facilities,
                        accessibility = destination.accessibility,
                        link = destination.link
                    )
                )
            } else {
                viewModel.deleteDestination(destination.id!!)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun convertFavoriteToDataItem(favorite: FavoriteDestination): DataItem {
        return DataItem(
            destinationName = favorite.destinationName,
            location = Location(place = favorite.location),
            pricing = favorite.pricing,
            rating = favorite.rating,
            images = favorite.imageUrl,
            id = favorite.id,
            description = favorite.description,
            facilities = favorite.facilities,
            accessibility = favorite.accessibility,
            link = favorite.link
        )
    }

    private fun convertRecommendationToDestination(recommendation: RecommendationsItem): DataItem {
        return DataItem(
            description = recommendation.description,
            category = recommendation.category,
            accessibility = recommendation.accessibility,
            address = recommendation.address,
            images = recommendation.images,
            rating = recommendation.rating,
            facilities = recommendation.facilities,
            pricing = recommendation.price,
            id = recommendation.id,
            location = recommendation.location?.let { Location(place = it.tempat) },
            activities = recommendation.activities,
            link = recommendation.link,
            url = null,
            destinationName = recommendation.destinationName
        )
    }
}