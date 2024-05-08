package com.eluded.privacymanager.features.panickwipe.trigger.usb

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eluded.privacymanager.Preferences
import com.eluded.privacymanager.R
import com.eluded.privacymanager.databinding.FragmentTileBinding
import com.eluded.privacymanager.databinding.FragmentUsbTriggerSettingsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [USBTriggerSettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class USBTriggerSettingsFragment : Fragment() {
    private lateinit var binding: FragmentUsbTriggerSettingsBinding
    private lateinit var ctx: Context
    private lateinit var prefs: Preferences
    private lateinit var prefsdb: Preferences
    public lateinit var name: String

    private val prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        prefs.copyTo(prefsdb, key)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentUsbTriggerSettingsBinding.inflate(inflater, container, false)
        init()
        setup()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        prefs.registerListener(prefsListener)
    }

    override fun onStop() {
        super.onStop()
        prefs.unregisterListener(prefsListener)
    }

    private fun init() {
        ctx = requireContext()
        prefs = Preferences(ctx)
        prefsdb = Preferences(ctx, encrypted = false)
        binding.apply {
            toggle.isChecked = prefs.wipeOnUSB;
            triggerwhenlocked.isChecked = prefs.wipeOnUSBOnlyWhenLocked
        }
    }

    private fun setup() = binding.apply {
        toggle.setOnClickListener{
            prefs.wipeOnUSB = toggle.isChecked;
        }

        triggerwhenlocked.setOnClickListener{
            prefs.wipeOnUSBOnlyWhenLocked = triggerwhenlocked.isChecked;
        }
    }
}