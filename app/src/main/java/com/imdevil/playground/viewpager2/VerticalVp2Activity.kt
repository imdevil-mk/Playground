package com.imdevil.playground.viewpager2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.imdevil.playground.databinding.ActivityViewPager2VerticalBinding
import com.imdevil.playground.demo.DemoNestedScrollableListFragment

class VerticalVp2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityViewPager2VerticalBinding

    private val fragmentCreators = listOf<() -> Fragment>(
        {
            DemoNestedScrollableListFragment.newInstance("Page 1", android.R.color.holo_blue_light)
        },
        {
            DemoNestedScrollableListFragment.newInstance(
                "Page 2",
                android.R.color.holo_orange_light
            )
        },
        {
            DemoNestedScrollableListFragment.newInstance("Page 3", android.R.color.holo_green_light)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewPager2VerticalBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.mainViewPager.adapter =
            ViewPager2Adapter(supportFragmentManager, lifecycle, fragmentCreators)

        binding.mainViewPager.currentItem = 0
    }
}