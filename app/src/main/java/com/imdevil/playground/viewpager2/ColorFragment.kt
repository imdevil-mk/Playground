package com.imdevil.playground.viewpager2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.imdevil.playground.databinding.FragmentColorBinding

private const val ARG_TIPS = "tips"
private const val ARG_COLOR = "color"

class ColorFragment : Fragment() {
    private var tips: String? = null
    private var color: Int? = null

    private lateinit var binding: FragmentColorBinding

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
    ): View? {
        binding = FragmentColorBinding.inflate(layoutInflater, container, false)

        binding.tips.text = tips
        color?.let { binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), it)) }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(tips: String, color: Int) =
            ColorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TIPS, tips)
                    putInt(ARG_COLOR, color)
                }
            }
    }
}