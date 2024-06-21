package com.example.maliva.view.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maliva.R
import com.example.maliva.adapter.category.CategoryAdapter
import com.example.maliva.adapter.category.CategoryItem
import com.example.maliva.adapter.destination.DestinationAdapter
import com.example.maliva.adapter.recomendation.RecommendationAdapter
import com.example.maliva.data.preference.LoginPreferences
import com.example.maliva.data.preference.dataStore
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.response.RecommendationsItem
import com.example.maliva.data.state.Result
import com.example.maliva.data.utils.ObtainViewModelFactory
import com.example.maliva.databinding.FragmentHomeBinding
import com.example.maliva.view.search.SearchActivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), CategoryAdapter.OnCategoryClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var recommendationAdapter: RecommendationAdapter
    private lateinit var destinationAdapter: DestinationAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var loginPreferences: LoginPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ObtainViewModelFactory.obtain(requireActivity())
        loginPreferences = LoginPreferences.getInstance(requireContext().dataStore)

        setupRecyclerViews()
        setupObservers()
        setupSearchViewClickListener()
        fetchLastSearchQueryAndRecommendations()
    }

    private fun setupRecyclerViews() {
        // Setup RecyclerView for popular destinations
        binding.rvPopularDestination.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recommendationAdapter =
            RecommendationAdapter(requireContext(), itemLayoutResId = R.layout.item_destination)
        binding.rvPopularDestination.adapter = recommendationAdapter

        // Setup RecyclerView for recommended destinations
        binding.rvRecommendedDestination.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        destinationAdapter =
            DestinationAdapter(requireContext(), showRating = false, itemLayoutResId = R.layout.item_destination_2)
        binding.rvRecommendedDestination.adapter = destinationAdapter

        // Setup RecyclerView for categories
        binding.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(emptyList(), this)
        binding.rvCategory.adapter = categoryAdapter
    }

    private fun setupObservers() {
        // Observe remote destination data
        viewModel.getAllDestination().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d(TAG, "getAllDestination: Loading")
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    Log.d(TAG, "getAllDestination: Success")
                    binding.progressBar.visibility = View.GONE
                    getDestinations(result.data.data)
                    getCategories(result.data.data)
                }
                is Result.Error -> {
                    Log.e(TAG, "getAllDestination: Error")
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), getString(R.string.title_signup), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        // Observe remote recommendations data
        viewModel.getRecommendations().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "getRecommendations: Success")
                    binding.progressBar.visibility = View.GONE
                    getRecommendedDestinations(result.data.recommendations)
                }
                is Result.Error -> {
                    val errorMessage = "Error fetching recommendations: ${result.error}"
                    Log.e(TAG, errorMessage)
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Log.e(TAG, "getRecommendations: Error")
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun fetchLastSearchQueryAndRecommendations() {
        lifecycleScope.launch {
            loginPreferences.getLastQuery().collect { lastQuery ->
                lastQuery?.let { query ->
                    Log.d(TAG, "Last query: $query")
                    viewModel.getRecommendations(query)
                }
            }
        }
    }

    private fun getRecommendedDestinations(result: List<RecommendationsItem?>?) {
        result?.let {
            Log.d(TAG, "getRecommendedDestinations: Received data: ${it.size} items")
            val limitedList = it.take(100)
            recommendationAdapter.submitList(limitedList)
        }
    }

    private fun getDestinations(result: List<DataItem?>?) {
        result?.let {
            Log.d(TAG, "getDestinations: Received data: ${it.size} items")
            val limitedList = it.take(100)
            destinationAdapter.submitList(limitedList)
        }
    }

    private fun getCategories(result: List<DataItem?>?) {
        result?.let {
            val categories = it.mapNotNull { dataItem ->
                dataItem?.activities?.let { activity ->
                    CategoryItem(name = activity)
                }
            }.distinctBy { it.name }

            categoryAdapter = CategoryAdapter(categories, this)
            binding.rvCategory.adapter = categoryAdapter
        }
    }

    private fun setupSearchViewClickListener() {
        binding.searchView.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCategoryClick(category: String) {
        val intent = Intent(requireContext(), SearchActivity::class.java)
        intent.putExtra("SELECTED_CATEGORY", category)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvPopularDestination.adapter = null
        binding.rvRecommendedDestination.adapter = null
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}