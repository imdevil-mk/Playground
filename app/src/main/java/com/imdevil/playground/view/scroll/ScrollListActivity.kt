package com.imdevil.playground.view.scroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imdevil.playground.R

class ScrollListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_scroll_list_with_nestedscrollview)
        setContentView(R.layout.activity_scroll_list_with_scrollablelinearlayout)

        val rv = findViewById<RecyclerView>(R.id.list)
        rv.layoutManager = LinearLayoutManager(baseContext)
        rv.adapter = DemoListAdapter().apply {
            submitList(prepareDemoData())
        }
    }
}