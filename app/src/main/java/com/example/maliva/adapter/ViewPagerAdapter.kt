package com.example.maliva.view.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.maliva.view.description.DescriptionFragment
import com.example.maliva.view.gallery.GalleryFragment
import com.example.maliva.view.review.ReviewFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DescriptionFragment()
            1 -> GalleryFragment()
            2 -> ReviewFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
