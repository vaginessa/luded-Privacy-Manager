package com.eluded.privacymanager.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eluded.privacymanager.Preferences
import com.eluded.privacymanager.R
import com.eluded.privacymanager.Utils
import com.eluded.privacymanager.admin.DeviceAdminManager
import com.eluded.privacymanager.databinding.FragmentStatusBinding
import java.util.Locale
import android.content.pm.PackageManager
import android.net.Uri


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
            val isEncrypted: Boolean =
                utils.getSystemProperty("ro.crypto.state", "").lowercase(Locale.ROOT) == "encrypted"
            isDeviceEncrypted.isChecked = isEncrypted
            isPanicWipeEnabled.isChecked = (prefs.isWipeData && (isWipeOnNotification || isWipeOnUSB ));
            if(!utils.isPackageInstalled("org.calyxos.datura", requireActivity().packageManager)) {
                isNetworkRoutingEnabled.visibility = View.GONE;
            }

            gitbutton.setOnClickListener{
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/eluded-smartphones/"))
                startActivity(browserIntent);
            }
        }
    }

    private fun setup() = binding.apply {


    }
}