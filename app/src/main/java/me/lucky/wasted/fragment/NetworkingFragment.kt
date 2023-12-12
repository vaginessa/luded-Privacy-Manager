package me.lucky.wasted.fragment

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import androidx.fragment.app.Fragment
import me.lucky.wasted.Preferences
import me.lucky.wasted.R
import me.lucky.wasted.Utils
import me.lucky.wasted.admin.DeviceAdminManager
import me.lucky.wasted.databinding.FragmentNetworkingBinding
import java.util.*


class NetworkingFragment : Fragment() {
    private lateinit var binding: FragmentNetworkingBinding
    private lateinit var ctx: Context
    private lateinit var prefs: Preferences
    private lateinit var prefsdb: Preferences

    private val prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        prefs.copyTo(prefsdb, key)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNetworkingBinding.inflate(inflater, container, false)
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
            routingmodeForcetor.isEnabled = prefs.toggleNetworking
            routingmodeForcevpn.isEnabled = prefs.toggleNetworking
            routingmodeForcevpnandtor.isEnabled = prefs.toggleNetworking

        }
    }

    private fun setup() = binding.apply {
        toggleNetworking.setOnCheckedChangeListener { _, isChecked ->
            prefs.toggleNetworking = isChecked
            routingmodeForcetor.isEnabled = prefs.toggleNetworking
            routingmodeForcevpn.isEnabled = prefs.toggleNetworking
            routingmodeForcevpnandtor.isEnabled = prefs.toggleNetworking
        }
    }
}