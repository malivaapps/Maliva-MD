package com.example.maliva.view.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.maliva.R
import com.example.maliva.databinding.FragmentProfileBinding
import com.example.maliva.view.register.RegisterActivity
import com.example.maliva.data.state.Result
import com.example.maliva.view.viewmodelfactory.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Initialize ViewModel
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel using ViewModelProvider
        profileViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext())).get(ProfileViewModel::class.java)

        // Observe changes in updateProfileResponse
        profileViewModel.updateProfileResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d("ProfileFragment", "Loading profile...")
                    // Show loading UI if needed
                }
                is Result.Success -> {
                    val updateProfile = result.data
                    if (updateProfile != null) {
                        Log.d("ProfileFragment", "Profile loaded successfully: $updateProfile")
                        displayUserProfile(updateProfile)
                    } else {
                        Log.d("ProfileFragment", "Profile data is null")
                    }
                }
                is Result.Error -> {
                    val errorMessage = result.error
                    Log.e("ProfileFragment", "Failed to load profile: $errorMessage")
                    // Show error message if needed
                }
            }
        })

        // Call function to fetch user profile data
        Log.d("ProfileFragment", "Calling updateProfile...")
        profileViewModel.userProfile()

        // Setup logout button
        binding.logoutButton.setOnClickListener {
            navigateToRegisterActivity()
        }
    }

    private fun displayUserProfile(updateProfile: UpdateProfile) {
        // Display username and email on UI
        Log.d("ProfileFragment", "Displaying username: ${updateProfile.username}")
        Log.d("ProfileFragment", "Displaying email: ${updateProfile.email}")
        binding.tvUsername.text = updateProfile.username ?: "No username available"
        binding.tvEmail.text = updateProfile.email ?: "No email available"
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(requireContext(), RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}