package com.example.maliva.view.search

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maliva.R
import com.example.maliva.adapter.destination.DestinationAdapter
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.state.Result
import com.example.maliva.data.utils.ObtainViewModelFactory
import com.example.maliva.databinding.ActivitySearchBinding
import com.example.maliva.view.filter.FilterFragment

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: DestinationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge display
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ObtainViewModelFactory.obtain(this)

        setupRecyclerView()
        setupObservers()

        // Retrieve filtered items from intent extras
        val filteredItems = intent.getSerializableExtra("FILTERED_ITEMS") as? ArrayList<DataItem>
        filteredItems?.let {
            // Update RecyclerView with filtered data
            updateRecyclerView(it)
        }

        // Retrieve selected category from intent extras
        val selectedCategory = intent.getStringExtra("SELECTED_CATEGORY")
        selectedCategory?.let {
            viewModel.filterDestinationsByCategory(it) // Initial filter by category
        }

        // Setup SearchView listener for query changes
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle query submission if needed
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.filterDestinations(it) // Trigger filtering on text change
                }
                return true
            }
        })

        binding.fabFilter.setOnClickListener {
            val bottomSheetFragment = FilterFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        // Setup click listener for back button
        binding.btnBack.setOnClickListener {
            finish() // Finish the activity when back button is clicked
        }
    }

    private fun setupRecyclerView() {
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        adapter = DestinationAdapter(this, itemLayoutResId = R.layout.item_destination_2)
        binding.rvSearch.adapter = adapter
    }

    private fun setupObservers() {
        // Observe filteredDestinations LiveData from viewModel
        viewModel.filteredDestinations.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    // Show loading state (optional)
                    // Example log for debugging
                    println("SearchActivity: Loading state")
                }
                is Result.Success -> {
                    // Update RecyclerView with filtered data
                    updateRecyclerView(result.data)
                    // Example log for debugging
                    println("SearchActivity: Success with data: ${result.data}")
                }
                is Result.Error -> {
                    // Show error message or handle error state (optional)
                    // Example log for debugging
                    println("SearchActivity: Error: ${result.error}")
                }
            }
        })
    }

    private fun updateRecyclerView(filteredItems: List<DataItem>) {
        if (filteredItems.isNotEmpty()) {
            adapter.submitList(filteredItems)
            binding.rvSearch.visibility = View.VISIBLE
            binding.imageNoItemsFound.visibility = View.GONE
        } else {
            adapter.submitList(emptyList())
            binding.rvSearch.visibility = View.GONE
            binding.imageNoItemsFound.visibility = View.VISIBLE
        }
    }
}
