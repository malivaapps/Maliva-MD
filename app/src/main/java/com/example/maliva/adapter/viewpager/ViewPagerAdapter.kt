package com.example.maliva.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.response.GalleryItem
import com.example.maliva.view.description.DescriptionFragment
import com.example.maliva.view.gallery.GalleryFragment
import com.example.maliva.view.review.ReviewFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity, private val destination: DataItem) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DescriptionFragment.newInstance(destination.description, destination.link, destination.facilities, destination.accessibility)
            1 -> GalleryFragment.newInstance(destination.id.toString())
            2 -> ReviewFragment.newInstance(destination.id.toString())
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}