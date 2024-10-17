package com.imdevil.playground.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.imdevil.playground.R
import com.imdevil.playground.view.scroll.DemoListAdapter
import com.imdevil.playground.view.scroll.DemoListFragment
import com.imdevil.playground.view.scroll.obtainSimpleListData

class DemoNestedScrollableListFragment : Fragment() {
    private var msg: String? = null
    private var color: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            msg = it.getString(ARG_MSG)
            color = it.getInt(ARG_COLOR)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_demo_nested_scrollable_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        color?.let {
            view.findViewById<FrameLayout>(R.id.root).setBackgroundResource(it)
        }
        val list = view.findViewById<RecyclerView>(R.id.list)
        list.adapter = DemoListAdapter().apply {
            submitList(obtainSimpleListData(msg ?: getString(R.string.app_name)))
        }
    }

    companion object {
        private const val ARG_MSG = "arg_msg"
        private const val ARG_COLOR = "arg_color"

        @JvmStatic
        fun newInstance(param1: String) =
            DemoListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MSG, param1)
                    putInt(ARG_COLOR, R.color.mediumpurple)
                }
            }

        @JvmStatic
        fun newInstance(param1: String, color: Int) =
            DemoListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MSG, param1)
                    putInt(ARG_COLOR, color)
                }
            }
    }
}