package com.imdevil.playground.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imdevil.playground.R
import com.imdevil.playground.multirecyclerview.*

const val ARG_OBJECT = "object"

// Instances of this class are fragments representing a single
// object in our collection.
class DemoObjectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_collection_object, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            val textView: TextView = view.findViewById(android.R.id.text1)
            textView.text = getInt(ARG_OBJECT).toString()

            textView.setOnClickListener {
                Toast.makeText(requireContext(), textView.text, Toast.LENGTH_SHORT).show()
                StateHolder.checking = !StateHolder.checking
            }

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

            val list: RecyclerView = view.findViewById(R.id.list)
            list.adapter = adapter
            list.layoutManager = LinearLayoutManager(requireContext())
            adapter.submitList(data)
        }
    }
}