package com.imdevil.playground.view.scroll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.imdevil.playground.R

class DemoListFragment : Fragment() {
    private var msg: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            msg = it.getString(ARG_MSG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_demo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = view.findViewById<RecyclerView>(R.id.list)
        list.adapter = DemoListAdapter().apply {
            submitList(obtainSimpleListData(msg ?: getString(R.string.app_name)))
        }
    }

    companion object {
        private const val ARG_MSG = "arg_msg"

        @JvmStatic
        fun newInstance(param1: String) =
            DemoListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MSG, param1)
                }
            }
    }
}