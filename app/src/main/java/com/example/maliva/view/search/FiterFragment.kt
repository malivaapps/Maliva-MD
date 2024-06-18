package com.example.maliva.view.search

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.maliva.R
import com.example.maliva.databinding.FragmentFilterBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale


class FiterFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)


        // Set peek height
        bottomSheetBehavior.peekHeight = resources.displayMetrics.heightPixels

        binding.srPrice.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            format.maximumFractionDigits = 3
            format.currency = Currency.getInstance("IDR")
            format.format(value.toDouble())
        }

        val distanceFormat = NumberFormat.getNumberInstance()
        distanceFormat.maximumFractionDigits = 2

        binding.srRange.setLabelFormatter { value ->
            "${distanceFormat.format(value.toDouble())} KM"
        }

        binding.cbRating1.setOnClickListener { checkBoxClick(binding.cbRating1) }
        binding.cbRating2.setOnClickListener { checkBoxClick(binding.cbRating2) }
        binding.cbRating3.setOnClickListener { checkBoxClick(binding.cbRating3) }
        binding.cbRating4.setOnClickListener { checkBoxClick(binding.cbRating4) }
        binding.cbRating5.setOnClickListener { checkBoxClick(binding.cbRating5) }

    }
    fun checkBoxClick(view: View) {
        binding.cbRating1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

            } else {
                // Checkbox tidak dicentang, lakukan sesuatu
            }
        }


        // Handler untuk checkbox rating 2
        binding.cbRating2.setOnCheckedChangeListener { _, isChecked ->
            // Handler untuk checkbox rating 2
            if (isChecked) {
                // Checkbox dicentang, lakukan sesuatu
            } else {
                // Checkbox tidak dicentang, lakukan sesuatu
            }
        }
        binding.cbRating3.setOnCheckedChangeListener { _, isChecked ->
            // Handler untuk checkbox rating 2
            if (isChecked) {
                // Checkbox dicentang, lakukan sesuatu
            } else {
                // Checkbox tidak dicentang, lakukan sesuatu
            }
        }
        binding.cbRating4.setOnCheckedChangeListener { _, isChecked ->
            // Handler untuk checkbox rating 2
            if (isChecked) {
                // Checkbox dicentang, lakukan sesuatu
            } else {
                // Checkbox tidak dicentang, lakukan sesuatu
            }
        }
        binding.cbRating5.setOnCheckedChangeListener { _, isChecked ->
            // Handler untuk checkbox rating 2
            if (isChecked) {
                // Checkbox dicentang, lakukan sesuatu
            } else {
                // Checkbox tidak dicentang, lakukan sesuatu
            }
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = 2050
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 2050

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
