package com.example.maliva.view.profilelogin.ProfileLoginFragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.maliva.R
import com.example.maliva.databinding.FragmentProfileLoginBinding
import com.example.maliva.view.main.MainActivity
import com.example.maliva.view.profilelogin.ProfileLoginViewModel
import com.example.maliva.view.viewmodelfactory.ViewModelFactory

class ProfileLoginFragment : Fragment() {
    private lateinit var binding: FragmentProfileLoginBinding
    private lateinit var viewModel: ProfileLoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory).get(ProfileLoginViewModel::class.java)

        binding.logoutButton.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        val alertBuilder = AlertDialog.Builder(requireContext())
        alertBuilder.setTitle(getString(R.string.logout))
        alertBuilder.setMessage(getString(R.string.logout_alert))
        alertBuilder.setPositiveButton(R.string.ok) { _, _ ->
            viewModel.logout()
            navigateToMain()
        }
        alertBuilder.setNegativeButton(R.string.cancel_logout) { dialog, _ ->
            dialog.dismiss()
        }
        alertBuilder.create().show()
    }

    private fun navigateToMain() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finishAffinity()
    }
}