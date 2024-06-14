package com.example.maliva.view.search

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.maliva.R
import com.example.maliva.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fabFilter = binding.fabFilter
        fabFilter.setOnClickListener {
            val bottomSheetFragment = FiterFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }
}