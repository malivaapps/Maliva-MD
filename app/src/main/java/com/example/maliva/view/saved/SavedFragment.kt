package com.example.maliva.view.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maliva.R
import com.example.maliva.adapter.destination.SavedDestinationAdapter

class SavedFragment : Fragment() {

    private lateinit var savedViewModel: SavedViewModel
    private lateinit var favoriteDestinationAdapter: SavedDestinationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application))
            .get(SavedViewModel::class.java)

        val rvSaved: RecyclerView = view.findViewById(R.id.rvSaved)
        rvSaved.layoutManager = LinearLayoutManager(requireContext())
        favoriteDestinationAdapter = SavedDestinationAdapter()

        // Set the adapter initially
        rvSaved.adapter = favoriteDestinationAdapter

        // Observe saved destinations from ViewModel

        savedViewModel.savedDestinations.observe(viewLifecycleOwner) { favorites ->
            favoriteDestinationAdapter.submitList(favorites)
        }

    }
}
