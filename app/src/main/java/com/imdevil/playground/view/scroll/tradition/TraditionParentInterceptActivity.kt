package com.imdevil.playground.view.scroll.tradition

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.imdevil.playground.R
import com.imdevil.playground.view.scroll.DemoListAdapter
import com.imdevil.playground.view.scroll.obtainSimpleListData

class TraditionParentInterceptActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_scroll_list_with_nestedscrollview)
        setContentView(R.layout.activity_scroll_list_with_intercept_linearlayout)

        val rv = findViewById<RecyclerView>(R.id.list)
        rv.adapter = DemoListAdapter().apply {
            submitList(obtainSimpleListData("外部拦截法"))
        }
    }
}