package com.example.maliva.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.maliva.R
import com.example.maliva.view.home.HomeFragment
import com.example.maliva.view.planner.PlannerFragment
import com.example.maliva.view.trip.TripFragment
import com.example.maliva.view.profile.ProfileFragment
import com.example.maliva.view.profilelogin.ProfileLoginFragment.ProfileLoginFragment
import com.example.maliva.view.profilelogin.ProfileLoginViewModel
import com.example.maliva.view.saved.SavedFragment
import com.example.maliva.view.viewmodelfactory.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {
    private lateinit var bottomBar: AnimatedBottomBar
    private lateinit var viewModel: ProfileLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(ProfileLoginViewModel::class.java)


        bottomBar = findViewById(R.id.bottom_bar)
        val fabGenerate: FloatingActionButton = findViewById(R.id.fab_generate)

        setupBottomBar()
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        fabGenerate.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun setupBottomBar() {
        bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(lastIndex: Int, lastTab: AnimatedBottomBar.Tab?,
                                       newIndex: Int, newTab: AnimatedBottomBar.Tab) {
                when (newTab.id) {
                    R.id.navigation_home -> loadFragment(HomeFragment())
                    R.id.navigation_trip -> loadFragment(TripFragment())
                    R.id.navigation_saved -> loadFragment(SavedFragment())
                    R.id.navigation_profile -> loadFragment(ProfileFragment())
                }
            }
        })
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.homeFragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showBottomSheet() {
        val plannerFragment = PlannerFragment()
        plannerFragment.show(supportFragmentManager, plannerFragment.tag)
    }


}