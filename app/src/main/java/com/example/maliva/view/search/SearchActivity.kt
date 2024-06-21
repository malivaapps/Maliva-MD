package com.example.maliva.view.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maliva.R
import com.example.maliva.adapter.destination.DestinationAdapter
import com.example.maliva.data.preference.LoginPreferences
import com.example.maliva.data.preference.dataStore
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.state.Result
import com.example.maliva.data.utils.ObtainViewModelFactory
import com.example.maliva.databinding.ActivitySearchBinding
import com.example.maliva.view.filter.FilterFragment
import com.example.maliva.view.home.HomeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: DestinationAdapter
    private lateinit var loginPreferences: LoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.GONE

        loginPreferences = LoginPreferences.getInstance(this.dataStore)

        viewModel = ObtainViewModelFactory.obtain(this)

        setupRecyclerView()
        setupObservers()
        setupListeners()

        val filteredItems = intent.getSerializableExtra("FILTERED_ITEMS") as? ArrayList<DataItem>
        filteredItems?.let {
            Log.d(TAG, "Filtered items received: ${it.size}")
            updateRecyclerView(it)
        }

        // Retrieve selected category from intent extras
        val selectedCategory = intent.getStringExtra("SELECTED_CATEGORY")
        selectedCategory?.let {
            Log.d(TAG, "Selected category: $it")
            viewModel.filterDestinationsByCategory(it)
        }

    }

    private fun setupRecyclerView() {
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        adapter = DestinationAdapter(this, itemLayoutResId = R.layout.item_destination_2)
        binding.rvSearch.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.searchResults.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d(TAG, "Search results loading")
                    if (viewModel.filteredDestinations.value !is Result.Loading) {
                        Log.d(TAG, "Showing progress bar for search results")
                        binding.progressBar.visibility = View.GONE
                    } else {
                        Log.d(TAG, "Filtered destinations are also loading, waiting...")
                    }
                }
                is Result.Success -> {
                    Log.d(TAG, "Search results success: ${result.data.size} items")
                    binding.progressBar.visibility = View.GONE
                    updateRecyclerView(result.data)
                }
                is Result.Error -> {
                    Log.e(TAG, "Error loading search results: ${result.error}")
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        viewModel.filteredDestinations.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d(TAG, "Filtered destinations loading")
                    if (viewModel.searchResults.value !is Result.Loading) {
                        Log.d(TAG, "Showing progress bar for filtered destinations")
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        Log.d(TAG, "Search results are also loading, waiting...")
                    }
                }
                is Result.Success -> {
                    Log.d(TAG, "Filtered destinations success: ${result.data.size} items")
                    binding.progressBar.visibility = View.GONE
                    updateRecyclerView(result.data)
                }
                is Result.Error -> {
                    Log.e(TAG, "Error loading filtered destinations: ${result.error}")
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun setupListeners() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchDestinations(it)
                    passQueryBackToHomeFragment(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchDestinations(it)

                }
                return true
            }
        })

        binding.fabFilter.setOnClickListener {
            val bottomSheetFragment = FilterFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }



    private fun passQueryBackToHomeFragment(query: String) {
        val intent = Intent().apply {
            putExtra("SEARCH_QUERY", query)
        }
        setResult(Activity.RESULT_OK, intent)
    }

    private fun updateRecyclerView(items: List<DataItem>) {
        Log.d(TAG, "Updating RecyclerView with ${items.size} items")
        if (items.isNotEmpty()) {
            adapter.submitList(items)
            binding.rvSearch.visibility = View.VISIBLE
            binding.imageNoItemsFound.visibility = View.GONE
        } else {
            adapter.submitList(emptyList())
            binding.rvSearch.visibility = View.GONE
            binding.imageNoItemsFound.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val TAG = "SearchActivity"
    }
}