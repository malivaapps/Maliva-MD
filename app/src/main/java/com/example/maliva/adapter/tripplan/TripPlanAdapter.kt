package com.example.maliva.adapter.tripplan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.maliva.data.response.DataItemDetail
import com.example.maliva.databinding.ItemTripPlanBinding

class TripPlanAdapter(private var dataItemDetails: List<DataItemDetail>) : RecyclerView.Adapter<TripPlanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTripPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataItemDetails[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataItemDetails.size
    }

    fun setData(data: List<DataItemDetail>) {
        dataItemDetails = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemTripPlanBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dataItemDetail: DataItemDetail) {
            binding.titleTextView.text = dataItemDetail.title ?: ""
        }
    }
}
