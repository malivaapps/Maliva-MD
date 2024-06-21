package com.example.maliva.adapter.tripresult


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maliva.R
import com.example.maliva.data.response.PlanItem
import com.example.maliva.data.response.TripPlanResponse

class TripResultAdapter(private val planItems: List<PlanItem>?) :
    RecyclerView.Adapter<TripResultAdapter.TripResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trip_result, parent, false)
        return TripResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripResultViewHolder, position: Int) {
        val planItem = planItems?.get(position)
        planItem?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return planItems?.size ?: 0
    }

    inner class TripResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val destinationImageView: ImageView = itemView.findViewById(R.id.destinationImageView)

        fun bind(planItem: PlanItem) {
            // Bind data from PlanItem to UI components
            titleTextView.text = planItem.namaWisata
            locationTextView.text = planItem.alamat
            priceTextView.text = "${planItem.harga} IDR"

            // Example: Load image using Glide (adjust as per your implementation)
            Glide.with(itemView.context).load(planItem.images).into(destinationImageView)
        }
    }
}
