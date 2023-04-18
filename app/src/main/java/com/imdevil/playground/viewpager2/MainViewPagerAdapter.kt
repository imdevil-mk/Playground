package com.imdevil.playground.viewpager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragmentCreators: List<() -> Fragment>,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = fragmentCreators.size

    override fun createFragment(position: Int): Fragment {
        return fragmentCreators[position].invoke()
    }
}