package com.eluded.privacymanager.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import com.eluded.privacymanager.Preferences
import com.eluded.privacymanager.Trigger
import com.eluded.privacymanager.Utils
import com.eluded.privacymanager.admin.DeviceAdminManager
import com.eluded.privacymanager.databinding.FragmentStatusBinding

class StatusFragment : Fragment() {
    private lateinit var binding: FragmentStatusBinding
    private lateinit var ctx: Context
    private lateinit var prefs: Preferences
    private lateinit var prefsdb: Preferences
    private val admin by lazy { DeviceAdminManager(ctx) }
    private val utils by lazy { Utils(ctx) }

    private val prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        prefs.copyTo(prefsdb, key)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStatusBinding.inflate(inflater, container, false)
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
            var isWipeOnNotification = utils.getWipeOnNotification();
            var isWipeOnUSB = utils.getWipeOnUSB();
            isPanicWipeEnabled.isChecked = (prefs.isWipeData && (isWipeOnNotification || isWipeOnUSB ));
        }
    }

    private fun setup() = binding.apply {


    }

}