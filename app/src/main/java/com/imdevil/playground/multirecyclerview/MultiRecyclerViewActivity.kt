package com.imdevil.playground.multirecyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.imdevil.playground.databinding.ActivityMultiRecyclerViewBinding

class MultiRecyclerViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMultiRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = MultiViewsAdapter()
        adapter.registerItemBuilder(FooItemBuilder())
        adapter.registerItemBuilder(BarItemBuilder())

        val data = listOf(
            FooData("foo", 1),
            BarData("bar"),
            BarData("bar1"),
            BarData("bar2"),
            FooData("foo", 1),
            BarData("bar"),
            BarData("bar1"),
            BarData("bar2"),
            FooData("foo", 1),
            BarData("bar"),
            BarData("bar1"),
            BarData("bar2"),
            FooData("foo", 1),
            BarData("bar"),
            BarData("bar1"),
            BarData("bar2"),
            FooData("foo", 1),
            BarData("bar"),
            BarData("bar1"),
            BarData("bar2"),
            FooData("foo", 1),
            BarData("bar"),
            BarData("bar1"),
            BarData("bar2"),
            FooData("foo", 1),
            BarData("bar"),
            BarData("bar1"),
            BarData("bar2"),
            FooData("foo", 1),
            BarData("bar"),
            BarData("bar1"),
            BarData("bar2"),
            FooData("foo", 1),
            BarData("bar"),
            BarData("bar1"),
            BarData("bar2"),
            FooData("foo", 1),
            BarData("bar"),
            BarData("bar1"),
            BarData("bar2"),
            FooData("foo", 1),
            BarData("bar"),
            BarData("bar1"),
            BarData("bar2"),
            FooData("foo", 1),
            BarData("bar"),
            BarData("bar1"),
            BarData("bar2"),
        )

        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)

        adapter.submitList(data)

    }
}