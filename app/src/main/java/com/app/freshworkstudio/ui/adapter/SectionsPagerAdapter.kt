package com.app.freshworkstudio.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.freshworkstudio.ui.fragments.TrendingFragment


/**
 * A [FragmentStateAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount() = 1

    override fun createFragment(position: Int): Fragment {
        return TrendingFragment.newInstance(position)
    }
}