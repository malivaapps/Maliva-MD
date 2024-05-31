package com.example.maliva.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.maliva.R
import com.example.maliva.view.home.HomeFragment
import com.example.maliva.view.saved.SavedFragment
import com.example.maliva.view.trip.TripFragment
import com.example.maliva.view.profile.ProfileFragment
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {
    private lateinit var bottomBar: AnimatedBottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomBar = findViewById(R.id.bottom_bar)

        setupBottomBar()
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
    }

    private fun setupBottomBar() {
        bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(lastIndex: Int, lastTab: AnimatedBottomBar.Tab?,
                                       newIndex: Int, newTab: AnimatedBottomBar.Tab) {
                when (newTab.id) {
                    R.id.navigation_home -> loadFragment(HomeFragment())
                    R.id.navigation_saved -> loadFragment(SavedFragment())
                    R.id.navigation_trip -> loadFragment(TripFragment())
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
}
