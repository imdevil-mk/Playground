package com.imdevil.playground.fragment.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.imdevil.playground.R
import com.imdevil.playground.base.LogFragment
import com.imdevil.playground.databinding.FragmentNotificationsBinding

class NotificationsFragment : LogFragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private val notificationsViewModel: NotificationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        binding.textNotifications.text = this.toString()
        binding.textNotifications.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_notifications_to_notification_child)
        }

        return binding.root
    }
}