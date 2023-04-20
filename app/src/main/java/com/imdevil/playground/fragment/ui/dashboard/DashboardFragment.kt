package com.imdevil.playground.fragment.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.imdevil.playground.base.LogFragment
import com.imdevil.playground.databinding.FragmentDashboardBinding

class DashboardFragment : LogFragment() {

    private lateinit var binding: FragmentDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        binding.textDashboard.text = this.toString()

        return binding.root
    }
}