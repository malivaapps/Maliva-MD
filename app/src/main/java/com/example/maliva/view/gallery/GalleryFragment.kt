package com.example.maliva.view.gallery

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
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
import com.example.maliva.data.utils.uriToFile
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import java.io.File



class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var destinationId: String
    private var selectedImageUri: Uri? = null
    private var imageFile: File? = null
    private lateinit var progressBar: ProgressBar

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            imageFile = uriToFile(it, requireContext())
            uploadImage()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        destinationId = requireArguments().getString(ARG_DESTINATION_ID) ?: ""
        recyclerView = root.findViewById(R.id.rv_gallery)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        galleryAdapter = GalleryAdapter(emptyList())
        recyclerView.adapter = galleryAdapter
        progressBar = root.findViewById(R.id.progressBar)

        val apiService = ApiConfig.getApiService("")
        val loginPreferences = LoginPreferences.getInstance(requireContext().dataStore)
        val repository = DestinationRepository.getInstance(apiService, loginPreferences)
        val viewModelFactory = GalleryViewModelFactory(repository)
        galleryViewModel = ViewModelProvider(this, viewModelFactory).get(GalleryViewModel::class.java)

        // Load gallery data
        loadGalleryData()

        // Set up gallery button
        val galleryButton: MaterialButton = root.findViewById(R.id.galleryButton)
        galleryButton.setOnClickListener {
            openGallery()
        }

        return root
    }

    private fun openGallery() {
        getImage.launch("image/*")
    }

    private fun uploadImage() {
        imageFile?.let {
            galleryViewModel.submitGallery(requireContext(), destinationId, it).observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Result.Loading -> {
                        progressBar.visibility = View.VISIBLE
                        Log.d(TAG, "Uploading image...")
                    }
                    is Result.Success -> {
                        progressBar.visibility = View.GONE
                        Log.d(TAG, "Image uploaded successfully")
                        // Refresh gallery data
                        loadGalleryData()
                        showSuccessDialog()
                    }
                    is Result.Error -> {
                        progressBar.visibility = View.GONE
                        val errorMessage = result.error
                        Log.e(TAG, "Error uploading image: $errorMessage")
                        Snackbar.make(requireView(), "Error uploading image: $errorMessage", Snackbar.LENGTH_LONG).show()
                    }
                }
            })
        }
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Success")
            .setMessage("Image uploaded successfully!")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun loadGalleryData() {
        galleryViewModel.getDestinationGallery(destinationId).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    Log.d(TAG, "Loading gallery data...")
                }
                is Result.Success -> {
                    progressBar.visibility = View.GONE

                    result.data?.data?.let { items ->
                        val filteredItems = items.filterNotNull()
                        Log.d(TAG, "Received ${filteredItems.size} gallery items")
                        galleryAdapter.updateGallery(filteredItems)
                    }
                }
                is Result.Error -> {
                    val errorMessage = result.error
                    Log.e(TAG, "Error loading gallery: $errorMessage")
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