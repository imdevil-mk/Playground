package com.imdevil.playground.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.imdevil.playground.base.LogFragment
import com.imdevil.playground.databinding.FragmentColorBinding

private const val ARG_TIPS = "tips"
private const val ARG_COLOR = "color"

class ColorLogFragment : LogFragment() {
    private var tips: String? = null
    private var color: Int? = null

    private lateinit var binding: FragmentColorBinding

    override fun getLogTag() = "Fragment-${this.javaClass.simpleName}-${this.hashCode()}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tips = it.getString(ARG_TIPS)
            color = it.getInt(ARG_COLOR)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentColorBinding.inflate(layoutInflater, container, false)

        binding.tips.text = tips
        color?.let { binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), it)) }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(tips: String, color: Int) =
            ColorLogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TIPS, tips)
                    putInt(ARG_COLOR, color)
                }
            }
    }
}