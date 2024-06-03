package com.example.maliva.view.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maliva.R
import com.example.maliva.adapter.destination.DestinationAdapter
import com.example.maliva.adapter.destination.DestinationItem

class SavedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sample data for saved destinations
        val savedDestinationList = listOf(
            DestinationItem("Coban Rondo", "Batu, Malang", 25000, 4.7f, R.drawable.img_destination1),
            DestinationItem("Mount Bromo", "Batu, Malang", 25000, 4.7f, R.drawable.img_destination2),
            DestinationItem("Pink Beach", "Batu, Malang", 25000, 4.7f, R.drawable.img_destination3),
            DestinationItem("Mangunan", "Batu, Malang", 25000, 4.7f, R.drawable.img_destination4)
        )

        val savedDestinationAdapter = DestinationAdapter(savedDestinationList, DestinationAdapter.VIEW_TYPE_SAVED, true)

        val rvSaved: RecyclerView = view.findViewById(R.id.rvSaved)
        rvSaved.adapter = savedDestinationAdapter
        rvSaved.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }
}
