package com.imdevil.playground.view.scroll.nestedScrollingParent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.imdevil.playground.R
import com.imdevil.playground.view.scroll.DemoListFragment
import com.imdevil.playground.viewpager2.ViewPager2Adapter

class NestedScrollingParentActivity : AppCompatActivity(R.layout.activity_nested_scrolling_parent) {

    private val fragmentCreators = listOf<() -> Fragment>(
        {
            DemoListFragment.newInstance("自定义NestedScrollingParentView")
        },
        {
            DemoListFragment.newInstance("自定义NestedScrollingParentView")
        },
        {
            DemoListFragment.newInstance("自定义NestedScrollingParentView")
        },
        {
            DemoListFragment.newInstance("自定义NestedScrollingParentView")
        },
    )

    private val tabTexts = listOf(
        "首页", "广场", "我的", "关注"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pager = findViewById<ViewPager2>(R.id.pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        pager.adapter = ViewPager2Adapter(supportFragmentManager, lifecycle, fragmentCreators)
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = tabTexts[position]
        }.attach()
    }
}