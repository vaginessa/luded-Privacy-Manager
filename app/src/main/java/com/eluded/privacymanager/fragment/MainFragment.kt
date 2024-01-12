package com.eluded.privacymanager.fragment

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eluded.privacymanager.fragment.*

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.eluded.privacymanager.Preferences
import com.eluded.privacymanager.R
import com.eluded.privacymanager.Trigger
import com.eluded.privacymanager.Utils
import com.eluded.privacymanager.admin.DeviceAdminManager
import com.eluded.privacymanager.databinding.FragmentMainBinding
import java.util.*

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
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
        binding = FragmentMainBinding.inflate(inflater, container, false)
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
        if (prefs.secret.isEmpty()) prefs.secret = makeSecret()
        binding.apply {
            secret.text = prefs.secret
            secret.setBackgroundColor(ctx.getColor(
                if (prefs.triggers != 0) R.color.secret_1 else R.color.secret_0
            ))
            wipeData.isChecked = prefs.isWipeData
            wipeEmbeddedSim.isChecked = prefs.isWipeEmbeddedSim
            wipeEmbeddedSim.isEnabled = wipeData.isChecked
            wipeFromCode.isChecked = prefs.triggers.and(Trigger.NOTIFICATION.value) != 0
            wipeOnUSB.isChecked = prefs.triggers.and(Trigger.USB.value) != 0
            wipeData.isEnabled = wipeOnUSB.isChecked || wipeFromCode.isChecked
            toggle.isChecked = prefs.isEnabled
        }
    }

    private fun setup() = binding.apply {
        wipeOnUSB.setOnCheckedChangeListener { _, isChecked ->
            prefs.wipeOnUSB = isChecked
            wipeData.isEnabled = isChecked || wipeFromCode.isChecked
            prefs.triggers = Utils.setFlag(prefs.triggers, Trigger.USB.value, isChecked)
            utils.updateForegroundRequiredEnabled()
        }

        openSettings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment, SettingsFragment())
        }

        wipeFromCode.setOnCheckedChangeListener { _, isChecked ->
            wipeData.isEnabled = isChecked || wipeOnUSB.isChecked
            prefs.triggers =
                Utils.setFlag(prefs.triggers, Trigger.NOTIFICATION.value, isChecked)
            utils.setNotificationEnabled(isChecked && prefs.isEnabled)
        }


        wipeData.setOnCheckedChangeListener { _, isChecked ->
            prefs.isWipeData = isChecked
            wipeEmbeddedSim.isEnabled = isChecked
        }

        wipeEmbeddedSim.setOnCheckedChangeListener { _, isChecked ->
            prefs.isWipeEmbeddedSim = isChecked
        }

        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) requestAdmin() else setOff()
        }


    }

    private fun setOn() {
        prefs.isEnabled = true
        Utils(ctx).setEnabled(true)
        binding.toggle.isChecked = true
    }

    private fun setOff() {
        prefs.isEnabled = false
        Utils(ctx).setEnabled(false)
        try { admin.remove() } catch (exc: SecurityException) {}
        binding.toggle.isChecked = false
    }

    private val registerForDeviceAdmin =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) setOn() else setOff()
        }

    private fun requestAdmin() = registerForDeviceAdmin.launch(admin.makeRequestIntent())
    private fun makeSecret() = UUID.randomUUID().toString()
}