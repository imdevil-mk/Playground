package com.imdevil.playground.view.scroll.appbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.imdevil.playground.R
import com.imdevil.playground.view.scroll.DemoListAdapter
import com.imdevil.playground.view.scroll.obtainSimpleListData

class AppbarActivity : AppCompatActivity(R.layout.activity_app_bar) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = findViewById<RecyclerView>(R.id.list)
        list.adapter = DemoListAdapter().apply {
            submitList(obtainSimpleListData("AppBarLayout"))
        }
    }
}