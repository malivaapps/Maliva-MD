package com.example.maliva.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maliva.R
import com.example.maliva.adapter.destination.DestinationAdapter
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.state.Result
import com.example.maliva.data.utils.ObtainViewModelFactory
import com.example.maliva.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

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

        setupObservers()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvPopularDestination.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = DestinationAdapter(itemLayoutResId = R.layout.item_destination)
        binding.rvPopularDestination.adapter = adapter

        binding.rvRecommendedDestination.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val recommendedAdapter = DestinationAdapter(showRating = false, itemLayoutResId = R.layout.item_destination_2)
        binding.rvRecommendedDestination.adapter = recommendedAdapter
    }

    private fun setupObservers() {
        viewModel.getAllDestination().observe(viewLifecycleOwner, Observer { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        // Update both RecyclerViews with data
                        getDestinations(result.data.data)
                        getRecommendedDestinations(result.data.data)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), getString(R.string.title_signup), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun getDestinations(result: List<DataItem?>?) {
        result?.let {
            val limitedList = it.filterNotNull().take(100)
            val adapter = binding.rvPopularDestination.adapter as DestinationAdapter
            adapter.submitList(limitedList)
        }
    }

    private fun getRecommendedDestinations(result: List<DataItem?>?) {
        result?.let {
            val limitedList = it.filterNotNull().take(100)
            val adapter = binding.rvRecommendedDestination.adapter as DestinationAdapter
            adapter.submitList(limitedList)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvPopularDestination.adapter = null
    }
}
