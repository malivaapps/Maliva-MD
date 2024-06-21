package com.example.maliva.view.tripresult

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maliva.adapter.tripresult.TripResultAdapter
import com.example.maliva.data.response.PlanItem
import com.example.maliva.databinding.ActivityTripResultBinding
import com.example.maliva.view.viewmodelfactory.ViewModelFactory
import com.example.maliva.data.state.Result
import com.example.maliva.view.main.MainActivity
import java.math.BigDecimal

class TripResultActivity : AppCompatActivity() {

    private lateinit var viewModel: TripResultViewModel
    private lateinit var binding: ActivityTripResultBinding

    private var category: String = ""
    private var type: String = ""
    private var child: String? = null
    private var budget: Int = 0
    private var lat: BigDecimal = BigDecimal.ZERO
    private var long: BigDecimal = BigDecimal.ZERO
    private var nrec: Int = 0
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        viewModel = ViewModelFactory.getInstance(this).create(TripResultViewModel::class.java)

        // Retrieve plan items from intent extras
        title = intent.getStringExtra("TITLE").orEmpty()
        val planItems = intent.getParcelableArrayListExtra<PlanItem>("PLAN_ITEMS") ?: arrayListOf()

        // Retrieve user inputs from intent extras with default values or null handling
        category = intent.getStringExtra("CATEGORY") ?: ""
        type = intent.getStringExtra("TYPE") ?: ""
        child = intent.getStringExtra("CHILD")
        budget = intent.getIntExtra("BUDGET", 0)
        lat = intent.getSerializableExtra("LATITUDE") as? BigDecimal ?: BigDecimal.ZERO
        long = intent.getSerializableExtra("LONGITUDE") as? BigDecimal ?: BigDecimal.ZERO
        nrec = intent.getIntExtra("NREC", 0)

        Log.d(TAG, "Received Intent Extras:")
        Log.d(TAG, "Title: $title")
        Log.d(TAG, "Category: $category")
        Log.d(TAG, "Type: $type")
        Log.d(TAG, "Child: $child")
        Log.d(TAG, "Budget: $budget")
        Log.d(TAG, "Latitude: $lat")
        Log.d(TAG, "Longitude: $long")
        Log.d(TAG, "Recommendations: $nrec")

        // Set the title in the TextView
        binding.tvTitle.text = title

        // Set up RecyclerView adapter
        val adapter = TripResultAdapter(planItems)
        binding.rvResult.layoutManager = LinearLayoutManager(this)
        binding.rvResult.adapter = adapter

        // Save trip plan button click listener
        binding.saveTripPlan.setOnClickListener {
            showSaveConfirmationDialog()
        }

        // Observe saveTripPlanResult for success or failure
        viewModel.saveTripPlanResult.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    // Handle success, maybe navigate to a success screen or update UI
                    Toast.makeText(this, "Trip plan saved successfully!", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                }
                is Result.Error -> {
                    // Handle error, display error message to the user
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                Result.Loading -> {
                    // Optionally show loading state
                }
            }
        })
    }

    private fun showSaveConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Save Trip Plan")
            .setMessage("Do you want to save the trip plan?")
            .setPositiveButton("Save") { _, _ ->
                // User clicked Save
                saveTripPlan()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                // User clicked Cancel, do nothing or dismiss the dialog
                navigateToMainActivity()
            }
            .create()

        alertDialog.show()
    }

    private fun saveTripPlan() {
        viewModel.saveTripPlan(category, type, child, budget, lat, long, nrec, title)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "TripResultActivity"
    }
}
