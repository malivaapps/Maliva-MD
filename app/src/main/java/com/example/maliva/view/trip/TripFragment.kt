package com.example.maliva.view.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maliva.adapter.tripplan.TripPlanAdapter
import com.example.maliva.data.response.DataItemDetail
import com.example.maliva.data.response.TripPlanDetailResponse
import com.example.maliva.databinding.FragmentTripBinding
import com.example.maliva.data.state.Result
import com.example.maliva.view.viewmodelfactory.ViewModelFactory

class TripFragment : Fragment() {

    private lateinit var binding: FragmentTripBinding
    private val viewModel: TripViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var tripPlanAdapter: TripPlanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripPlanAdapter = TripPlanAdapter(emptyList())

        binding.rvTripPlan.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tripPlanAdapter
        }

        observeTripPlan()
    }

    private fun observeTripPlan() {
        viewModel.getTripPlan().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val tripPlanResponse = result.data

                    // Map DataItem to DataItemDetail
                    val dataItemDetails = tripPlanResponse.data.orEmpty().mapNotNull { dataItem ->
                        dataItem?.let {
                            DataItemDetail(
                                id = it.id,
                                title = it.title,
                            )
                        }
                    }

                    tripPlanAdapter.setData(dataItemDetails)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    val errorMessage = result.error
                    // Handle error
                }
                Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }
}
