package com.example.maliva.view.planner

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import com.example.maliva.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class PlannerFragment : BottomSheetDialogFragment() {

    private lateinit var checkboxTraveler1: CheckBox
    private lateinit var checkboxTraveler2: CheckBox
    private lateinit var checkboxTraveler3: CheckBox
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_planner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Reference the CheckBoxes
        val traveler1View = view.findViewById<View>(R.id.checkboxTraveler1)
        val traveler2View = view.findViewById<View>(R.id.checkboxTraveler2)
        val traveler3View = view.findViewById<View>(R.id.checkboxTraveler3)

        checkboxTraveler1 = traveler1View.findViewById(R.id.checkbox)
        checkboxTraveler2 = traveler2View.findViewById(R.id.checkbox)
        checkboxTraveler3 = traveler3View.findViewById(R.id.checkbox)

        // Set unique content for each checkbox item
        setupCheckbox(traveler1View, "Just Me", "A solo traveler in exploration")
        setupCheckbox(traveler2View, "Friends", "Go out with fun friends")
        setupCheckbox(traveler3View, "Family", "A happy trip with your family")

        // Set click listeners for CheckBoxes
        checkboxTraveler1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkboxTraveler2.isChecked = false
                checkboxTraveler3.isChecked = false
            }
        }

        checkboxTraveler2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkboxTraveler1.isChecked = false
                checkboxTraveler3.isChecked = false
            }
        }

        checkboxTraveler3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkboxTraveler1.isChecked = false
                checkboxTraveler2.isChecked = false
            }
        }

        // Reference the ViewFlipper
        viewFlipper = view.findViewById(R.id.viewFlipper)

        // Set click listener for planner button
        val plannerButton = view.findViewById<MaterialButton>(R.id.plannerButton)
        plannerButton.setOnClickListener {
            viewFlipper.showNext()
        }

        val plannerButton2 = view.findViewById<MaterialButton>(R.id.plannerButton2)
        plannerButton2.setOnClickListener {
            viewFlipper.showNext()
        }

        val btn_back = view.findViewById<ImageView>(R.id.btn_back)
        btn_back.setOnClickListener {
            dismiss() // Close the bottom sheet
        }

        val btn_back2 = view.findViewById<ImageView>(R.id.btn_back2)
        btn_back2.setOnClickListener {
            viewFlipper.showPrevious() // Switch to page 1
        }

        val btn_back3 = view.findViewById<ImageView>(R.id.btn_back3)
        btn_back3.setOnClickListener {
            viewFlipper.showPrevious() // Switch to page 2
        }

        // Set up the AutoCompleteTextView dropdown menu
        setupDropdownMenu(view)

        // Initialize bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

        // Prevent bottom sheet from being collapsed or hidden
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Do nothing
            }
        })

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        // Disable swipe-to-dismiss functionality
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.skipCollapsed = true
    }

    private fun setupDropdownMenu(view: View) {
        autoCompleteTextView = view.findViewById(R.id.spinnerRegion)

        // Define the options for the dropdown menu including the hint
        val optionsWithHint = arrayOf("Option 1", "Option 2", "Option 3")

        // Create an ArrayAdapter with the options and custom dropdown layout
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner, optionsWithHint)

        // Apply the adapter to the AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter)

        // Disable user input
        autoCompleteTextView.isFocusable = false
        autoCompleteTextView.isClickable = true

        // Set a listener to handle item selection
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                // Handle hint selection
                // Do nothing or show a message indicating that the user needs to select an option
                Toast.makeText(requireContext(), "Please select an option", Toast.LENGTH_SHORT).show()
            } else {
                // Handle other item selections
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCheckbox(view: View, primaryText: String, secondaryText: String) {
        val primaryTextView = view.findViewById<TextView>(R.id.primaryText)
        val secondaryTextView = view.findViewById<TextView>(R.id.secondaryText)

        primaryTextView.text = primaryText
        secondaryTextView.text = secondaryText
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
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
}
