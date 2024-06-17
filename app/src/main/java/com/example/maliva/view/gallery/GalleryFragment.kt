package com.example.maliva.view.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maliva.R
import com.example.maliva.adapter.gallery.GalleryAdapter
import com.example.maliva.data.api.ApiConfig
import com.example.maliva.data.preference.LoginPreferences
import com.example.maliva.data.preference.dataStore
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.state.Result


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var destinationId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        // Retrieve destination ID from arguments
        destinationId = requireArguments().getString(ARG_DESTINATION_ID) ?: ""

        recyclerView = root.findViewById(R.id.rv_gallery)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        galleryAdapter = GalleryAdapter(emptyList())
        recyclerView.adapter = galleryAdapter

        // Initialize ViewModel using ViewModelProvider and Factory
        val apiService = ApiConfig.getApiService("") // You need to pass your token here
        val loginPreferences = LoginPreferences.getInstance(requireContext().dataStore)
        val repository = DestinationRepository.getInstance(apiService, loginPreferences)
        val viewModelFactory = GalleryViewModelFactory(repository)
        galleryViewModel = ViewModelProvider(this, viewModelFactory).get(GalleryViewModel::class.java)

        // Load gallery data
        loadGalleryData()

        return root
    }

    private fun loadGalleryData() {
        galleryViewModel.getDestinationGallery(destinationId).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    // Handle loading state
                }
                is Result.Success -> {
                    result.data?.data?.let { items ->
                        // Filter out null items
                        val filteredItems = items.filterNotNull()
                        galleryAdapter.updateGallery(filteredItems)
                    }
                }
                is Result.Error -> {
                    // Handle error state
                }
            }
        })
    }

    companion object {
        private const val ARG_DESTINATION_ID = "destination_id"

        fun newInstance(destinationId: String): GalleryFragment {
            val fragment = GalleryFragment()
            val args = Bundle().apply {
                putString(ARG_DESTINATION_ID, destinationId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}