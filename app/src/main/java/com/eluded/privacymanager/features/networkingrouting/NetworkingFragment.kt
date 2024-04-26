package com.eluded.privacymanager.features.networkingrouting

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eluded.privacymanager.Preferences
import com.eluded.privacymanager.databinding.FragmentNetworkingBinding
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
            routingmodeForcetor.isEnabled = false;
            routingmodeForcevpn.isEnabled = false;
            routingmodeForcevpnandtor.isEnabled = false;

        }
    }

    private fun setup() = binding.apply {
        toggleNetworking.setOnCheckedChangeListener { _, isChecked ->
            prefs.toggleNetworking = isChecked
            routingmodeForcetor.isEnabled = prefs.toggleNetworking
            routingmodeForcevpn.isEnabled = prefs.toggleNetworking
            routingmodeForcevpnandtor.isEnabled = prefs.toggleNetworking

            Intent().also { intent ->
                if(isChecked) {
                    intent.setAction("org.calyxos.datura.action.enable")
                } else {
                    intent.setAction("org.calyxos.datura.action.disable")
                }
                ctx.sendBroadcast(intent, "org.calyxos.datura.permission.CHANGE_SETTINGS")
            }
        }

        routingmodeForcetor.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked && prefs.toggleNetworking){
                Intent().also { intent ->
                    Log.d("TAG", "Send Force Tor")
                    intent.setAction("org.calyxos.datura.action.forceTor")
                    intent.setPackage("org.calyxos.datura")
                    ctx.sendBroadcast(intent)
                }
            }
        }

        routingmodeForcevpn.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked && prefs.toggleNetworking) {
                Intent().also { intent ->
                    Log.d("TAG", "Send Force VPN")

                    intent.setAction("org.calyxos.datura.action.forceVPN")
                    ctx.sendBroadcast(intent, "org.calyxos.datura.permission.CHANGE_SETTINGS")
                }
            }
        }
    }
}