package com.imdevil.playground.fragment.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.iterator
import androidx.fragment.app.viewModels
import com.imdevil.playground.base.LogFragment
import com.imdevil.playground.databinding.FragmentHomeBinding
import java.util.Deque
import java.util.LinkedList

class HomeFragment : LogFragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //binding.textHome.text = this.toString()
        binding.traversal.setOnClickListener {
            traversal(binding.root)
        }
        binding.noTraversal.setOnClickListener {
            noTraversal(binding.root)
        }

        return binding.root
    }

    private fun traversal(root: ViewGroup) {
        val it = root.children.iterator()

        while (it.hasNext()) {
            val child = it.next()
            if (child is TextView) {
                child.text = "traversal"
            } else if (child is ViewGroup) {
                traversal(child)
            }
        }
    }

    private fun traversal(it: Iterator<View>) {
        while (it.hasNext()) {
            val child = it.next()
            if (child is TextView) {
                child.text = "fuck"
            } else if (child is ViewGroup) {
                traversal(child)
            }
        }
    }

    private fun noTraversal(root: ViewGroup) {
        val stack: Deque<View> = LinkedList()

        stack.push(root)

        while (stack.isNotEmpty()) {
            val visit = stack.pop()
            if (visit is TextView) {
                visit.text = "noTraversal"
            } else if (visit is ViewGroup) {
                val it = visit.iterator()
                while (it.hasNext()) {
                    stack.push(it.next())
                }
            }
        }
    }
}