package com.example.maliva.view.planner

import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.maliva.data.response.TripPlanResponse
import com.example.maliva.data.state.Result
import com.example.maliva.databinding.FragmentPlannerBinding
import com.example.maliva.view.viewmodelfactory.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.math.BigDecimal
import android.Manifest
import android.content.Intent
import com.example.maliva.R
import com.example.maliva.data.response.PlanItem
import com.example.maliva.view.tripresult.TripResultActivity


class PlannerFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPlannerBinding
    private val viewModel: PlannerViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlannerBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkboxTraveler1.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkboxTraveler2.checkbox.isChecked = false
                binding.checkboxTraveler3.checkbox.isChecked = false
            }
        }

        binding.checkboxTraveler2.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkboxTraveler1.checkbox.isChecked = false
                binding.checkboxTraveler3.checkbox.isChecked = false
            }
        }

        binding.checkboxTraveler3.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkboxTraveler1.checkbox.isChecked = false
                binding.checkboxTraveler2.checkbox.isChecked = false
            }
        }

        // Validate inputs before switching to the next page
        binding.plannerButton.setOnClickListener {
            if (validatePage1Inputs()) {
                binding.viewFlipper.showNext()
            }
        }

        binding.plannerButton2.setOnClickListener {
            if (validatePage2Inputs()) {
                binding.viewFlipper.showNext()
            }
        }

        binding.btnBack.setOnClickListener {
            dismiss() // Close the bottom sheet
        }

        binding.btnBack2.setOnClickListener {
            binding.viewFlipper.showPrevious() // Switch to page 1
        }

        binding.btnBack3.setOnClickListener {
            binding.viewFlipper.showPrevious() // Switch to page 2
        }

        binding.plannerButton3.setOnClickListener {
            if (validatePage3Inputs()) {
                Log.d("PlannerFragment", "Saving trip plan button clicked")
                saveTripPlan()
            }
        }

        binding.sliderPrice.addOnChangeListener { slider, value, fromUser ->
            val budget = value.toInt()
            binding.budgetEditText.setText(budget.toString())
        }

        binding.fetchLocationButton.setOnClickListener {
            fetchCurrentLocation()
        }

        viewModel.tripPlan.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success -> {
                    val tripPlanResponse = result.data
                    Log.d("PlannerFragment", "Trip plan successfully reloaded: $tripPlanResponse")
                    updateUI(tripPlanResponse)
                }
                is Result.Error -> {
                    val errorMessage = result.error
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
                Result.Loading -> {
                    Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun validatePage1Inputs(): Boolean {
        return when {
            !(binding.checkboxTraveler1.checkbox.isChecked || binding.checkboxTraveler2.checkbox.isChecked || binding.checkboxTraveler3.checkbox.isChecked) -> {
                Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun validatePage2Inputs(): Boolean {
        return when {
            binding.latLongEditText.text.toString().trim().isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter current location of yours by click the get location", Toast.LENGTH_SHORT).show()
                false
            }
            !(binding.chipPantai.isChecked || binding.chipSumber.isChecked || binding.chipAirTerjun.isChecked || binding.chipBukit.isChecked ||
                    binding.chipKebun.isChecked || binding.chipSungai.isChecked || binding.chipTaman.isChecked || binding.chipHutan.isChecked ||
                    binding.chipGunung.isChecked || binding.chipWaduk.isChecked || binding.chipBendungan.isChecked || binding.chipLembah.isChecked) -> {
                Toast.makeText(requireContext(), "Please select a type", Toast.LENGTH_SHORT).show()
                false
            }
            !(binding.chipYes.isChecked || binding.chipNo.isChecked) -> {
                Toast.makeText(requireContext(), "Please select child preference", Toast.LENGTH_SHORT).show()
                false
            }
            binding.destinationEditText.text.toString().trim().isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter a number of the amount of destination", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun validatePage3Inputs(): Boolean {
        return when {
            binding.sliderPrice.value == 0f -> {
                Toast.makeText(requireContext(), "Please select a budget", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun saveTripPlan() {
        try {
            val category = when {
                binding.checkboxTraveler1.checkbox.isChecked -> "Solo"
                binding.checkboxTraveler2.checkbox.isChecked -> "Friends"
                binding.checkboxTraveler3.checkbox.isChecked -> "Family"
                else -> {
                    Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            val type = when {
                binding.chipPantai.isChecked -> "Pantai"
                binding.chipSumber.isChecked -> "Kebun"
                binding.chipAirTerjun.isChecked -> "Air Terjun"
                binding.chipBukit.isChecked -> "Bukit"
                binding.chipKebun.isChecked -> "Kebun"
                binding.chipSungai.isChecked -> "Sungai"
                binding.chipTaman.isChecked -> "Taman"
                binding.chipHutan.isChecked -> "Hutan"
                binding.chipGunung.isChecked -> "Gunung"
                binding.chipWaduk.isChecked -> "Waduk"
                binding.chipBendungan.isChecked -> "Bendungan"
                binding.chipLembah.isChecked -> "Lembah"
                else -> "Default Type"
            }

            val child = when {
                binding.chipYes.isChecked -> "Yes"
                binding.chipNo.isChecked -> "No"
                else -> null
            }

            val budget = binding.sliderPrice.value.toInt()

            val nrec = try {
                binding.destinationEditText.text.toString().toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(), "Please enter a valid number for recommendations", Toast.LENGTH_SHORT).show()
                return
            }

            val title = try {
                binding.titleEditText.text.toString()
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(), "Please enter a valid number for recommendations", Toast.LENGTH_SHORT).show()
                return
            }

            val latLongInput = binding.latLongEditText.text.toString().trim()
            val (lat, long) = if (latLongInput.isNotEmpty()) {
                val latLongParts = latLongInput.split(",")
                if (latLongParts.size == 2) {
                    val latStr = latLongParts[0].trim()
                    val longStr = latLongParts[1].trim()
                    Pair(BigDecimal(latStr), BigDecimal(longStr))
                } else {
                    Toast.makeText(requireContext(), "Invalid latitude and longitude format", Toast.LENGTH_SHORT).show()
                    return
                }
            } else {
                Toast.makeText(requireContext(), "Please enter latitude and longitude", Toast.LENGTH_SHORT).show()
                return
            }

            Log.d(TAG, "Saving trip plan with parameters: category=$category, type=$type, child=$child, budget=$budget, lat=$lat, long=$long, nrec=$nrec")

            // Ensure that viewModel.saveTripPlan() matches the correct method signature
            viewModel.generateTripPlan(category, type, child, budget, lat, long, nrec, title) { result ->
                when (result) {
                    is Result.Success -> {
                        val tripPlanResponse = result.data
                        val (title, planItems) = generatePlanItems(tripPlanResponse)

                        // Start TripResultActivity and pass the planItems list through Intent
                        val intent = Intent(requireContext(), TripResultActivity::class.java).apply {
                            putExtra("TITLE", title)
                            putParcelableArrayListExtra("PLAN_ITEMS", ArrayList(planItems))
                            putExtra("CATEGORY", category)
                            putExtra("TYPE", type)
                            putExtra("CHILD", child)
                            putExtra("BUDGET", budget)
                            putExtra("LATITUDE", lat)
                            putExtra("LONGITUDE", long)
                            putExtra("NREC", nrec)
                        }
                        startActivity(intent)
                    }
                    is Result.Error -> {
                        // Handle error case
                        Toast.makeText(requireContext(), "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    }

                    Result.Loading -> TODO()
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error saving trip plan", e)
            Toast.makeText(requireContext(), "An unknown error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun generatePlanItems(response: TripPlanResponse): Pair<String, List<PlanItem>> {
        val planItems = mutableListOf<PlanItem>()
        val title = response.data?.title.orEmpty()

        response.data?.plan?.forEach { apiPlanItem ->
            val planItem = PlanItem(
                images = apiPlanItem?.images.orEmpty(),
                alamat = apiPlanItem?.alamat.orEmpty(),
                kategori = apiPlanItem?.kategori.orEmpty(),
                jenisWisata = apiPlanItem?.jenisWisata.orEmpty(),
                rating = apiPlanItem?.rating ?: 0.0,
                latitude = apiPlanItem?.latitude ?: BigDecimal.ZERO,
                longitude = apiPlanItem?.longitude ?: BigDecimal.ZERO,
                deskripsi = apiPlanItem?.deskripsi.orEmpty(),
                childFriendly = apiPlanItem?.childFriendly.orEmpty(),
                linkAlamat = apiPlanItem?.linkAlamat.orEmpty(),
                fasilitasYangTersedia = apiPlanItem?.fasilitasYangTersedia.orEmpty(),
                namaWisata = apiPlanItem?.namaWisata.orEmpty(),
                aksesibilitas = apiPlanItem?.aksesibilitas.orEmpty(),
                harga = apiPlanItem?.harga ?: 0
            )
            planItems.add(planItem)
        }

        return Pair(title, planItems)
    }

    private fun updateUI(tripPlanResponse: TripPlanResponse) {
        // Example: Update UI with trip plan details
        // You can bind data from tripPlanResponse to your UI components here
    }

    private fun fetchCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission granted, proceed with location request
            fusedLocationClient.lastLocation
                .addOnSuccessListener(requireActivity()) { location ->
                    if (location != null) {
                        val latitude = BigDecimal.valueOf(location.latitude)
                        val longitude = BigDecimal.valueOf(location.longitude)
                        Log.d(TAG, "Current location - Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                        updateLocationUI(latitude, longitude)
                    } else {
                        Log.e(TAG, "Last known location is null.")
                        // Handle null location scenario, e.g., fallback or retry logic
                        Toast.makeText(requireContext(), "Failed to fetch location", Toast.LENGTH_SHORT).show()
                         val defaultLatitude = BigDecimal.valueOf(-7.257472)
                         val defaultLongitude = BigDecimal.valueOf(112.752088)
                         updateLocationUI(defaultLatitude, defaultLongitude)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error getting location", e)
                    Toast.makeText(requireContext(), "Error getting location: ${e.message}", Toast.LENGTH_SHORT).show()
                    // Handle failure scenario, e.g., retry logic
                }
        }
    }


    private fun updateLocationUI(latitude: BigDecimal, longitude: BigDecimal) {
        binding.latLongEditText.setText("$latitude, $longitude")
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet =
                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = 1950
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 1950
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.window?.setDimAmount(0.8f)
        return dialog
    }

    companion object {
        private const val TAG = "PlannerFragment"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}