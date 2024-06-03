package com.example.maliva.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maliva.R
import com.example.maliva.adapter.category.CategoryAdapter
import com.example.maliva.adapter.category.CategoryItem
import com.example.maliva.adapter.destination.DestinationAdapter
import com.example.maliva.adapter.destination.DestinationItem
import com.example.maliva.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Category RecyclerView setup
        val categoryList = listOf(
            CategoryItem("Beach", R.drawable.ic_beach),
            CategoryItem("Mountain", R.drawable.ic_mountain),
            CategoryItem("Forest", R.drawable.ic_forest)
        )
        val categoryAdapter = CategoryAdapter(categoryList)
        binding.rvCategory.adapter = categoryAdapter
        binding.rvCategory.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        // Destination RecyclerView setup for popular destinations
        val popularDestinationList = listOf(
            DestinationItem("Coban Rondo", "Batu, Malang", 25000, 4.7f, R.drawable.img_destination1),
            DestinationItem("Mount Bromo", "Batu, Malang", 25000, 4.7f, R.drawable.img_destination2),
            DestinationItem("Pink Beach", "Batu, Malang", 25000, 4.7f, R.drawable.img_destination3),
            DestinationItem("Mangunan", "Batu, Malang", 25000, 4.7f, R.drawable.img_destination4)
        )
        val popularDestinationAdapter = DestinationAdapter(popularDestinationList, DestinationAdapter.VIEW_TYPE_POPULAR, true) // Show rating for popular destinations
        binding.rvPopularDestination.adapter = popularDestinationAdapter
        binding.rvPopularDestination.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        // Destination RecyclerView setup for recommended destinations
        val recommendedDestinationList = listOf(
            DestinationItem("Coban Rondo", "Batu, Malang", 25000, 4.7f, R.drawable.img_destination1),
            DestinationItem("Mount Bromo", "Batu, Malang", 25000, 4.7f, R.drawable.img_destination2),
            DestinationItem("Pink Beach", "Batu, Malang", 25000, 4.7f, R.drawable.img_destination3),
            DestinationItem("Mangunan", "Batu, Malang", 25000, 4.7f, R.drawable.img_destination4)
        )
        // Destination RecyclerView setup for recommended destinations
        val recommendedDestinationAdapter = DestinationAdapter(recommendedDestinationList, DestinationAdapter.VIEW_TYPE_RECOMMENDED, false) // Don't show rating for recommended destinations
        binding.rvRecommendedDestination.adapter = recommendedDestinationAdapter
        binding.rvRecommendedDestination.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}

