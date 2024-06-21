package com.example.maliva.view.filter

import android.app.Dialog
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.maliva.R
import com.example.maliva.databinding.FragmentFilterBinding
import com.example.maliva.view.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.slider.RangeSlider
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import com.example.maliva.data.state.Result
import com.example.maliva.data.response.DataItem
import com.example.maliva.view.search.SearchActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FilterFragment : BottomSheetDialogFragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
        maximumFractionDigits = 0
        currency = Currency.getInstance("IDR")
    }

    private val distanceFormat = NumberFormat.getNumberInstance().apply {
        maximumFractionDigits = 0
    }

    private val viewModel: FilterViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

        // Set peek height and other configurations
        bottomSheetBehavior.peekHeight = resources.displayMetrics.heightPixels

        // Setting up the label formatter for srPrice (RangeSlider)
        binding.srPrice.setLabelFormatter { value: Float ->
            currencyFormat.format(value.toInt())
        }

        // Add a listener to srPrice to update MinPriceEditText and MaxPriceEditText
        binding.srPrice.addOnChangeListener { slider, value, fromUser ->
            if (slider.values.size >= 2) { // Ensure there are at least two values
                val minValue = slider.values[0].toInt()
                val maxValue = slider.values[1].toInt()

                // Update MinPrice and MaxPrice EditTexts with formatted text
                binding.MinPrice.editText?.setText(currencyFormat.format(minValue))
                binding.MaxPrice.editText?.setText(currencyFormat.format(maxValue))
            }
        }

        binding.srPrice.apply {
            valueFrom = 0f
            valueTo = 120000f
            stepSize = 1000f
            setValues(0f, 120000f)
            setLabelFormatter { value ->
                currencyFormat.format(value.toInt())
            }
        }

        // Disable minPriceEditText and maxPriceEditText initially
        binding.MinPrice.isEnabled = false
        binding.MaxPrice.isEnabled = false

        // Setting up the label formatter for srRange (RangeSlider for kilometers)
        binding.srRange.setLabelFormatter { value: Float ->
            "${distanceFormat.format(value.toInt())} KM"
        }

        binding.srRange.addOnChangeListener { slider, value, fromUser ->
            binding.maxRangeEditText.setText(distanceFormat.format(value.toInt()))
        }

        // Add a listener to srRange to update maxRangeEditText
        binding.srRange.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {}

            override fun onStopTrackingTouch(slider: RangeSlider) {
                if (slider.values.size >= 2) { // Ensure there are at least two values
                    val minValue = slider.values[0].toInt()
                    val maxValue = slider.values[1].toInt()

                    // Update maxRangeEditText with formatted text
                    // Update MinPrice and MaxPrice EditTexts with formatted text
                    binding.minRange.editText?.setText(distanceFormat.format(minValue))
                    binding.maxRange.editText?.setText(distanceFormat.format(maxValue))

                    Log.d(TAG, "Min Range: $minValue KM, Max Range: $maxValue KM")

                    // Fetch current location and filter destinations
                    fetchCurrentLocationAndFilter(minValue, maxValue)
                }
            }
        })

        // Enable maxRangeEditText to allow text to be set programmatically
        binding.minRange.isEnabled = false
        binding.maxRange.isEnabled = false

        // Observe categories LiveData from ViewModel
        viewModel.allCategories.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "Categories loaded successfully: ${result.data}")
                    // Populate chips with categories
                    populateCategoriesChips(result.data) // Call populateCategoriesChips with fetched data
                }

                is Result.Error -> {
                    // Handle error
                    // You can show a Toast or handle the error state here
                    Log.e(TAG, "Error loading categories: ${result.error}")
                }

                is Result.Loading -> {
                    Log.d(TAG, "Loading categories...")
                    // You can show loading indicator if needed
                }
                // Add an else branch if necessary
            }
        })

        // Fetch categories from ViewModel
        viewModel.fetchAllCategories()

        // Observe types LiveData from ViewModel
        viewModel.allTypes.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "Types loaded successfully: ${result.data}")
                    // Populate chips with types
                    populateTypesChips(result.data) // Call populateTypesChips with fetched data
                }

                is Result.Error -> {
                    // Handle error
                    // You can show a Toast or handle the error state here
                    Log.e(TAG, "Error loading types: ${result.error}")
                }

                is Result.Loading -> {
                    Log.d(TAG, "Loading types...")
                    // You can show loading indicator if needed
                }
                // Add an else branch if necessary
            }
        })

        // Fetch types from ViewModel
        viewModel.fetchAllTypes()

        // Setup rating checkboxes
        setupRatingCheckboxes()

        binding.btnApply.setOnClickListener {
            Log.d(TAG, "Apply filters button clicked")
            applyFilters()
        }
    }

    private fun fetchCurrentLocationAndFilter(minRange: Int, maxRange: Int) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Companion.LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission granted
            fusedLocationClient.lastLocation
                .addOnSuccessListener(requireActivity(), OnSuccessListener { location ->
                    location?.let {
                        val latitude = BigDecimal.valueOf(it.latitude)
                        val longitude = BigDecimal.valueOf(it.longitude)
                        Log.d(TAG, "Current location - Latitude: ${it.latitude}, Longitude: ${it.longitude}")
                        filterDestinationsByLocationAndRange(latitude, longitude, minRange, maxRange)
                    } ?: run {
                        Log.e(TAG, "Last known location is null.")
                    }
                })
        }
    }

    private fun filterDestinationsByLocationAndRange(lat: BigDecimal, long: BigDecimal, minRange: Int, maxRange: Int) {
        Log.d(TAG, "Filtering destinations - Latitude: $lat, Longitude: $long, Min Range: $minRange KM, Max Range: $maxRange KM")
        viewModel.viewModelScope.launch {
            viewModel.filterDestinationsByLocationAndRange(lat, long, minRange, maxRange)
        }
    }

    private fun setupRatingCheckboxes() {
        binding.cbRating1.setOnClickListener { onRatingCheckboxClick(1) }
        binding.cbRating2.setOnClickListener { onRatingCheckboxClick(2) }
        binding.cbRating3.setOnClickListener { onRatingCheckboxClick(3) }
        binding.cbRating4.setOnClickListener { onRatingCheckboxClick(4) }
        binding.cbRating5.setOnClickListener { onRatingCheckboxClick(5) }
    }

    private fun onRatingCheckboxClick(rating: Int) {
        viewModel.viewModelScope.launch {
            viewModel.filterDestinationsByRating(rating)
        }
    }

    private fun populateCategoriesChips(categories: List<String>) {
        val chipGroup = binding.chipGroup6
        val inflater = LayoutInflater.from(chipGroup.context)

        // Clear existing chips if needed
        chipGroup.removeAllViews()

        // Create Family chip
        val familyChip = inflater.inflate(R.layout.chip_category, chipGroup, false) as Chip
        familyChip.text = "Family"
        familyChip.isCheckable = true
        familyChip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.viewModelScope.launch {
                    viewModel.filterDestinationsByCategory("family")
                }
            } else {
                // Handle unchecked state if needed
            }
        }
        chipGroup.addView(familyChip)

        // Create Solo chip
        val soloChip = inflater.inflate(R.layout.chip_category, chipGroup, false) as Chip
        soloChip.text = "Solo"
        soloChip.isCheckable = true
        soloChip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.viewModelScope.launch {
                    viewModel.filterDestinationsByCategory("solo")
                }
            } else {
                // Handle unchecked state if needed
            }
        }
        chipGroup.addView(soloChip)

        // Create Solo chip
        val friendsChip = inflater.inflate(R.layout.chip_category, chipGroup, false) as Chip
        friendsChip.text = "Friends"
        friendsChip.isCheckable = true
        friendsChip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.viewModelScope.launch {
                    viewModel.filterDestinationsByCategory("friends")
                }
            } else {
                // Handle unchecked state if needed
            }
        }
        chipGroup.addView(friendsChip)
    }

    private fun populateTypesChips(types: List<String>) {
        val chipGroup = binding.chipActivities
        val inflater = LayoutInflater.from(chipGroup.context)

        // Clear existing chips if needed
        chipGroup.removeAllViews()

        // Create chips for each type
        types.forEach { type ->
            val chip = inflater.inflate(R.layout.chip_types, chipGroup, false) as Chip
            chip.text = type.capitalize(Locale.ROOT)
            chip.isCheckable = true
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.viewModelScope.launch {
                        viewModel.filterDestinationByType(type.toLowerCase(Locale.ROOT))
                    }
                } else {
                    // Handle unchecked state if needed
                }
            }
            chipGroup.addView(chip)
        }
    }

    private fun applyFilters() {
        // Obtain the current price range values
        val minPrice = binding.srPrice.values[0].toInt()
        val maxPrice = binding.srPrice.values[1].toInt()

        // Launch a coroutine to filter by price separately
        viewModel.viewModelScope.launch {
            viewModel.filterDestinationsByPrice(minPrice, maxPrice)
        }

        // Observe filteredDestinations to apply all filters including price
        viewModel.filteredDestinations.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success -> {
                    var filteredItems = result.data

                    // Apply category filter if selectedCategories is not empty
                    val selectedCategories = getSelectedCategories()
                    if (selectedCategories.isNotEmpty()) {
                        filteredItems = filteredItems.filter { item ->
                            selectedCategories.any { category ->
                                item.category?.contains(category, ignoreCase = true) ?: false
                            }
                        }
                    }

                    // Apply type filter if selectedTypes is not empty
                    val selectedTypes = getSelectedTypes()
                    if (selectedTypes.isNotEmpty()) {
                        filteredItems = filteredItems.filter { item ->
                            selectedTypes.any { type ->
                                item.activities?.contains(type, ignoreCase = true) ?: false
                            }
                        }
                    }

                    // Apply rating filter
                    val selectedRating = getSelectedRating()
                    if (selectedRating != null) {
                        filteredItems = filteredItems.filter { item ->
                            item.rating!! >= selectedRating && item.rating < (selectedRating + 1.0)
                        }
                    }

//                    val minValue = binding.srRange.values[0].toInt()
//                    val maxValue = binding.srRange.values[1].toInt()
//
//                    fetchCurrentLocationAndFilter(minValue, maxValue)

                    // Pass filtered items to SearchActivity
                    sendFilteredItemsToSearchActivity(filteredItems)
                }

                is Result.Error -> {
                    Log.e(TAG, "Error applying filters: ${result.error}")
                    // Handle error scenario if necessary
                }

                is Result.Loading -> {
                    Log.d(TAG, "Loading while applying filters...")
                    // Handle loading scenario if necessary
                }
            }
        })
    }


    private fun getSelectedRating(): Int? {
        val checkedRating = listOf(
            binding.cbRating1,
            binding.cbRating2,
            binding.cbRating3,
            binding.cbRating4,
            binding.cbRating5
        ).firstOrNull { it.isChecked }

        return when (checkedRating) {
            binding.cbRating1 -> 1
            binding.cbRating2 -> 2
            binding.cbRating3 -> 3
            binding.cbRating4 -> 4
            binding.cbRating5 -> 5
            else -> null
        }
    }


    private fun getSelectedCategories(): List<String> {
        val selectedCategories = mutableListOf<String>()
        for (i in 0 until binding.chipGroup6.childCount) {
            val chip = binding.chipGroup6.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedCategories.add(chip.text.toString().toLowerCase(Locale.ROOT))
            }
        }
        return selectedCategories
    }

    private fun getSelectedTypes(): List<String> {
        val selectedTypes = mutableListOf<String>()
        for (i in 0 until binding.chipActivities.childCount) {
            val chip = binding.chipActivities.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedTypes.add(chip.text.toString().toLowerCase(Locale.ROOT))
            }
        }
        return selectedTypes
    }



    private fun sendFilteredItemsToSearchActivity(filteredItems: List<DataItem>) {
        // Create an Intent to pass filteredItems to SearchActivity
        val intent = Intent(requireContext(), SearchActivity::class.java).apply {
            putExtra("FILTERED_ITEMS", ArrayList(filteredItems))
        }
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet =
                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = 2050
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 2050
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.window?.setDimAmount(0.8f)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "FilterFragment"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
