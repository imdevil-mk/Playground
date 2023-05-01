package com.imdevil.playground.view.scroll.coordinatorLayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.imdevil.playground.R
import com.imdevil.playground.view.scroll.DemoListAdapter
import com.imdevil.playground.view.scroll.obtainSimpleListData

class CoordinatorLayoutActivity : AppCompatActivity(R.layout.activity_coordinator_layout) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = findViewById<RecyclerView>(R.id.list)
        list.adapter = DemoListAdapter().apply {
            submitList(obtainSimpleListData("Behavior嵌套滑动" ?: getString(R.string.app_name)))
        }
    }
}