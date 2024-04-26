package com.eluded.privacymanager.features.panickwipe

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.eluded.privacymanager.Preferences
import com.eluded.privacymanager.Utils
import com.eluded.privacymanager.admin.DeviceAdminManager
import com.eluded.privacymanager.databinding.FragmentTriggerSettingsListBinding
import com.eluded.privacymanager.features.panickwipe.trigger.PanickTrigger
import com.eluded.privacymanager.features.panickwipe.trigger.application.ApplicationFragment
import com.eluded.privacymanager.features.panickwipe.trigger.lock.LockFragment
import com.eluded.privacymanager.features.panickwipe.trigger.notification.NotificationFragment
import com.eluded.privacymanager.features.panickwipe.trigger.tile.TileFragment

class TriggerSettingsFragment : Fragment() {
    private lateinit var binding: FragmentTriggerSettingsListBinding
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
        binding = FragmentTriggerSettingsListBinding.inflate(inflater, container, false)
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
    }

    private fun setup() = binding.apply {
        val dataset = listOf(
            PanickTrigger(TileFragment(), "Tile Trigger"),
            PanickTrigger(
                LockFragment(), "Lock Trigger"
            ),
            PanickTrigger(NotificationFragment(), "Notification Trigger"),
            PanickTrigger(ApplicationFragment(), "Application Trigger")
        )
        val customAdapter = PanickTriggerRecyclerAdapter(dataset, ctx)

        val recyclerView: RecyclerView = binding.triggerlist
        recyclerView.adapter = customAdapter
    }
}