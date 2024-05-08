package com.eluded.privacymanager.features.panickwipe.trigger.notification

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eluded.privacymanager.Utils

import com.eluded.privacymanager.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        setup()
        return binding.root
    }

    private fun setup() = binding.apply {
        var utils = Utils(requireContext())
        toggle.isChecked = utils.getWipeOnNotification();
        toggle.setOnCheckedChangeListener { _, isChecked ->
            utils.setNotificationEnabled(isChecked)
            utils.updateWipeOnNotification(isChecked)
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }
}