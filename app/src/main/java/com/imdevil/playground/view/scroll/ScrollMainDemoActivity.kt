package com.imdevil.playground.view.scroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.imdevil.playground.R
import com.imdevil.playground.base.setupButtonClick
import com.imdevil.playground.view.offset.ViewOffsetActivity
import com.imdevil.playground.view.scroll.appbar.AppbarActivity
import com.imdevil.playground.view.scroll.coordinatorLayout.CoordinatorLayoutActivity
import com.imdevil.playground.view.scroll.nestedScrollingParent.NestedScrollingParentActivity
import com.imdevil.playground.view.scroll.nestedScrollingParent2.NestedScrollingParent2Activity
import com.imdevil.playground.view.scroll.nestedScrollingParent2Demo.NestedScrollingParent2CollapsingActivity
import com.imdevil.playground.view.scroll.tradition.TraditionParentInterceptActivity

class ScrollMainDemoActivity : AppCompatActivity(R.layout.activity_scroll_main_demo) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupButtonClick(R.id.parent_intercept, TraditionParentInterceptActivity::class.java)
        setupButtonClick(R.id.nested_scrolling_parent, NestedScrollingParentActivity::class.java)
        setupButtonClick(R.id.nested_scrolling_parent2, NestedScrollingParent2Activity::class.java)
        setupButtonClick(
            R.id.nested_scrolling_parent2_collapsing,
            NestedScrollingParent2CollapsingActivity::class.java
        )
        setupButtonClick(R.id.nested_scrolling_coordinator, CoordinatorLayoutActivity::class.java)
        setupButtonClick(R.id.app_bar_layout, AppbarActivity::class.java)
        setupButtonClick(R.id.view_offset, ViewOffsetActivity::class.java)
    }
}