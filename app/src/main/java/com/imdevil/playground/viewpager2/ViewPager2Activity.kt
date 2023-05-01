package com.imdevil.playground.viewpager2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.imdevil.playground.databinding.ActivityViewPager2Binding

class ViewPager2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityViewPager2Binding

    private val fragmentCreators = listOf<() -> Fragment>(
        {
            ViewPager2Fragment.newInstance(
                "PARENT_ONE",
                android.R.color.holo_blue_light
            )
        },
        {
            ViewPager2Fragment.newInstance(
                "PARENT_TWO",
                android.R.color.holo_green_light
            )
        },
        {
            ViewPager2Fragment.newInstance(
                "PARENT_THREE",
                android.R.color.holo_orange_light
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewPager2Binding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.mainViewPager.adapter =
            ViewPager2Adapter(supportFragmentManager, lifecycle, fragmentCreators)

        binding.mainViewPager.currentItem = 0
    }
}