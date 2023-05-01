package com.imdevil.playground.viewpager2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.imdevil.playground.R
import com.imdevil.playground.databinding.FragmentViewPager2NestedScrollBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_COLOR = "color"

class ViewPager2Fragment : Fragment() {
    private var param1: String? = null
    private var color: Int? = null

    private val fragmentCreators = listOf(
        {
            ColorFragment.newInstance(
                "CHILD_ONE",
                R.color.peachpuff
            )
        },
        {
            ColorFragment.newInstance(
                "CHILD_TWO",
                R.color.azure
            )
        },
        {
            ColorFragment.newInstance(
                "CHILD_THREE",
                R.color.firebrick
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            color = it.getInt(ARG_COLOR)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentViewPager2NestedScrollBinding.inflate(inflater, container, false)

        binding.topTips.text = param1
        color?.let { binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), it)) }

        binding.viewPager.adapter =
            ViewPager2Adapter(childFragmentManager, lifecycle, fragmentCreators)

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(top: String, color: Int) =
            ViewPager2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, top)
                    putInt(ARG_COLOR, color)
                }
            }
    }
}